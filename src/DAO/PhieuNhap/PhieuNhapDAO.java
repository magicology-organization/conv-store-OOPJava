/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.PhieuNhap;

import ConnectDB.ConnectDB;
import Entity.NhanVien.NhanVien;
import Entity.PhieuNhap.NhaCungCap;
import Entity.PhieuNhap.PhieuNhap;
import java.math.BigDecimal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuNhapDAO {
    private final Connection conn;

    public PhieuNhapDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<PhieuNhap> findAll() {
        String sql = "SELECT maPN, maNV, maNCC, thoiGian FROM PhieuNhap ORDER BY maPN";
        List<PhieuNhap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> findAllWithDetails() {
        String sql = """
                    SELECT pn.maPN,
                           ncc.tenNCC,
                           ncc.SDT       AS soDienThoai,
                           nv.tenNV,
                           pn.thoiGian,
                           ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongHoaDon
                    FROM PhieuNhap pn
                    JOIN NhaCungCap ncc ON ncc.maNCC = pn.maNCC
                    JOIN NhanVien   nv  ON nv.maNV   = pn.maNV
                    LEFT JOIN CTPhieuNhap ct ON ct.maPN = pn.maPN
                    GROUP BY pn.maPN, ncc.tenNCC, ncc.SDT, nv.tenNV, pn.thoiGian
                    ORDER BY pn.maPN ASC
                """;

        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDateTime tg = rs.getTimestamp("thoiGian").toLocalDateTime();
                BigDecimal tong = rs.getBigDecimal("tongHoaDon");
                out.add(new Object[] {
                        rs.getString("maPN"),
                        rs.getString("tenNCC"),
                        rs.getString("soDienThoai"),
                        rs.getString("tenNV"),
                        tg,
                        tong
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public String taoMaPhieuNhap() {
        final String prefix = "PN-";
        final String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maPN, 4, 10) AS INT)), 0) AS maxNo " +
                "FROM PhieuNhap WHERE maPN LIKE 'PN-%'";

        int next = 1;
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                next = rs.getInt("maxNo") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tạo mã phiếu nhập", e);
        }

        return prefix + String.format("%05d", next);
    }

    public Optional<PhieuNhap> findById(String maPN) {
        if (isBlank(maPN))
            return Optional.empty();
        String sql = "SELECT maPN, maNV, maNCC, thoiGian FROM PhieuNhap WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Object[]> findByMaNCC(String maNCC) {
        List<Object[]> list = new ArrayList<>();

        String sql = """
                    SELECT
                        pn.maPN,
                        nv.tenNV,
                        pn.thoiGian,
                        ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongHoaDon
                    FROM PhieuNhap pn
                    JOIN NhanVien nv ON nv.maNV = pn.maNV
                    LEFT JOIN CTPhieuNhap ct ON ct.maPN = pn.maPN
                    WHERE pn.maNCC = ?
                    GROUP BY pn.maPN, nv.tenNV, pn.thoiGian
                    ORDER BY pn.maPN ASC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maPN = rs.getString("maPN");
                String tenNV = rs.getString("tenNV");
                java.sql.Timestamp tg = rs.getTimestamp("thoiGian");
                java.time.LocalDateTime thoiGian = tg != null ? tg.toLocalDateTime() : null;
                double tong = rs.getDouble("tongHoaDon");

                list.add(new Object[] { maPN, tenNV, thoiGian, tong });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Object[]> search(String maPN, String tenNCC, String sdt,
            java.util.Date tuNgay, java.util.Date denNgay) {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT pn.maPN,
                           ncc.tenNCC,
                           ncc.SDT AS soDienThoai,
                           nv.tenNV,
                           pn.thoiGian,
                           ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongTien
                    FROM PhieuNhap pn
                    JOIN NhaCungCap ncc ON ncc.maNCC = pn.maNCC
                    JOIN NhanVien nv ON nv.maNV = pn.maNV
                    LEFT JOIN CTPhieuNhap ct ON ct.maPN = pn.maPN
                    WHERE pn.maPN LIKE ?
                      AND ncc.tenNCC LIKE ?
                      AND ncc.SDT LIKE ?
                """);

        if (tuNgay != null)
            sql.append(" AND CAST(pn.thoiGian AS DATE) >= ? ");
        if (denNgay != null)
            sql.append(" AND CAST(pn.thoiGian AS DATE) <= ? ");

        sql.append("""
                    GROUP BY pn.maPN, ncc.tenNCC, ncc.SDT, nv.tenNV, pn.thoiGian
                    ORDER BY pn.maPN ASC
                """);

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            ps.setString(i++, "%" + maPN + "%");
            ps.setString(i++, "%" + tenNCC + "%");
            ps.setString(i++, "%" + sdt + "%");
            if (tuNgay != null)
                ps.setDate(i++, new java.sql.Date(tuNgay.getTime()));
            if (denNgay != null)
                ps.setDate(i++, new java.sql.Date(denNgay.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] {
                            rs.getString("maPN"),
                            rs.getString("tenNCC"),
                            rs.getString("soDienThoai"),
                            rs.getString("tenNV"),
                            rs.getTimestamp("thoiGian").toLocalDateTime(),
                            rs.getBigDecimal("tongTien")
                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean insert(PhieuNhap pn) {
        if (pn == null || isBlank(pn.getMaPN()))
            return false;

        String sql = "INSERT INTO PhieuNhap(maPN, maNV, maNCC, thoiGian) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPN());
            ps.setString(2, pn.getNhanVien() != null ? pn.getNhanVien().getMaNV() : null);
            ps.setString(3, pn.getNCC() != null ? pn.getNCC().getMaNCC() : null);
            setDateTimeOrNull(ps, 4, pn.getThoiGian());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(PhieuNhap pn) {
        if (pn == null || isBlank(pn.getMaPN()))
            return false;

        String sql = "UPDATE PhieuNhap SET maNV=?, maNCC=?, thoiGian=? WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getNhanVien() != null ? pn.getNhanVien().getMaNV() : null);
            ps.setString(2, pn.getNCC() != null ? pn.getNCC().getMaNCC() : null);
            setDateTimeOrNull(ps, 3, pn.getThoiGian());
            ps.setString(4, pn.getMaPN());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maPN) {
        if (isBlank(maPN))
            return false;
        String sql = "DELETE FROM PhieuNhap WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<PhieuNhap> thongTinIn(String maPN) {
        String sql = """
                    SELECT pn.maPN, pn.thoiGian,
                           nv.maNV, nv.tenNV,
                           ncc.maNCC, ncc.tenNCC, ncc.SDT
                    FROM PhieuNhap pn
                    LEFT JOIN NhanVien nv ON nv.maNV = pn.maNV
                    LEFT JOIN NhaCungCap ncc ON ncc.maNCC = pn.maNCC
                    WHERE pn.maPN = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhieuNhap pn = new PhieuNhap();
                    pn.setMaPN(rs.getString("maPN"));
                    pn.setThoiGian(rs.getTimestamp("thoiGian").toLocalDateTime());

                    // Nhân viên
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("maNV"));
                    nv.setTenNV(rs.getString("tenNV"));
                    pn.setNhanVien(nv);

                    // Nhà cung cấp
                    NhaCungCap ncc = new NhaCungCap();
                    ncc.setMaNCC(rs.getString("maNCC"));
                    ncc.setTenNCC(rs.getString("tenNCC"));
                    ncc.setSdt(rs.getString("SDT"));
                    pn.setNCC(ncc);

                    return Optional.of(pn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static PhieuNhap mapRow(ResultSet rs) throws SQLException {
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(rs.getString("maPN"));

        // Gán các FK dưới dạng đối tượng
        NhanVien nv = new NhanVien();
        nv.setMaNV(rs.getString("maNV"));
        pn.setNhanVien(nv);

        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNCC(rs.getString("maNCC"));
        pn.setNCC(ncc);

        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null) {
            pn.setThoiGian(ts.toLocalDateTime());
        }

        return pn;
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null)
            ps.setNull(idx, Types.TIMESTAMP);
        else
            ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

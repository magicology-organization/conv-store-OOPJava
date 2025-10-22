package DAO.HoaDon;

import ConnectDB.ConnectDB;
import Entity.HoaDon.HoaDon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoaDonDAO {
    private final Connection conn;

    public HoaDonDAO() {
        this.conn = ConnectDB.getConnection();
    }

    /* ===================== CRUD ===================== */

    public List<HoaDon> findAll() {
        String sql = """
                    SELECT maHD, maNV, maKH, thoiGian, kieuThanhToan
                    FROM HoaDon
                    ORDER BY maHD
                """;
        List<HoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trả về danh sách tổng hợp để load lên bảng:
     * [0] maHD
     * [1] tenKH
     * [2] soDienThoai
     * [3] thoiGian (LocalDateTime)
     * [4] tenNV
     * [5] tongHoaDon (BigDecimal)
     * [6] giamGia (BigDecimal)
     * [7] tongTien (BigDecimal)
     * [8] kieuThanhToan
     */
    public String taoMaHoaDon() {
        final String prefix = "HD-";
        final String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maHD, 4, 10) AS INT)), 0) AS maxNo " +
                "FROM HoaDon WHERE maHD LIKE 'HD-%'";

        int next = 1;
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                next = rs.getInt("maxNo") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tạo mã hóa đơn", e);
        }

        return prefix + String.format("%05d", next);
    }

    public List<Object[]> findAllWithDetails() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
                    SELECT  h.maHD,
                            kh.tenKH,
                            kh.SDT AS soDienThoai,
                            nv.tenNV,
                            h.thoiGian,
                            h.kieuThanhToan,
                            ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongHoaDon,
                            ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongTien
                    FROM HoaDon h
                    JOIN KhachHang kh ON kh.maKH = h.maKH
                    JOIN NhanVien  nv ON nv.maNV = h.maNV
                    LEFT JOIN CTHoaDon ct ON ct.maHD = h.maHD
                    GROUP BY h.maHD, kh.tenKH, kh.SDT, nv.tenNV, h.thoiGian, h.kieuThanhToan
                    ORDER BY h.maHD ASC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("maHD"), // 0
                        rs.getString("tenKH"), // 1
                        rs.getString("soDienThoai"), // 2
                        rs.getTimestamp("thoiGian").toLocalDateTime(), // 3
                        rs.getString("tenNV"), // 4
                        rs.getBigDecimal("tongHoaDon"), // 5
                        rs.getBigDecimal("tongTien"), // 6
                        rs.getString("kieuThanhToan") // 7
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Optional<HoaDon> findById(String maHD) {
        String sql = """
                    SELECT maHD, maNV, maKH, thoiGian, kieuThanhToan
                    FROM HoaDon WHERE maHD = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(HoaDon hd) {
        String sql = """
                    INSERT INTO HoaDon(maHD, maNV, maKH, thoiGian, kieuThanhToan)
                    VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaNV());
            ps.setString(3, hd.getMaKH());
            setTimestampOrNull(ps, 4, hd.getThoiGian());
            ps.setString(5, hd.getKieuThanhToan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(HoaDon hd) {
        String sql = """
                    UPDATE HoaDon
                    SET maNV=?, maKH=?, thoiGian=?, kieuThanhToan=?
                    WHERE maHD=?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaNV());
            ps.setString(2, hd.getMaKH());
            setTimestampOrNull(ps, 3, hd.getThoiGian());
            ps.setString(4, hd.getKieuThanhToan());
            ps.setString(5, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== Helpers ===================== */

    private HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaNV(rs.getString("maNV"));
        hd.setMaKH(rs.getString("maKH"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null)
            hd.setThoiGian(ts.toLocalDateTime());
        hd.setKieuThanhToan(rs.getString("kieuThanhToan"));
        // Nếu entity HoaDon còn các field cũ (tongTruocGiam, giamGiaHoaDon,
        // maGiamGiaCodeSnap),
        // chỉ cần không set là được (không còn trong bảng).
        return hd;
    }

    private static void setTimestampOrNull(PreparedStatement ps, int idx, LocalDateTime ldt)
            throws SQLException {
        if (ldt != null)
            ps.setTimestamp(idx, Timestamp.valueOf(ldt));
        else
            ps.setNull(idx, Types.TIMESTAMP);
    }
}

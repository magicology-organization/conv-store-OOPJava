package DAO.SanPham;

import ConnectDB.ConnectDB;
import Entity.SanPham.SanPham;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class SanPhamDAO {
    private final Connection conn;

    public SanPhamDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<SanPham> findAll() {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham ORDER BY maSP";
        List<SanPham> list = new ArrayList<>();
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
                    SELECT sp.maSP,
                           sp.tenSP,
                           sp.moTaSP,
                           dm.tenDM       AS danhMuc,
                           xx.tenXX       AS xuatXu,
                           dvt.tenDVT     AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD         AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc   dm  ON dm.maDM   = sp.maDM
                    JOIN XuatXu    xx  ON xx.maXX   = sp.maXX
                    JOIN DonViTinh dvt ON dvt.maDVT = sp.maDVT
                    ORDER BY sp.maSP ASC
                """;
        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Object[] {
                        rs.getString("maSP"), // 0
                        rs.getString("tenSP"), // 1
                        rs.getString("moTaSP"), // 2
                        rs.getString("danhMuc"), // 3
                        rs.getString("xuatXu"), // 4
                        rs.getString("donViTinh"), // 5
                        rs.getBigDecimal("giaNhap"), // 6
                        rs.getBigDecimal("donGia"), // 7
                        rs.getInt("soLuong"), // 8
                        rs.getTimestamp("hsd") // 9 (có thể null)
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public List<Object[]> findAllWithDetailsByName(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim();
        String sql = """
                    SELECT sp.maSP,
                           sp.tenSP,
                           sp.moTaSP,
                           dm.tenDM       AS danhMuc,
                           xx.tenXX       AS xuatXu,
                           dvt.tenDVT     AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD         AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc   dm  ON dm.maDM   = sp.maDM
                    JOIN XuatXu    xx  ON xx.maXX   = sp.maXX
                    JOIN DonViTinh dvt ON dvt.maDVT = sp.maDVT
                    WHERE sp.tenSP LIKE ?
                    ORDER BY sp.tenSP ASC, sp.maSP ASC
                """;

        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Object[] {
                            rs.getString("maSP"),
                            rs.getString("tenSP"),
                            rs.getString("moTaSP"),
                            rs.getString("danhMuc"),
                            rs.getString("xuatXu"),
                            rs.getString("donViTinh"),
                            rs.getBigDecimal("giaNhap"),
                            rs.getBigDecimal("donGia"),
                            rs.getInt("soLuong"),
                            rs.getTimestamp("hsd") // 9
                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm sản phẩm theo tên!", e);
        }
        return out;
    }

    public List<Object[]> findAllWithDetailsByDanhMuc(String tenDM) {
        String sql = """
                    SELECT sp.maSP,
                           sp.tenSP,
                           sp.moTaSP,
                           dm.tenDM       AS danhMuc,
                           xx.tenXX       AS xuatXu,
                           dvt.tenDVT     AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD         AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc   dm  ON dm.maDM   = sp.maDM
                    JOIN XuatXu    xx  ON xx.maXX   = sp.maXX
                    JOIN DonViTinh dvt ON dvt.maDVT = sp.maDVT
                    WHERE dm.tenDM = ?
                    ORDER BY sp.tenSP ASC
                """;

        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Object[] {
                            rs.getString("maSP"),
                            rs.getString("tenSP"),
                            rs.getString("moTaSP"),
                            rs.getString("danhMuc"),
                            rs.getString("xuatXu"),
                            rs.getString("donViTinh"),
                            rs.getBigDecimal("giaNhap"),
                            rs.getBigDecimal("donGia"),
                            rs.getInt("soLuong"),
                            rs.getTimestamp("hsd")
                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lọc theo danh mục!", e);
        }
        return out;
    }

    public Optional<SanPham> findById(String maSP) {
        if (isBlank(maSP))
            return Optional.empty();
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(SanPham sp) {
        if (sp == null || isBlank(sp.getMaSP()))
            return false;
        String sql = "INSERT INTO SanPham(maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, sp, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SanPham sp) {
        if (sp == null || isBlank(sp.getMaSP()))
            return false;
        String sql = "UPDATE SanPham SET tenSP=?, anhSP=?, moTaSP=?, maDM=?, maDVT=?, maXX=?, soLuong=?, giaNhap=?, donGia=?, HSD=? WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, sp, true);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maSP) {
        if (isBlank(maSP))
            return false;
        String sql = "DELETE FROM SanPham WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SanPham> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE tenSP LIKE ? ORDER BY tenSP, maSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> findByDanhMuc(String maDM) {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE maDM=? ORDER BY tenSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> findExpiredBefore(LocalDateTime dt) {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE HSD < ? ORDER BY HSD, tenSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setDateTimeOrNull(ps, 1, dt);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean capNhatTonKhoSauBan(String maSP, int soLuongBan) {
        String sql = """
            UPDATE SanPham
            SET soLuong = soLuong - ?
            WHERE maSP = ? AND soLuong >= ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongBan);
            ps.setString(2, maSP);
            ps.setInt(3, soLuongBan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static SanPham mapRow(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setAnhSP(rs.getBytes("anhSP"));
        sp.setMoTaSP(rs.getString("moTaSP"));
        sp.setMaDM(rs.getString("maDM"));
        sp.setMaDVT(rs.getString("maDVT"));
        sp.setMaXX(rs.getString("maXX"));
        sp.setSoLuong(rs.getInt("soLuong"));
        sp.setGiaNhap(rs.getBigDecimal("giaNhap"));
        sp.setDonGia(rs.getBigDecimal("donGia"));
        Timestamp ts = rs.getTimestamp("HSD");
        if (ts != null)
            sp.setHsd(ts.toLocalDateTime());
        return sp;
    }

    private static void fillParams(PreparedStatement ps, SanPham sp, boolean forUpdate) throws SQLException {
        if (forUpdate) {
            ps.setString(1, nullToEmpty(sp.getTenSP()));
            setBytesOrNull(ps, 2, sp.getAnhSP());
            ps.setString(3, nullToEmpty(sp.getMoTaSP()));
            ps.setString(4, nullToEmpty(sp.getMaDM()));
            ps.setString(5, nullToEmpty(sp.getMaDVT()));
            ps.setString(6, nullToEmpty(sp.getMaXX()));
            ps.setInt(7, sp.getSoLuong());
            ps.setBigDecimal(8, sp.getGiaNhap());
            ps.setBigDecimal(9, sp.getDonGia());
            setDateTimeOrNull(ps, 10, sp.getHsd());
            ps.setString(11, sp.getMaSP());
        } else {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, nullToEmpty(sp.getTenSP()));
            setBytesOrNull(ps, 3, sp.getAnhSP());
            ps.setString(4, nullToEmpty(sp.getMoTaSP()));
            ps.setString(5, nullToEmpty(sp.getMaDM()));
            ps.setString(6, nullToEmpty(sp.getMaDVT()));
            ps.setString(7, nullToEmpty(sp.getMaXX()));
            ps.setInt(8, sp.getSoLuong());
            ps.setBigDecimal(9, sp.getGiaNhap());
            ps.setBigDecimal(10, sp.getDonGia());
            setDateTimeOrNull(ps, 11, sp.getHsd());
        }
    }

    private static void setBytesOrNull(PreparedStatement ps, int idx, byte[] bytes) throws SQLException {
        if (bytes == null)
            ps.setNull(idx, Types.VARBINARY);
        else
            ps.setBytes(idx, bytes);
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

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

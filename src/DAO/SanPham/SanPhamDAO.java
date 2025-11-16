package DAO.SanPham;

import ConnectDB.ConnectDB;
import Entity.SanPham.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
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
                           dm.tenDM AS danhMuc,
                           xx.tenXX AS xuatXu,
                           dvt.tenDVT AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc dm ON dm.maDM = sp.maDM
                    JOIN XuatXu xx ON xx.maXX = sp.maXX
                    JOIN DonViTinh dvt ON dvt.maDVT = sp.maDVT
                    ORDER BY sp.maSP ASC
                """;
        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
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
                           dm.tenDM AS danhMuc,
                           xx.tenXX AS xuatXu,
                           dvt.tenDVT AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc dm ON dm.maDM = sp.maDM
                    JOIN XuatXu xx ON xx.maXX = sp.maXX
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
                            rs.getTimestamp("hsd")
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
                           dm.tenDM AS danhMuc,
                           xx.tenXX AS xuatXu,
                           dvt.tenDVT AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc dm ON dm.maDM = sp.maDM
                    JOIN XuatXu xx ON xx.maXX = sp.maXX
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

        String sql = """
                    SELECT sp.maSP, sp.tenSP, sp.anhSP, sp.moTaSP,
                           sp.maDM, dm.tenDM,
                           sp.maDVT, dvt.tenDVT,
                           sp.maXX, xx.tenXX,
                           sp.soLuong, sp.giaNhap, sp.donGia, sp.HSD
                    FROM SanPham sp
                    JOIN DanhMuc dm ON sp.maDM = dm.maDM
                    JOIN DonViTinh dvt ON sp.maDVT = dvt.maDVT
                    JOIN XuatXu xx ON sp.maXX = xx.maXX
                    WHERE sp.maSP = ?
                """;

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

        if (sp.getAnhSP() == null) {
            findById(sp.getMaSP()).ifPresent(old -> sp.setAnhSP(old.getAnhSP()));
        }

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

    public List<Object[]> search(String maSP, String tenSP, String danhMuc, String xuatXu) {
        List<Object[]> list = new ArrayList<>();

        // --- Câu SQL động ---
        StringBuilder sql = new StringBuilder("""
                    SELECT sp.maSP,
                           sp.tenSP,
                           sp.moTaSP,
                           dm.tenDM AS danhMuc,
                           xx.tenXX AS xuatXu,
                           dvt.tenDVT AS donViTinh,
                           sp.giaNhap,
                           sp.donGia,
                           sp.soLuong,
                           sp.HSD AS hsd
                    FROM SanPham sp
                    JOIN DanhMuc dm ON dm.maDM = sp.maDM
                    JOIN XuatXu xx ON xx.maXX = sp.maXX
                    JOIN DonViTinh dvt ON dvt.maDVT = sp.maDVT
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        // --- Thêm điều kiện động ---
        if (maSP != null && !maSP.trim().isEmpty()) {
            sql.append(" AND sp.maSP LIKE ?");
            params.add("%" + maSP.trim() + "%");
        }
        if (tenSP != null && !tenSP.trim().isEmpty()) {
            sql.append(" AND sp.tenSP LIKE ?");
            params.add("%" + tenSP.trim() + "%");
        }
        if (danhMuc != null && !danhMuc.equalsIgnoreCase("Tất cả") && !danhMuc.isEmpty()) {
            sql.append(" AND dm.tenDM = ?");
            params.add(danhMuc);
        }
        if (xuatXu != null && !xuatXu.equalsIgnoreCase("Tất cả") && !xuatXu.isEmpty()) {
            sql.append(" AND xx.tenXX = ?");
            params.add(xuatXu);
        }

        sql.append(" ORDER BY sp.maSP ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // --- Gán tham số ---
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
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
                    };
                    list.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi tìm kiếm sản phẩm!", e);
        }

        return list;
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

    public List<SanPham> kiemTraSanPhamHetHan(LocalDateTime dt) {
        String sql = """
                    SELECT sp.maSP, sp.tenSP, sp.anhSP, sp.moTaSP,
                           sp.maDM, dm.tenDM,
                           sp.maDVT, dvt.tenDVT,
                           sp.maXX, xx.tenXX,
                           sp.soLuong, sp.giaNhap, sp.donGia, sp.HSD
                    FROM SanPham sp
                    JOIN DanhMuc dm ON sp.maDM = dm.maDM
                    JOIN DonViTinh dvt ON sp.maDVT = dvt.maDVT
                    JOIN XuatXu xx ON sp.maXX = xx.maXX
                    WHERE sp.HSD < ?
                    ORDER BY sp.HSD, sp.tenSP
                """;

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

    public boolean capNhatTonKhoSauNhap(String maSP, int soLuongNhap) {
        String sql = """
                    UPDATE SanPham
                    SET soLuong = soLuong + ?
                    WHERE maSP = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongNhap);
            ps.setString(2, maSP);
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

        DanhMuc dm = new DanhMuc();
        dm.setMaDM(rs.getString("maDM"));
        dm.setTenDM(rs.getString("tenDM"));
        sp.setDanhMuc(dm);

        DonViTinh dvt = new DonViTinh();
        dvt.setMaDVT(rs.getString("maDVT"));
        dvt.setTenDVT(rs.getString("tenDVT"));
        sp.setDonViTinh(dvt);

        XuatXu xx = new XuatXu();
        xx.setMaXX(rs.getString("maXX"));
        xx.setTenXX(rs.getString("tenXX"));
        sp.setXuatXu(xx);

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
            ps.setString(4, sp.getDanhMuc() != null ? sp.getDanhMuc().getMaDM() : null);
            ps.setString(5, sp.getDonViTinh() != null ? sp.getDonViTinh().getMaDVT() : null);
            ps.setString(6, sp.getXuatXu() != null ? sp.getXuatXu().getMaXX() : null);
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
            ps.setString(5, sp.getDanhMuc() != null ? sp.getDanhMuc().getMaDM() : null);
            ps.setString(6, sp.getDonViTinh() != null ? sp.getDonViTinh().getMaDVT() : null);
            ps.setString(7, sp.getXuatXu() != null ? sp.getXuatXu().getMaXX() : null);
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

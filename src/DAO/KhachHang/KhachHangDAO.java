package DAO.KhachHang;

import ConnectDB.ConnectDB;
import Entity.KhachHang.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO {
    private final Connection conn;

    public KhachHangDAO() {
        this.conn = ConnectDB.getConnection();
    }

    /* ===================== CRUD ===================== */

    public List<KhachHang> findAll() {
        String sql = "SELECT maKH, tenKH, gioiTinh, SDT FROM KhachHang ORDER BY maKH";
        List<KhachHang> list = new ArrayList<>();
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
                    SELECT maKH, tenKH, gioiTinh, sdt
                    FROM KhachHang
                    ORDER BY maKH ASC
                """;
        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String gt = rs.getString("gioiTinh");
                if (gt == null)
                    gt = "";
                else if (gt.equalsIgnoreCase("1") || gt.equalsIgnoreCase("true"))
                    gt = "Nam";
                else if (gt.equalsIgnoreCase("0") || gt.equalsIgnoreCase("false"))
                    gt = "Nữ";

                out.add(new Object[] {
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        gt,
                        rs.getString("sdt")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public String taoMaKH() {
        final String prefix = "KH-";
        final String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(maKH, 4, 10) AS INT)), 0) AS maxNo " +
                "FROM KhachHang WHERE maKH LIKE 'KH-%'";

        int next = 1;
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                next = rs.getInt("maxNo") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tạo mã khách hàng", e);
        }

        return prefix + String.format("%05d", next);
    }

    public List<KhachHang> search(String maKH, String tenKH, String sdt, String gioiTinh) {
        StringBuilder sql = new StringBuilder("""
                    SELECT maKH, tenKH, gioiTinh, SDT
                    FROM KhachHang
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        // Thêm điều kiện động
        if (maKH != null && !maKH.trim().isEmpty()) {
            sql.append(" AND maKH LIKE ?");
            params.add("%" + maKH.trim() + "%");
        }
        if (tenKH != null && !tenKH.trim().isEmpty()) {
            sql.append(" AND tenKH LIKE ?");
            params.add("%" + tenKH.trim() + "%");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND SDT LIKE ?");
            params.add("%" + sdt.trim() + "%");
        }
        if (gioiTinh != null && !gioiTinh.equalsIgnoreCase("Tất cả") && !gioiTinh.isEmpty()) {
            sql.append(" AND gioiTinh = ?");
            // Map trực tiếp Nam/Nữ hoặc 1/0 tùy DB
            if (gioiTinh.equalsIgnoreCase("Nam")) {
                params.add("Nam"); // hoặc "1" nếu DB lưu 1/0
            } else if (gioiTinh.equalsIgnoreCase("Nữ")) {
                params.add("Nữ"); // hoặc "0" nếu DB lưu 1/0
            }
        }

        sql.append(" ORDER BY maKH");

        List<KhachHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Gán tham số động
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    kh.setTenKH(rs.getString("tenKH"));

                    // Chuẩn hóa giới tính
                    Object raw = rs.getObject("gioiTinh");
                    if (raw instanceof Boolean)
                        kh.setGioiTinh(((Boolean) raw) ? "Nam" : "Nữ");
                    else if (raw instanceof Number)
                        kh.setGioiTinh(((Number) raw).intValue() == 1 ? "Nam" : "Nữ");
                    else {
                        String s = String.valueOf(raw);
                        if (s.equals("1") || s.equalsIgnoreCase("true"))
                            kh.setGioiTinh("Nam");
                        else if (s.equals("0") || s.equalsIgnoreCase("false"))
                            kh.setGioiTinh("Nữ");
                        else
                            kh.setGioiTinh(s);
                    }

                    kh.setSdt(rs.getString("SDT"));
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(maKH, tenKH, gioiTinh, SDT) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSdt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang findById(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSdt(rs.getString("sdt"));
                kh.setGioiTinh(rs.getString("gioiTinh"));
                return kh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH=?, sdt=?, gioiTinh=? WHERE maKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm vào class DAO.KhachHang.KhachHangDAO
    public Optional<Entity.KhachHang.KhachHang> findByPhone(String sdt) {
        String sql = "SELECT maKH, tenKH, SDT, gioiTinh FROM KhachHang WHERE SDT = ?";
        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Entity.KhachHang.KhachHang kh = new Entity.KhachHang.KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    kh.setTenKH(rs.getString("tenKH"));
                    kh.setSdt(rs.getString("SDT"));

                    // Chuẩn hóa giới tính về String "Nam"/"Nữ"
                    Object raw = rs.getObject("gioiTinh");
                    String gt;
                    if (raw instanceof Boolean)
                        gt = ((Boolean) raw) ? "Nam" : "Nữ";
                    else if (raw instanceof Number)
                        gt = (((Number) raw).intValue() == 1) ? "Nam" : "Nữ";
                    else
                        gt = String.valueOf(raw);
                    kh.setGioiTinh(gt);

                    return Optional.of(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /* ===================== Helpers ===================== */

    private KhachHang mapRow(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setTenKH(rs.getString("tenKH"));
        kh.setGioiTinh(rs.getString("gioiTinh"));
        kh.setSdt(rs.getString("SDT")); // SQL Server không phân biệt hoa/thường
        return kh;
    }
}

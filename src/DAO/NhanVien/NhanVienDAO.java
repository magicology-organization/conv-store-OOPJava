/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.NhanVien;

import ConnectDB.ConnectDB;
import Entity.NhanVien.NhanVien;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NhanVienDAO {
    private final Connection conn;

    public NhanVienDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<NhanVien> findAll() {
        String sql = "SELECT maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, cccd, trangThai FROM NhanVien ORDER BY maNV";
        List<NhanVien> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Object[]> findAllWithDetails() {
        String sql = """
            SELECT maNV, tenNV, chucVu, SDT, gioiTinh, trangThai
            FROM NhanVien
            ORDER BY maNV ASC
        """;
        List<Object[]> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // --- Chuẩn hóa giới tính về "Nam"/"Nữ"
                Object rawGT = rs.getObject("gioiTinh");
                String gt;
                if      (rawGT instanceof Boolean) gt = ((Boolean) rawGT) ? "Nam" : "Nữ";
                else if (rawGT instanceof Number)  gt = (((Number) rawGT).intValue() == 1) ? "Nam" : "Nữ";
                else                                gt = String.valueOf(rawGT); // đã là "Nam"/"Nữ"

                // --- Chuẩn hóa trạng thái về "Đang làm"/"Nghỉ việc"
                Object rawTT = rs.getObject("trangThai");
                String tt;
                if      (rawTT instanceof Boolean) tt = ((Boolean) rawTT) ? "Đang làm" : "Nghỉ việc";
                else if (rawTT instanceof Number)  tt = (((Number) rawTT).intValue() == 1) ? "Đang làm" : "Nghỉ việc";
                else {
                    String s = String.valueOf(rawTT).trim();
                    tt = (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("Đang làm"))
                            ? "Đang làm" : "Nghỉ việc";
                }

                out.add(new Object[]{
                    rs.getString("maNV"),   // 0
                    rs.getString("tenNV"),  // 1
                    rs.getString("chucVu"), // 2
                    rs.getString("SDT"),    // 3
                    gt,                     // 4  -> String "Nam"/"Nữ"
                    tt                      // 5  -> String "Đang làm"/"Nghỉ việc"
                });
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }



    public Optional<NhanVien> findById(String maNV) {
        if (isBlank(maNV)) return Optional.empty();
        String sql = "SELECT maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, cccd, trangThai FROM NhanVien WHERE maNV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(NhanVien nv) {
        if (nv == null || isBlank(nv.getMaNV())) return false;
        String sql = "INSERT INTO NhanVien(maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, cccd, trangThai) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, nv);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhanVien nv) {
        if (nv == null || isBlank(nv.getMaNV())) return false;
        String sql = "UPDATE NhanVien SET tenNV=?, sdt=?, gioiTinh=?, ngaySinh=?, ngayVaoLam=?, chucVu=?, cccd=?, trangThai=? WHERE maNV=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getGioiTinh());
            ps.setTimestamp(4, Timestamp.valueOf(nv.getNgaySinh()));
            ps.setTimestamp(5, Timestamp.valueOf(nv.getNgayVaoLam()));
            ps.setString(6, nv.getChucVu());
            ps.setString(7, nv.getCccd());
            ps.setString(8, nv.getTrangThai());
            ps.setString(9, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maNV) {
        if (isBlank(maNV)) return false;
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NhanVien> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maNV, tenNV, sdt, gioiTinh, ngaySinh, ngayVaoLam, chucVu, cccd, trangThai FROM NhanVien WHERE tenNV LIKE ? ORDER BY tenNV, maNV";
        List<NhanVien> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static NhanVien mapRow(ResultSet rs) throws SQLException {
        return new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("sdt"),
                rs.getString("gioiTinh"),
                rs.getTimestamp("ngaySinh").toLocalDateTime(),
                rs.getTimestamp("ngayVaoLam").toLocalDateTime(),
                rs.getString("chucVu"),
                rs.getString("cccd"),
                rs.getString("trangThai")
        );
    }

    private static void fillParams(PreparedStatement ps, NhanVien nv) throws SQLException {
        ps.setString(1, nv.getMaNV());
        ps.setString(2, nv.getTenNV());
        ps.setString(3, nv.getSdt());
        ps.setString(4, nv.getGioiTinh());
        ps.setTimestamp(5, Timestamp.valueOf(nv.getNgaySinh()));
        ps.setTimestamp(6, Timestamp.valueOf(nv.getNgayVaoLam()));
        ps.setString(7, nv.getChucVu());
        ps.setString(8, nv.getCccd());
        ps.setString(9, nv.getTrangThai());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}


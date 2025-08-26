/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.NhanVien;

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


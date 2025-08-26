/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO {
    private final Connection conn;

    public KhachHangDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<KhachHang> findAll() {
        String sql = "SELECT maKH, tenKH, gioiTinh, sdt, tuoi FROM KhachHang ORDER BY maKH";
        List<KhachHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<KhachHang> findById(String maKH) {
        if (isBlank(maKH)) return Optional.empty();
        String sql = "SELECT maKH, tenKH, gioiTinh, sdt, tuoi FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(KhachHang kh) {
        if (kh == null || isBlank(kh.getMaKH())) return false;
        String sql = "INSERT INTO KhachHang(maKH, tenKH, gioiTinh, sdt, tuoi) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, nullToEmpty(kh.getTenKH()));
            ps.setString(3, nullToEmpty(kh.getGioiTinh()));
            ps.setString(4, nullToEmpty(kh.getSdt()));
            if (kh.getTuoi() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, kh.getTuoi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHang kh) {
        if (kh == null || isBlank(kh.getMaKH())) return false;
        String sql = "UPDATE KhachHang SET tenKH=?, gioiTinh=?, sdt=?, tuoi=? WHERE maKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(kh.getTenKH()));
            ps.setString(2, nullToEmpty(kh.getGioiTinh()));
            ps.setString(3, nullToEmpty(kh.getSdt()));
            if (kh.getTuoi() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, kh.getTuoi());
            ps.setString(5, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maKH) {
        if (isBlank(maKH)) return false;
        String sql = "DELETE FROM KhachHang WHERE maKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhachHang> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maKH, tenKH, gioiTinh, sdt, tuoi FROM KhachHang WHERE tenKH LIKE ? ORDER BY tenKH, maKH";
        List<KhachHang> list = new ArrayList<>();
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

    private static KhachHang mapRow(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getString("maKH"),
                rs.getString("tenKH"),
                rs.getString("gioiTinh"),
                rs.getString("sdt"),
                rs.getInt("tuoi")
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

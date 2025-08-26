/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.CTHoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CTHoaDonDAO {
    private final Connection conn;

    public CTHoaDonDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<CTHoaDon> findAllByMaHD(String maHD) {
        String sql = "SELECT maHD, maSP, soLuong, donGia FROM CTHoaDon WHERE maHD=?";
        List<CTHoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Optional<CTHoaDon> findById(String maHD, String maSP) {
        String sql = "SELECT maHD, maSP, soLuong, donGia FROM CTHoaDon WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(CTHoaDon ct) {
        if (ct == null || isBlank(ct.getMaHD()) || isBlank(ct.getMaSP())) return false;
        String sql = "INSERT INTO CTHoaDon(maHD, maSP, soLuong, donGia) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(CTHoaDon ct) {
        if (ct == null || isBlank(ct.getMaHD()) || isBlank(ct.getMaSP())) return false;
        String sql = "UPDATE CTHoaDon SET soLuong=?, donGia=? WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getMaHD());
            ps.setString(4, ct.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maHD, String maSP) {
        String sql = "DELETE FROM CTHoaDon WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private static CTHoaDon mapRow(ResultSet rs) throws SQLException {
        return new CTHoaDon(
                rs.getString("maHD"),
                rs.getString("maSP"),
                rs.getInt("soLuong"),
                rs.getDouble("donGia")
        );
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}


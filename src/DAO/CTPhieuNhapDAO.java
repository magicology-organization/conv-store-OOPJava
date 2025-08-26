/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.CTPhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CTPhieuNhapDAO {
    private final Connection conn;

    public CTPhieuNhapDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<CTPhieuNhap> findAllByMaPN(String maPN) {
        String sql = "SELECT maPN, maSP, soLuong, donGia FROM CTPhieuNhap WHERE maPN=?";
        List<CTPhieuNhap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Optional<CTPhieuNhap> findById(String maPN, String maSP) {
        String sql = "SELECT maPN, maSP, soLuong, donGia FROM CTPhieuNhap WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ps.setString(2, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(CTPhieuNhap ct) {
        if (ct == null || isBlank(ct.getMaPN()) || isBlank(ct.getMaSP())) return false;
        String sql = "INSERT INTO CTPhieuNhap(maPN, maSP, soLuong, donGia) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPN());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(CTPhieuNhap ct) {
        if (ct == null || isBlank(ct.getMaPN()) || isBlank(ct.getMaSP())) return false;
        String sql = "UPDATE CTPhieuNhap SET soLuong=?, donGia=? WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getMaPN());
            ps.setString(4, ct.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maPN, String maSP) {
        String sql = "DELETE FROM CTPhieuNhap WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private static CTPhieuNhap mapRow(ResultSet rs) throws SQLException {
        return new CTPhieuNhap(
                rs.getString("maPN"),
                rs.getString("maSP"),
                rs.getInt("soLuong"),
                rs.getDouble("donGia")
        );
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}


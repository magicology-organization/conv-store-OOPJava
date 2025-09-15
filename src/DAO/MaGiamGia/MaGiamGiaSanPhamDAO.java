/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.MaGiamGia;

/**
 *
 * @author ADMIN
 */

import ConnectDB.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaGiamGiaSanPhamDAO {
    private final Connection conn;

    public MaGiamGiaSanPhamDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<String> findSanPhamByCoupon(String maCoupon) {
        String sql = "SELECT maSP FROM MaGiamGia_SanPham WHERE maCoupon=? ORDER BY maSP";
        List<String> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getString("maSP"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean add(String maCoupon, String maSP) {
        String sql = "INSERT INTO MaGiamGia_SanPham(maCoupon, maSP) VALUES(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean remove(String maCoupon, String maSP) {
        String sql = "DELETE FROM MaGiamGia_SanPham WHERE maCoupon=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean removeAllOfCoupon(String maCoupon) {
        String sql = "DELETE FROM MaGiamGia_SanPham WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}

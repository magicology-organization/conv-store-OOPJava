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

public class MaGiamGiaDanhMucDAO {
    private final Connection conn;

    public MaGiamGiaDanhMucDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<String> findDanhMucByCoupon(String maCoupon) {
        String sql = "SELECT maDM FROM MaGiamGia_DanhMuc WHERE maCoupon=? ORDER BY maDM";
        List<String> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(rs.getString("maDM"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean add(String maCoupon, String maDM) {
        String sql = "INSERT INTO MaGiamGia_DanhMuc(maCoupon, maDM) VALUES(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.setString(2, maDM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(String maCoupon, String maDM) {
        String sql = "DELETE FROM MaGiamGia_DanhMuc WHERE maCoupon=? AND maDM=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.setString(2, maDM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeAllOfCoupon(String maCoupon) {
        String sql = "DELETE FROM MaGiamGia_DanhMuc WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

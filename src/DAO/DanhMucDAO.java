/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.DanhMuc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DanhMucDAO {
    private final Connection conn;

    public DanhMucDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<DanhMuc> findAll() {
        String sql = "SELECT maDM, tenDM FROM DanhMuc ORDER BY maDM";
        List<DanhMuc> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<DanhMuc> findById(String maDM) {
        if (isBlank(maDM)) return Optional.empty();
        String sql = "SELECT maDM, tenDM FROM DanhMuc WHERE maDM = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(DanhMuc dm) {
        if (dm == null || isBlank(dm.getMaDM())) return false;
        String sql = "INSERT INTO DanhMuc(maDM, tenDM) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dm.getMaDM());
            ps.setString(2, nullToEmpty(dm.getTenDM()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(DanhMuc dm) {
        if (dm == null || isBlank(dm.getMaDM())) return false;
        String sql = "UPDATE DanhMuc SET tenDM=? WHERE maDM=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(dm.getTenDM()));
            ps.setString(2, dm.getMaDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maDM) {
        if (isBlank(maDM)) return false;
        String sql = "DELETE FROM DanhMuc WHERE maDM=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static DanhMuc mapRow(ResultSet rs) throws SQLException {
        return new DanhMuc(
                rs.getString("maDM"),
                rs.getString("tenDM")
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}


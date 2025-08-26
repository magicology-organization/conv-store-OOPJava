/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.XuatXu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XuatXuDAO {
    private final Connection conn;

    public XuatXuDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<XuatXu> findAll() {
        String sql = "SELECT maXX, tenXX FROM XuatXu ORDER BY maXX";
        List<XuatXu> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<XuatXu> findById(String maXX) {
        if (maXX == null || maXX.isBlank()) return Optional.empty();
        String sql = "SELECT maXX, tenXX FROM XuatXu WHERE maXX = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maXX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(XuatXu xx) {
        if (xx == null || isBlank(xx.getMaXX())) return false;
        String sql = "INSERT INTO XuatXu(maXX, tenXX) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, xx.getMaXX());
            ps.setString(2, nullToEmpty(xx.getTenXX()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Nếu vi phạm PK -> false
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(XuatXu xx) {
        if (xx == null || isBlank(xx.getMaXX())) return false;
        String sql = "UPDATE XuatXu SET tenXX = ? WHERE maXX = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(xx.getTenXX()));
            ps.setString(2, xx.getMaXX());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maXX) {
        if (isBlank(maXX)) return false;
        String sql = "DELETE FROM XuatXu WHERE maXX = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maXX);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Có thể rơi vào lỗi FK constraint nếu đang được tham chiếu
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String maXX) {
        if (isBlank(maXX)) return false;
        String sql = "SELECT 1 FROM XuatXu WHERE maXX = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maXX);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM XuatXu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<XuatXu> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maXX, tenXX FROM XuatXu WHERE tenXX LIKE ? ORDER BY tenXX, maXX";
        List<XuatXu> list = new ArrayList<>();
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

    public int insertBatch(List<XuatXu> items) {
        if (items == null || items.isEmpty()) return 0;
        String sql = "INSERT INTO XuatXu(maXX, tenXX) VALUES(?, ?)";
        int[] result;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (XuatXu xx : items) {
                if (xx == null || isBlank(xx.getMaXX())) continue;
                ps.setString(1, xx.getMaXX());
                ps.setString(2, nullToEmpty(xx.getTenXX()));
                ps.addBatch();
            }
            result = ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return 0;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        int ok = 0;
        for (int r : result) if (r >= 0) ok++;
        return ok;
    }

    private static XuatXu mapRow(ResultSet rs) throws SQLException {
        String ma = rs.getString("maXX");
        String ten = rs.getString("tenXX");
        return new XuatXu(ma, ten);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}


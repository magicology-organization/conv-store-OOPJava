/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.DonViTinh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonViTinhDAO {
    private final Connection conn;

    public DonViTinhDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<DonViTinh> findAll() {
        String sql = "SELECT maDVT, tenDVT FROM DonViTinh ORDER BY maDVT";
        List<DonViTinh> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<DonViTinh> findById(String maDVT) {
        if (isBlank(maDVT)) return Optional.empty();
        String sql = "SELECT maDVT, tenDVT FROM DonViTinh WHERE maDVT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDVT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(DonViTinh dvt) {
        if (dvt == null || isBlank(dvt.getMaDVT())) return false;
        String sql = "INSERT INTO DonViTinh(maDVT, tenDVT) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dvt.getMaDVT());
            ps.setString(2, nullToEmpty(dvt.getTenDVT()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(DonViTinh dvt) {
        if (dvt == null || isBlank(dvt.getMaDVT())) return false;
        String sql = "UPDATE DonViTinh SET tenDVT=? WHERE maDVT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(dvt.getTenDVT()));
            ps.setString(2, dvt.getMaDVT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maDVT) {
        if (isBlank(maDVT)) return false;
        String sql = "DELETE FROM DonViTinh WHERE maDVT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDVT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static DonViTinh mapRow(ResultSet rs) throws SQLException {
        return new DonViTinh(
                rs.getString("maDVT"),
                rs.getString("tenDVT")
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}


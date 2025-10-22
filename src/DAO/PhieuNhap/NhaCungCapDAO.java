/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.PhieuNhap;

import ConnectDB.ConnectDB;
import Entity.Phieu.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NhaCungCapDAO {
    private final Connection conn;

    public NhaCungCapDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<NhaCungCap> findAll() {
        String sql = "SELECT maNCC, tenNCC, diaChiNCC, sdt FROM NhaCungCap ORDER BY maNCC";
        List<NhaCungCap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<NhaCungCap> findById(String maNCC) {
        if (isBlank(maNCC)) return Optional.empty();
        String sql = "SELECT maNCC, tenNCC, diaChiNCC, sdt FROM NhaCungCap WHERE maNCC = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(NhaCungCap ncc) {
        if (ncc == null || isBlank(ncc.getMaNCC())) return false;
        String sql = "INSERT INTO NhaCungCap(maNCC, tenNCC, diaChiNCC, sdt) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, nullToEmpty(ncc.getTenNCC()));
            ps.setString(3, nullToEmpty(ncc.getDiaChiNCC()));
            ps.setString(4, nullToEmpty(ncc.getSdt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhaCungCap ncc) {
        if (ncc == null || isBlank(ncc.getMaNCC())) return false;
        String sql = "UPDATE NhaCungCap SET tenNCC=?, diaChiNCC=?, sdt=? WHERE maNCC=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(ncc.getTenNCC()));
            ps.setString(2, nullToEmpty(ncc.getDiaChiNCC()));
            ps.setString(3, nullToEmpty(ncc.getSdt()));
            ps.setString(4, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maNCC) {
        if (isBlank(maNCC)) return false;
        String sql = "DELETE FROM NhaCungCap WHERE maNCC = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<NhaCungCap> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maNCC, tenNCC, diaChiNCC, sdt FROM NhaCungCap WHERE tenNCC LIKE ? ORDER BY tenNCC, maNCC";
        List<NhaCungCap> list = new ArrayList<>();
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

    private static NhaCungCap mapRow(ResultSet rs) throws SQLException {
        return new NhaCungCap(
                rs.getString("maNCC"),
                rs.getString("tenNCC"),
                rs.getString("diaChiNCC"),
                rs.getString("sdt")
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}


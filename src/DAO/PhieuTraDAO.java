/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.PhieuTra;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuTraDAO {
    private final Connection conn;

    public PhieuTraDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<PhieuTra> findAll() {
        String sql = "SELECT maPT, maNV, maKH, thoiGian FROM PhieuTra ORDER BY maPT";
        List<PhieuTra> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Optional<PhieuTra> findById(String maPT) {
        if (isBlank(maPT)) return Optional.empty();
        String sql = "SELECT maPT, maNV, maKH, thoiGian FROM PhieuTra WHERE maPT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(PhieuTra pt) {
        if (pt == null || isBlank(pt.getMaPT())) return false;
        String sql = "INSERT INTO PhieuTra(maPT, maNV, maKH, thoiGian) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pt.getMaPT());
            ps.setString(2, pt.getMaNV());
            ps.setString(3, pt.getMaKH());
            setDateTimeOrNull(ps, 4, pt.getThoiGian());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(PhieuTra pt) {
        if (pt == null || isBlank(pt.getMaPT())) return false;
        String sql = "UPDATE PhieuTra SET maNV=?, maKH=?, thoiGian=? WHERE maPT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pt.getMaNV());
            ps.setString(2, pt.getMaKH());
            setDateTimeOrNull(ps, 3, pt.getThoiGian());
            ps.setString(4, pt.getMaPT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maPT) {
        if (isBlank(maPT)) return false;
        String sql = "DELETE FROM PhieuTra WHERE maPT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private static PhieuTra mapRow(ResultSet rs) throws SQLException {
        PhieuTra pt = new PhieuTra();
        pt.setMaPT(rs.getString("maPT"));
        pt.setMaNV(rs.getString("maNV"));
        pt.setMaKH(rs.getString("maKH"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null) pt.setThoiGian(ts.toLocalDateTime());
        return pt;
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null) ps.setNull(idx, Types.TIMESTAMP); else ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}


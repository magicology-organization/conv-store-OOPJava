/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.SanPham;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SanPhamDAO {
    private final Connection conn;

    public SanPhamDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<SanPham> findAll() {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham ORDER BY maSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Optional<SanPham> findById(String maSP) {
        if (isBlank(maSP)) return Optional.empty();
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(SanPham sp) {
        if (sp == null || isBlank(sp.getMaSP())) return false;
        String sql = "INSERT INTO SanPham(maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, sp, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(SanPham sp) {
        if (sp == null || isBlank(sp.getMaSP())) return false;
        String sql = "UPDATE SanPham SET tenSP=?, anhSP=?, moTaSP=?, maDM=?, maDVT=?, maXX=?, soLuong=?, giaNhap=?, donGia=?, HSD=? WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, sp, true);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maSP) {
        if (isBlank(maSP)) return false;
        String sql = "DELETE FROM SanPham WHERE maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<SanPham> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE tenSP LIKE ? ORDER BY tenSP, maSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<SanPham> findByDanhMuc(String maDM) {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE maDM=? ORDER BY tenSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<SanPham> findExpiredBefore(LocalDateTime dt) {
        String sql = "SELECT maSP, tenSP, anhSP, moTaSP, maDM, maDVT, maXX, soLuong, giaNhap, donGia, HSD FROM SanPham WHERE HSD < ? ORDER BY HSD, tenSP";
        List<SanPham> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setDateTimeOrNull(ps, 1, dt);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private static SanPham mapRow(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setAnhSP(rs.getBytes("anhSP"));
        sp.setMoTaSP(rs.getString("moTaSP"));
        sp.setMaDM(rs.getString("maDM"));
        sp.setMaDVT(rs.getString("maDVT"));
        sp.setMaXX(rs.getString("maXX"));
        sp.setSoLuong(rs.getInt("soLuong"));
        sp.setGiaNhap(rs.getDouble("giaNhap"));
        sp.setDonGia(rs.getDouble("donGia"));
        Timestamp ts = rs.getTimestamp("HSD");
        if (ts != null) sp.setHsd(ts.toLocalDateTime());
        return sp;
    }

    private static void fillParams(PreparedStatement ps, SanPham sp, boolean forUpdate) throws SQLException {
        if (forUpdate) {
            ps.setString(1, nullToEmpty(sp.getTenSP()));
            setBytesOrNull(ps, 2, sp.getAnhSP());
            ps.setString(3, nullToEmpty(sp.getMoTaSP()));
            ps.setString(4, nullToEmpty(sp.getMaDM()));
            ps.setString(5, nullToEmpty(sp.getMaDVT()));
            ps.setString(6, nullToEmpty(sp.getMaXX()));
            ps.setInt(7, sp.getSoLuong());
            ps.setDouble(8, sp.getGiaNhap());
            ps.setDouble(9, sp.getDonGia());
            setDateTimeOrNull(ps, 10, sp.getHsd());
            ps.setString(11, sp.getMaSP());
        } else {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, nullToEmpty(sp.getTenSP()));
            setBytesOrNull(ps, 3, sp.getAnhSP());
            ps.setString(4, nullToEmpty(sp.getMoTaSP()));
            ps.setString(5, nullToEmpty(sp.getMaDM()));
            ps.setString(6, nullToEmpty(sp.getMaDVT()));
            ps.setString(7, nullToEmpty(sp.getMaXX()));
            ps.setInt(8, sp.getSoLuong());
            ps.setDouble(9, sp.getGiaNhap());
            ps.setDouble(10, sp.getDonGia());
            setDateTimeOrNull(ps, 11, sp.getHsd());
        }
    }

    private static void setBytesOrNull(PreparedStatement ps, int idx, byte[] bytes) throws SQLException {
        if (bytes == null) ps.setNull(idx, Types.VARBINARY); else ps.setBytes(idx, bytes);
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null) ps.setNull(idx, Types.TIMESTAMP); else ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String nullToEmpty(String s) { return s == null ? "" : s; }
}


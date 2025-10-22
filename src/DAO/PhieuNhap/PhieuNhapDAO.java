/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.PhieuNhap;

import ConnectDB.ConnectDB;
import Entity.Phieu.PhieuNhap;
import java.math.BigDecimal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuNhapDAO {
    private final Connection conn;

    public PhieuNhapDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<PhieuNhap> findAll() {
        String sql = "SELECT maPN, maNV, maNCC, thoiGian FROM PhieuNhap ORDER BY maPN";
        List<PhieuNhap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
        public List<Object[]> findAllWithDetails() {
        String sql = """
            SELECT pn.maPN,
                   ncc.tenNCC,
                   ncc.SDT       AS soDienThoai,
                   nv.tenNV,
                   pn.thoiGian,
                   ISNULL(SUM(ct.soLuong * ct.donGia), 0) AS tongHoaDon
            FROM PhieuNhap pn
            JOIN NhaCungCap ncc ON ncc.maNCC = pn.maNCC
            JOIN NhanVien   nv  ON nv.maNV   = pn.maNV
            LEFT JOIN CTPhieuNhap ct ON ct.maPN = pn.maPN
            GROUP BY pn.maPN, ncc.tenNCC, ncc.SDT, nv.tenNV, pn.thoiGian
            ORDER BY pn.maPN ASC
        """;

        List<Object[]> out = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDateTime tg = rs.getTimestamp("thoiGian").toLocalDateTime();
                BigDecimal tong = rs.getBigDecimal("tongHoaDon");
                out.add(new Object[]{
                        rs.getString("maPN"),        // 0
                        rs.getString("tenNCC"),      // 1
                        rs.getString("soDienThoai"), // 2
                        rs.getString("tenNV"),       // 3
                        tg,                          // 4 (LocalDateTime)
                        tong                         // 5 (BigDecimal)
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }
    public Optional<PhieuNhap> findById(String maPN) {
        if (isBlank(maPN)) return Optional.empty();
        String sql = "SELECT maPN, maNV, maNCC, thoiGian FROM PhieuNhap WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(PhieuNhap pn) {
        if (pn == null || isBlank(pn.getMaPN())) return false;
        String sql = "INSERT INTO PhieuNhap(maPN, maNV, maNCC, thoiGian) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPN());
            ps.setString(2, pn.getMaNV());
            ps.setString(3, pn.getMaNCC());
            setDateTimeOrNull(ps, 4, pn.getThoiGian());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(PhieuNhap pn) {
        if (pn == null || isBlank(pn.getMaPN())) return false;
        String sql = "UPDATE PhieuNhap SET maNV=?, maNCC=?, thoiGian=? WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaNV());
            ps.setString(2, pn.getMaNCC());
            setDateTimeOrNull(ps, 3, pn.getThoiGian());
            ps.setString(4, pn.getMaPN());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maPN) {
        if (isBlank(maPN)) return false;
        String sql = "DELETE FROM PhieuNhap WHERE maPN=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private static PhieuNhap mapRow(ResultSet rs) throws SQLException {
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(rs.getString("maPN"));
        pn.setMaNV(rs.getString("maNV"));
        pn.setMaNCC(rs.getString("maNCC"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null) pn.setThoiGian(ts.toLocalDateTime());
        return pn;
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null) ps.setNull(idx, Types.TIMESTAMP); else ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}


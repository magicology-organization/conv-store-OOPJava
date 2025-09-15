package DAO.PhieuXuat;

import ConnectDB.ConnectDB;
import Entity.Phieu.PhieuXuat;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuXuatDAO {
    private final Connection conn;

    public PhieuXuatDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<PhieuXuat> findAll() {
        String sql = "SELECT maPX, maNV, thoiGian, lyDo FROM PhieuXuat ORDER BY thoiGian DESC, maPX";
        List<PhieuXuat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Optional<PhieuXuat> findById(String maPX) {
        if (isBlank(maPX)) return Optional.empty();
        String sql = "SELECT maPX, maNV, thoiGian, lyDo FROM PhieuXuat WHERE maPX=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean insert(PhieuXuat px) {
        if (px == null || isBlank(px.getMaPX())) return false;
        String sql = "INSERT INTO PhieuXuat(maPX, maNV, thoiGian, lyDo) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, px.getMaPX());
            ps.setString(2, px.getMaNV());
            setDateTimeOrNull(ps, 3, px.getThoiGian());
            ps.setString(4, nullToEmpty(px.getLyDo()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(PhieuXuat px) {
        if (px == null || isBlank(px.getMaPX())) return false;
        String sql = "UPDATE PhieuXuat SET maNV=?, thoiGian=?, lyDo=? WHERE maPX=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, px.getMaNV());
            setDateTimeOrNull(ps, 2, px.getThoiGian());
            ps.setString(3, nullToEmpty(px.getLyDo()));
            ps.setString(4, px.getMaPX());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String maPX) {
        if (isBlank(maPX)) return false;
        String sql = "DELETE FROM PhieuXuat WHERE maPX=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // tiện ích truy vấn thêm (tuỳ chọn)
    public List<PhieuXuat> findByNhanVien(String maNV) {
        String sql = "SELECT maPX, maNV, thoiGian, lyDo FROM PhieuXuat WHERE maNV=? ORDER BY thoiGian DESC";
        List<PhieuXuat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<PhieuXuat> findInRange(LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT maPX, maNV, thoiGian, lyDo FROM PhieuXuat WHERE thoiGian BETWEEN ? AND ? ORDER BY thoiGian";
        List<PhieuXuat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setDateTimeOrNull(ps, 1, from);
            setDateTimeOrNull(ps, 2, to);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ---- helpers ----
    private static PhieuXuat mapRow(ResultSet rs) throws SQLException {
        PhieuXuat px = new PhieuXuat();
        px.setMaPX(rs.getString("maPX"));
        px.setMaNV(rs.getString("maNV"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null) px.setThoiGian(ts.toLocalDateTime());
        px.setLyDo(rs.getString("lyDo"));
        return px;
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null) ps.setNull(idx, Types.TIMESTAMP);
        else ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String nullToEmpty(String s) { return s == null ? "" : s; }
}

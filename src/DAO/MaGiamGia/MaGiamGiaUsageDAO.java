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
import Entity.MaGiamGia.MaGiamGiaUsage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class MaGiamGiaUsageDAO {
    private final Connection conn;

    public MaGiamGiaUsageDAO() {
        this.conn = ConnectDB.getConnection();
    }

    // ====== Queries ======
    public Optional<MaGiamGiaUsage> findById(String maUsage, String maCoupon, String maKH, String maHD) {
        String sql = "SELECT maUsage, maCoupon, maKH, maHD, thoiGian, soTienGiam " +
                "FROM MaGiamGiaUsage WHERE maUsage=? AND maCoupon=? AND maKH=? AND maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maUsage);
            ps.setString(2, maCoupon);
            ps.setString(3, maKH);
            ps.setString(4, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<MaGiamGiaUsage> listByCoupon(String maCoupon) {
        String sql = "SELECT maUsage, maCoupon, maKH, maHD, thoiGian, soTienGiam " +
                "FROM MaGiamGiaUsage WHERE maCoupon=? ORDER BY thoiGian DESC, maUsage DESC";
        List<MaGiamGiaUsage> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MaGiamGiaUsage> listByCustomer(String maKH) {
        String sql = "SELECT maUsage, maCoupon, maKH, maHD, thoiGian, soTienGiam " +
                "FROM MaGiamGiaUsage WHERE maKH=? ORDER BY thoiGian DESC, maUsage DESC";
        List<MaGiamGiaUsage> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countTotal(String maCoupon) {
        String sql = "SELECT COUNT(*) FROM MaGiamGiaUsage WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByCustomer(String maCoupon, String maKH) {
        String sql = "SELECT COUNT(*) FROM MaGiamGiaUsage WHERE maCoupon=? AND maKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            ps.setString(2, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ====== Mutations ======
    public boolean insert(MaGiamGiaUsage u) {
        // PK là chuỗi do bạn cấp: maUsage + maCoupon + maKH + maHD
        String sql = "INSERT INTO MaGiamGiaUsage(maUsage, maCoupon, maKH, maHD, thoiGian, soTienGiam) " +
                "VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getMaUsage());
            ps.setString(2, u.getMaCoupon());
            ps.setString(3, u.getMaKH());
            ps.setString(4, u.getMaHD());
            setDateTimeOrNull(ps, 5, u.getThoiGian());
            ps.setBigDecimal(6, nvl(u.getSoTienGiam()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MaGiamGiaUsage u) {
        String sql = "UPDATE MaGiamGiaUsage SET thoiGian=?, soTienGiam=? " +
                "WHERE maUsage=? AND maCoupon=? AND maKH=? AND maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setDateTimeOrNull(ps, 1, u.getThoiGian());
            ps.setBigDecimal(2, nvl(u.getSoTienGiam()));
            ps.setString(3, u.getMaUsage());
            ps.setString(4, u.getMaCoupon());
            ps.setString(5, u.getMaKH());
            ps.setString(6, u.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maUsage, String maCoupon, String maKH, String maHD) {
        String sql = "DELETE FROM MaGiamGiaUsage WHERE maUsage=? AND maCoupon=? AND maKH=? AND maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maUsage);
            ps.setString(2, maCoupon);
            ps.setString(3, maKH);
            ps.setString(4, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------- helpers -------
    private static MaGiamGiaUsage mapRow(ResultSet rs) throws SQLException {
        MaGiamGiaUsage u = new MaGiamGiaUsage();
        u.setMaUsage(rs.getString("maUsage"));
        u.setMaCoupon(rs.getString("maCoupon"));
        u.setMaKH(rs.getString("maKH"));
        u.setMaHD(rs.getString("maHD"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null)
            u.setThoiGian(ts.toLocalDateTime());
        u.setSoTienGiam(rs.getBigDecimal("soTienGiam"));
        return u;
    }

    private static void setDateTimeOrNull(PreparedStatement ps, int idx, LocalDateTime dt) throws SQLException {
        if (dt == null)
            ps.setNull(idx, Types.TIMESTAMP);
        else
            ps.setTimestamp(idx, Timestamp.valueOf(dt));
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}

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
import Entity.MaGiamGia.MaGiamGia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

public class MaGiamGiaDAO {
    private final Connection conn;

    public MaGiamGiaDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<MaGiamGia> findAll() {
        String sql = "SELECT maCoupon,kieuCoupon,giaTriCoupon,donHangToiThieu,giamToiDa,hieuLucTu,hieuLucDen,luotDung,luotDungKH,hoatDong "
                +
                "FROM MaGiamGia ORDER BY maCoupon";
        List<MaGiamGia> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<MaGiamGia> findByCode(String maCoupon) {
        String sql = "SELECT maCoupon,kieuCoupon,giaTriCoupon,donHangToiThieu,giamToiDa,hieuLucTu,hieuLucDen,luotDung,luotDungKH,hoatDong "
                +
                "FROM MaGiamGia WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<MaGiamGia> findActiveByCode(String maCoupon) {
        String sql = "SELECT maCoupon,kieuCoupon,giaTriCoupon,donHangToiThieu,giamToiDa,hieuLucTu,hieuLucDen,luotDung,luotDungKH,hoatDong "
                +
                "FROM MaGiamGia WHERE maCoupon=? AND hoatDong=1 AND GETDATE() BETWEEN hieuLucTu AND hieuLucDen";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(MaGiamGia c) {
        String sql = "INSERT INTO MaGiamGia(maCoupon,kieuCoupon,giaTriCoupon,donHangToiThieu,giamToiDa,hieuLucTu,hieuLucDen,luotDung,luotDungKH,hoatDong) "
                +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fill(ps, c, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MaGiamGia c) {
        String sql = "UPDATE MaGiamGia SET kieuCoupon=?,giaTriCoupon=?,donHangToiThieu=?,giamToiDa=?,hieuLucTu=?,hieuLucDen=?,luotDung=?,luotDungKH=?,hoatDong=? "
                +
                "WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fill(ps, c, true);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maCoupon) {
        String sql = "DELETE FROM MaGiamGia WHERE maCoupon=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCoupon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MaGiamGia mapRow(ResultSet rs) throws SQLException {
        MaGiamGia c = new MaGiamGia();
        c.setMaCoupon(rs.getString("maCoupon"));
        c.setKieuCoupon(rs.getString("kieuCoupon"));
        c.setGiaTriCoupon(rs.getBigDecimal("giaTriCoupon"));
        c.setDonHangToiThieu(rs.getBigDecimal("donHangToiThieu"));
        c.setGiamToiDa(rs.getBigDecimal("giamToiDa"));
        Timestamp tu = rs.getTimestamp("hieuLucTu");
        Timestamp den = rs.getTimestamp("hieuLucDen");
        if (tu != null)
            c.setHieuLucTu(tu.toLocalDateTime());
        if (den != null)
            c.setHieuLucDen(den.toLocalDateTime());
        c.setLuotDung(rs.getInt("luotDung"));
        c.setLuotDungKH(rs.getInt("luotDungKH"));
        c.setHoatDong(rs.getBoolean("hoatDong"));
        return c;
    }

    private void fill(PreparedStatement ps, MaGiamGia c, boolean forUpdate) throws SQLException {
        if (forUpdate) {
            ps.setString(1, c.getKieuCoupon());
            ps.setBigDecimal(2, c.getGiaTriCoupon());
            ps.setBigDecimal(3, c.getDonHangToiThieu());
            ps.setBigDecimal(4, c.getGiamToiDa()); // NOT NULL
            setDateTimeOrNull(ps, 5, c.getHieuLucTu());
            setDateTimeOrNull(ps, 6, c.getHieuLucDen());
            ps.setInt(7, c.getLuotDung());
            ps.setInt(8, c.getLuotDungKH());
            ps.setBoolean(9, c.isHoatDong());
            ps.setString(10, c.getMaCoupon());
        } else {
            ps.setString(1, c.getMaCoupon());
            ps.setString(2, c.getKieuCoupon());
            ps.setBigDecimal(3, c.getGiaTriCoupon());
            ps.setBigDecimal(4, c.getDonHangToiThieu());
            ps.setBigDecimal(5, c.getGiamToiDa()); // NOT NULL
            setDateTimeOrNull(ps, 6, c.getHieuLucTu());
            setDateTimeOrNull(ps, 7, c.getHieuLucDen());
            ps.setInt(8, c.getLuotDung());
            ps.setInt(9, c.getLuotDungKH());
            ps.setBoolean(10, c.isHoatDong());
        }
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

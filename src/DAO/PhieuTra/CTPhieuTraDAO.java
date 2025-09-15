package DAO.PhieuTra;

import ConnectDB.ConnectDB;
import Entity.Phieu.CTPhieuTra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class CTPhieuTraDAO {
    private final Connection conn;

    public CTPhieuTraDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<CTPhieuTra> findAllByMaPT(String maPT) {
        String sql = "SELECT maPT, maSP, soLuong, donGia FROM CTPhieuTra WHERE maPT=?";
        List<CTPhieuTra> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<CTPhieuTra> findById(String maPT, String maSP) {
        String sql = "SELECT maPT, maSP, soLuong, donGia FROM CTPhieuTra WHERE maPT=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            ps.setString(2, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(CTPhieuTra ct) {
        if (ct == null || isBlank(ct.getMaPT()) || isBlank(ct.getMaSP()))
            return false;
        String sql = "INSERT INTO CTPhieuTra(maPT, maSP, soLuong, donGia) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPT());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setBigDecimal(4, ct.getDonGia()); // << đổi sang BigDecimal
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CTPhieuTra ct) {
        if (ct == null || isBlank(ct.getMaPT()) || isBlank(ct.getMaSP()))
            return false;
        String sql = "UPDATE CTPhieuTra SET soLuong=?, donGia=? WHERE maPT=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, ct.getDonGia()); // << đổi sang BigDecimal
            ps.setString(3, ct.getMaPT());
            ps.setString(4, ct.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maPT, String maSP) {
        String sql = "DELETE FROM CTPhieuTra WHERE maPT=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPT);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static CTPhieuTra mapRow(ResultSet rs) throws SQLException {
        return new CTPhieuTra(
                rs.getString("maPT"),
                rs.getString("maSP"),
                rs.getInt("soLuong"),
                rs.getBigDecimal("donGia") // << đổi sang BigDecimal
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

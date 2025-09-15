package DAO.PhieuXuat;

import ConnectDB.ConnectDB;
import Entity.Phieu.CTPhieuXuat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class CTPhieuXuatDAO {
    private final Connection conn;

    public CTPhieuXuatDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<CTPhieuXuat> findAllByMaPX(String maPX) {
        String sql = "SELECT maPX, maSP, soLuong, donGia FROM CTPhieuXuat WHERE maPX=?";
        List<CTPhieuXuat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<CTPhieuXuat> findById(String maPX, String maSP) {
        String sql = "SELECT maPX, maSP, soLuong, donGia FROM CTPhieuXuat WHERE maPX=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
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

    public boolean insert(CTPhieuXuat ct) {
        if (ct == null || isBlank(ct.getMaPX()) || isBlank(ct.getMaSP()))
            return false;
        String sql = "INSERT INTO CTPhieuXuat(maPX, maSP, soLuong, donGia) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPX());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setBigDecimal(4, ct.getDonGia()); // BigDecimal cho DECIMAL(18,2)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CTPhieuXuat ct) {
        if (ct == null || isBlank(ct.getMaPX()) || isBlank(ct.getMaSP()))
            return false;
        String sql = "UPDATE CTPhieuXuat SET soLuong=?, donGia=? WHERE maPX=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, ct.getDonGia());
            ps.setString(3, ct.getMaPX());
            ps.setString(4, ct.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maPX, String maSP) {
        String sql = "DELETE FROM CTPhieuXuat WHERE maPX=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Tổng tiền 1 phiếu xuất = SUM(soLuong * donGia) */
    public Optional<BigDecimal> sumThanhTienByMaPX(String maPX) {
        String sql = "SELECT SUM(CAST(soLuong as decimal(18,2)) * donGia) AS tong FROM CTPhieuXuat WHERE maPX=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal v = rs.getBigDecimal("tong");
                    return Optional.ofNullable(v == null ? BigDecimal.ZERO : v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(BigDecimal.ZERO);
    }

    private static CTPhieuXuat mapRow(ResultSet rs) throws SQLException {
        return new CTPhieuXuat(
                rs.getString("maPX"),
                rs.getString("maSP"),
                rs.getInt("soLuong"),
                rs.getBigDecimal("donGia"));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

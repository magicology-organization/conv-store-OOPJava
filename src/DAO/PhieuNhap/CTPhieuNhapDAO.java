package DAO.PhieuNhap;

import ConnectDB.ConnectDB;
import Entity.PhieuNhap.CTPhieuNhap;
import Entity.PhieuNhap.PhieuNhap;
import Entity.SanPham.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CTPhieuNhapDAO {
    private final Connection conn;

    public CTPhieuNhapDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<CTPhieuNhap> findAllByMaPN(String maPN) {
        String sql = "SELECT maPN, maSP, soLuong, donGia FROM CTPhieuNhap WHERE maPN=?";
        List<CTPhieuNhap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<CTPhieuNhap> findById(String maPN, String maSP) {
        String sql = "SELECT maPN, maSP, soLuong, donGia FROM CTPhieuNhap WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
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

    public boolean insert(CTPhieuNhap ct) {
        if (ct == null || ct.getPhieuNhap() == null || ct.getSanPham() == null)
            return false;

        String sql = "INSERT INTO CTPhieuNhap(maPN, maSP, soLuong, donGia) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getPhieuNhap().getMaPN());
            ps.setString(2, ct.getSanPham().getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setBigDecimal(4, ct.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CTPhieuNhap ct) {
        if (ct == null || ct.getPhieuNhap() == null || ct.getSanPham() == null)
            return false;

        String sql = "UPDATE CTPhieuNhap SET soLuong=?, donGia=? WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, ct.getDonGia());
            ps.setString(3, ct.getPhieuNhap().getMaPN());
            ps.setString(4, ct.getSanPham().getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maPN, String maSP) {
        String sql = "DELETE FROM CTPhieuNhap WHERE maPN=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CTPhieuNhap> thongTinChiTietIn(String maPN) {
        String sql = """
                    SELECT ct.maPN, ct.maSP, sp.tenSP, ct.soLuong, ct.donGia
                    FROM CTPhieuNhap ct
                    JOIN SanPham sp ON sp.maSP = ct.maSP
                    WHERE ct.maPN = ?
                """;

        List<CTPhieuNhap> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuNhap pn = new PhieuNhap(rs.getString("maPN"));
                    SanPham sp = new SanPham(rs.getString("maSP"));
                    sp.setTenSP(rs.getString("tenSP"));

                    int soLuong = rs.getInt("soLuong");
                    java.math.BigDecimal donGia = rs.getBigDecimal("donGia");

                    list.add(new CTPhieuNhap(pn, sp, soLuong, donGia));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static CTPhieuNhap mapRow(ResultSet rs) throws SQLException {
        PhieuNhap pn = new PhieuNhap(rs.getString("maPN"));
        SanPham sp = new SanPham(rs.getString("maSP"));
        int soLuong = rs.getInt("soLuong");
        java.math.BigDecimal donGia = rs.getBigDecimal("donGia");
        return new CTPhieuNhap(pn, sp, soLuong, donGia);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

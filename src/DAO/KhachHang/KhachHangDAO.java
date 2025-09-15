package DAO.KhachHang;

import ConnectDB.ConnectDB;
import Entity.KhachHang.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO {
    private final Connection conn;

    public KhachHangDAO() {
        this.conn = ConnectDB.getConnection();
    }

    /* ===================== CRUD ===================== */

    public List<KhachHang> findAll() {
        String sql = "SELECT maKH, tenKH, gioiTinh, SDT FROM KhachHang ORDER BY maKH";
        List<KhachHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<KhachHang> findById(String maKH) {
        String sql = "SELECT maKH, tenKH, gioiTinh, SDT FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(maKH, tenKH, gioiTinh, SDT) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSdt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH=?, gioiTinh=?, SDT=? WHERE maKH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getGioiTinh());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== Helpers ===================== */

    private KhachHang mapRow(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setTenKH(rs.getString("tenKH"));
        kh.setGioiTinh(rs.getString("gioiTinh"));
        kh.setSdt(rs.getString("SDT")); // SQL Server không phân biệt hoa/thường
        return kh;
    }

    /* (tuỳ chọn) tìm theo từ khoá tên/SDT */
    public List<KhachHang> search(String keyword) {
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String sql = "SELECT maKH, tenKH, gioiTinh, SDT FROM KhachHang " +
                "WHERE maKH LIKE ? OR tenKH LIKE ? OR SDT LIKE ? ORDER BY maKH";
        List<KhachHang> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

package DAO.HoaDon;

import ConnectDB.ConnectDB;
import Entity.HoaDon.CTHoaDon;
import Entity.HoaDon.HoaDon;
import Entity.SanPham.SanPham;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class CTHoaDonDAO {
    private final Connection conn;

    public CTHoaDonDAO() {
        this.conn = ConnectDB.getConnection();
    }


    public List<CTHoaDon> findAllByMaHD(String maHD) {
        String sql = "SELECT maHD, maSP, soLuong, donGia FROM CTHoaDon WHERE maHD = ?";
        List<CTHoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<CTHoaDon> findById(String maHD, String maSP) {
        String sql = "SELECT maHD, maSP, soLuong, donGia FROM CTHoaDon WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
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

    public boolean insert(CTHoaDon ct) {
        if (ct == null || ct.getHoaDon() == null || ct.getSanPham() == null)
            return false;

        String sql = "INSERT INTO CTHoaDon(maHD, maSP, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getHoaDon().getMaHD());
            ps.setString(2, ct.getSanPham().getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setBigDecimal(4, nvl(ct.getDonGia()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CTHoaDon ct) {
        if (ct == null || ct.getHoaDon() == null || ct.getSanPham() == null)
            return false;

        String sql = "UPDATE CTHoaDon SET soLuong=?, donGia=? WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, nvl(ct.getDonGia()));
            ps.setString(3, ct.getHoaDon().getMaHD());
            ps.setString(4, ct.getSanPham().getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maHD, String maSP) {
        String sql = "DELETE FROM CTHoaDon WHERE maHD=? AND maSP=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByMaHD(String maHD) {
        String sql = "DELETE FROM CTHoaDon WHERE maHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CTHoaDon> thongTinChiTietIn(String maHD) {
        String sql = """
                    SELECT ct.maHD, ct.maSP, sp.tenSP, ct.soLuong, ct.donGia
                    FROM CTHoaDon ct
                    JOIN SanPham sp ON sp.maSP = ct.maSP
                    WHERE ct.maHD = ?
                """;

        List<CTHoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon(rs.getString("maHD"));
                    SanPham sp = new SanPham(rs.getString("maSP"));
                    sp.setTenSP(rs.getString("tenSP"));

                    int soLuong = rs.getInt("soLuong");
                    BigDecimal donGia = rs.getBigDecimal("donGia");

                    list.add(new CTHoaDon(hd, sp, soLuong, donGia));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private static CTHoaDon mapRow(ResultSet rs) throws SQLException {
        String maHD = rs.getString("maHD");
        String maSP = rs.getString("maSP");
        int soLuong = rs.getInt("soLuong");
        BigDecimal donGia = rs.getBigDecimal("donGia");

        HoaDon hoaDon = new HoaDon(maHD);
        SanPham sanPham = new SanPham(maSP);

        return new CTHoaDon(hoaDon, sanPham, soLuong, donGia);
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}

package DAO.HoaDon;

import ConnectDB.ConnectDB;
import Entity.HoaDon.HoaDon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoaDonDAO {
    private final Connection conn;

    public HoaDonDAO() {
        this.conn = ConnectDB.getConnection();
    }

    /* ===================== CRUD ===================== */

    public List<HoaDon> findAll() {
        String sql = "SELECT maHD, maNV, maKH, thoiGian, kieuThanhToan " +
                "FROM HoaDon ORDER BY maHD";
        List<HoaDon> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<HoaDon> findById(String maHD) {
        String sql = "SELECT maHD, maNV, maKH, thoiGian, kieuThanhToan " +
                "FROM HoaDon WHERE maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon(maHD, maNV, maKH, thoiGian, kieuThanhToan) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaNV());
            ps.setString(3, hd.getMaKH());
            setTimestampOrNull(ps, 4, hd.getThoiGian());
            // lấy đúng chuỗi từ ComboBox: "Tiền mặt" hoặc "Chuyển khoản"
            ps.setString(5, hd.getKieuThanhToan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(HoaDon hd) {
        String sql = "UPDATE HoaDon SET maNV=?, maKH=?, thoiGian=?, kieuThanhToan=? " +
                "WHERE maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaNV());
            ps.setString(2, hd.getMaKH());
            setTimestampOrNull(ps, 3, hd.getThoiGian());
            ps.setString(4, hd.getKieuThanhToan());
            ps.setString(5, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE maHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== Helpers ===================== */

    private HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaNV(rs.getString("maNV"));
        hd.setMaKH(rs.getString("maKH"));
        Timestamp ts = rs.getTimestamp("thoiGian");
        if (ts != null)
            hd.setThoiGian(ts.toLocalDateTime());
        // trả về đúng chuỗi để setSelectedItem trên ComboBox
        hd.setKieuThanhToan(rs.getString("kieuThanhToan"));
        return hd;
    }

    private static void setTimestampOrNull(PreparedStatement ps, int idx, LocalDateTime ldt)
            throws SQLException {
        if (ldt != null)
            ps.setTimestamp(idx, Timestamp.valueOf(ldt));
        else
            ps.setNull(idx, Types.TIMESTAMP);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import Entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaiKhoanDAO {
    private final Connection conn;

    public TaiKhoanDAO() {
        this.conn = ConnectDB.getConnection();
    }

    public List<TaiKhoan> findAll() {
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan ORDER BY maTK";
        List<TaiKhoan> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<TaiKhoan> findById(String maTK) {
        if (isBlank(maTK)) return Optional.empty();
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan WHERE maTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean insert(TaiKhoan tk) {
        if (tk == null || isBlank(tk.getMaTK())) return false;
        String sql = "INSERT INTO TaiKhoan(maTK, tenTK, matKhauTK, maNV) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTK());
            ps.setString(2, nullToEmpty(tk.getTenTK()));
            ps.setString(3, nullToEmpty(tk.getMatKhauTK()));
            if (isBlank(tk.getMaNV())) ps.setNull(4, Types.NVARCHAR);
            else ps.setString(4, tk.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(TaiKhoan tk) {
        if (tk == null || isBlank(tk.getMaTK())) return false;
        String sql = "UPDATE TaiKhoan SET tenTK = ?, matKhauTK = ?, maNV = ? WHERE maTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nullToEmpty(tk.getTenTK()));
            ps.setString(2, nullToEmpty(tk.getMatKhauTK()));
            if (isBlank(tk.getMaNV())) ps.setNull(3, Types.NVARCHAR);
            else ps.setString(3, tk.getMaNV());
            ps.setString(4, tk.getMaTK());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maTK) {
        if (isBlank(maTK)) return false;
        String sql = "DELETE FROM TaiKhoan WHERE maTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String maTK) {
        if (isBlank(maTK)) return false;
        String sql = "SELECT 1 FROM TaiKhoan WHERE maTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM TaiKhoan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Optional<TaiKhoan> findByUsername(String tenTK) {
        if (isBlank(tenTK)) return Optional.empty();
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan WHERE tenTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<TaiKhoan> checkDangNhap(String tenTK, String matKhauTK) {
        if (isBlank(tenTK) || isBlank(matKhauTK)) return Optional.empty();
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan WHERE tenTK = ? AND matKhauTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenTK);
            ps.setString(2, matKhauTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean doiMatKhau(String tenTK, String matKhauMoi) {
        if (isBlank(tenTK)) return false;
        String sql = "UPDATE TaiKhoan SET matKhauTK = ? WHERE tenTK = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setString(2, tenTK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TaiKhoan> findByMaNV(String maNV) {
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan WHERE maNV = ? ORDER BY maTK";
        List<TaiKhoan> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (isBlank(maNV)) ps.setNull(1, Types.NVARCHAR);
            else ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteByMaNV(String maNV) {
        String sql = "DELETE FROM TaiKhoan WHERE maNV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (isBlank(maNV)) ps.setNull(1, Types.NVARCHAR);
            else ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TaiKhoan> searchByTenTK(String keyword) {
        String kw = keyword == null ? "" : keyword.trim();
        String sql = "SELECT maTK, tenTK, matKhauTK, maNV FROM TaiKhoan WHERE tenTK LIKE ? ORDER BY tenTK, maTK";
        List<TaiKhoan> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static TaiKhoan mapRow(ResultSet rs) throws SQLException {
        return new TaiKhoan(
                rs.getString("maTK"),
                rs.getString("tenTK"),
                rs.getString("matKhauTK"),
                rs.getString("maNV")
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}


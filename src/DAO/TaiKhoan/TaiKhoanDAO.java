/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.TaiKhoan;

import ConnectDB.ConnectDB;
import Entity.NhanVien.NhanVien;
import Entity.TaiKhoan.TaiKhoan;

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
        String sql = """
                SELECT maTK, tenTK, matKhauTK, maNV
                FROM TaiKhoan
                ORDER BY maTK
                """;

        List<TaiKhoan> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<Object[]> findAllWithDetails() {
        String sql = """
                SELECT tk.maTK, tk.tenTK,
                       nv.tenNV, nv.chucVu
                FROM TaiKhoan tk
                LEFT JOIN NhanVien nv ON tk.maNV = nv.maNV
                ORDER BY tk.maTK
                """;

        List<Object[]> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(new Object[] {
                        rs.getString("maTK"), // 0 - mã tài khoản
                        rs.getString("tenNV"), // 1 - tên nhân viên
                        rs.getString("tenTK"), // 2 - tài khoản
                        rs.getString("chucVu") // 3 - chức vụ
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Optional<TaiKhoan> findById(String maTK) {
        String sql = """
                SELECT maTK, tenTK, matKhauTK, maNV
                FROM TaiKhoan
                WHERE maTK = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<TaiKhoan> findByUsername(String tenTK) {
        String sql = """
                SELECT maTK, tenTK, matKhauTK, maNV
                FROM TaiKhoan
                WHERE tenTK = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenTK);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Object[]> search(String maTK, String tenTK, String maNV, String tenNV) {
        List<Object[]> list = new ArrayList<>();

        String sql = """
                SELECT tk.maTK, nv.tenNV, tk.tenTK, nv.chucVu
                FROM TaiKhoan tk
                LEFT JOIN NhanVien nv ON tk.maNV = nv.maNV
                WHERE (? IS NULL OR tk.maTK LIKE ?)
                  AND (? IS NULL OR tk.tenTK LIKE ?)
                  AND (? IS NULL OR nv.maNV LIKE ?)
                  AND (? IS NULL OR nv.tenNV LIKE ?)
                ORDER BY tk.maTK
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // Chuẩn hóa giá trị tìm kiếm
            String mTK = (maTK == null || maTK.isBlank()) ? null : "%" + maTK + "%";
            String tTK = (tenTK == null || tenTK.isBlank()) ? null : "%" + tenTK + "%";
            String mNV = (maNV == null || maNV.isBlank()) ? null : "%" + maNV + "%";
            String tNV = (tenNV == null || tenNV.isBlank()) ? null : "%" + tenNV + "%";

            ps.setString(1, mTK);
            ps.setString(2, mTK);
            ps.setString(3, tTK);
            ps.setString(4, tTK);
            ps.setString(5, mNV);
            ps.setString(6, mNV);
            ps.setString(7, tNV);
            ps.setString(8, tNV);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("maTK"),
                        rs.getString("tenNV"),
                        rs.getString("tenTK"),
                        rs.getString("chucVu")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(TaiKhoan tk) {
        String sql = """
                INSERT INTO TaiKhoan (maTK, tenTK, matKhauTK, maNV)
                VALUES (?,?,?,?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTK());
            ps.setString(2, tk.getTenTK());
            ps.setString(3, tk.getMatKhauTK());

            if (tk.getNhanVien() != null)
                ps.setString(4, tk.getNhanVien().getMaNV());
            else
                ps.setNull(4, Types.NVARCHAR);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(TaiKhoan tk) {
        String sql = """
                UPDATE TaiKhoan
                SET tenTK = ?, matKhauTK = ?, maNV = ?
                WHERE maTK = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenTK());
            ps.setString(2, tk.getMatKhauTK());

            if (tk.getNhanVien() != null)
                ps.setString(3, tk.getNhanVien().getMaNV());
            else
                ps.setNull(3, Types.NVARCHAR);

            ps.setString(4, tk.getMaTK());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String maTK) {
        String sql = "DELETE FROM TaiKhoan WHERE maTK = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    /**
     * Check đăng nhập
     */
    public Optional<TaiKhoan> checkLogin(String username, String password) {
        String sql = """
                SELECT maTK, tenTK, matKhauTK, maNV
                FROM TaiKhoan
                WHERE tenTK = ? AND matKhauTK = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Tạo mã TK tự động dạng TK-00001
     */
    public String taoMaTK() {
        final String prefix = "TK-";
        String sql = """
                SELECT ISNULL(MAX(CAST(SUBSTRING(maTK, 4, 10) AS INT)), 0) AS maxNo
                FROM TaiKhoan
                WHERE maTK LIKE 'TK-%'
                """;

        int next = 1;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                next = rs.getInt("maxNo") + 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefix + String.format("%05d", next);
    }

    private TaiKhoan mapRow(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setMaTK(rs.getString("maTK"));
        tk.setTenTK(rs.getString("tenTK"));
        tk.setMatKhauTK(rs.getString("matKhauTK"));

        String maNV = rs.getString("maNV");
        if (maNV != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNV(maNV);

            // Lấy tên nhân viên từ DB
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT tenNV FROM NhanVien WHERE maNV = ?")) {
                ps.setString(1, maNV);
                try (ResultSet rsNV = ps.executeQuery()) {
                    if (rsNV.next()) {
                        nv.setTenNV(rsNV.getString("tenNV"));
                    }
                }
            }

            tk.setNhanVien(nv);
        }

        return tk;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO.ThongKe;

import ConnectDB.ConnectDB;
import Entity.ThongKe.ThongKe;
import Entity.ThongKe.ThongKeTheoThang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ThongKeDAO {

    /**
     * 1️⃣ Thống kê theo giờ (24 giờ trong ngày)
     */
    public List<ThongKe> getThongKeTheoGio(Date ngay) {
        List<ThongKe> list = new ArrayList<>();
        String sql = """
                ;WITH Hours AS (
                    SELECT 0 AS Gio
                    UNION ALL
                    SELECT Gio + 1 FROM Hours WHERE Gio < 23
                )
                SELECT h.Gio,
                       ISNULL(SUM(cthd.soLuong * cthd.donGia),0) AS DoanhThu,
                       ISNULL(SUM(cthd.soLuong * sp.giaNhap),0) AS Von
                FROM Hours h
                LEFT JOIN HoaDon hd ON CAST(hd.thoiGian AS DATE) = ? AND DATEPART(HOUR, hd.thoiGian) = h.Gio
                LEFT JOIN CTHoaDon cthd ON hd.maHD = cthd.maHD
                LEFT JOIN SanPham sp ON cthd.maSP = sp.maSP
                GROUP BY h.Gio
                ORDER BY h.Gio
                OPTION (MAXRECURSION 0);
                """;

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(ngay.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int gio = rs.getInt("Gio");
                double doanhThu = rs.getDouble("DoanhThu");
                double von = rs.getDouble("Von");

                Calendar cal = Calendar.getInstance();
                cal.setTime(ngay);
                cal.set(Calendar.HOUR_OF_DAY, gio);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);

                list.add(new ThongKe(cal.getTime(), doanhThu, von));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 2️⃣ Thống kê theo ngày trong tháng
     */
    public List<ThongKe> getThongKeTheoThang(int thang, int nam) {
        List<ThongKe> list = new ArrayList<>();
        String sql = """
                ;WITH Days AS (
                    SELECT CAST(DATEFROMPARTS(?, ?, 1) AS DATE) AS Ngay
                    UNION ALL
                    SELECT DATEADD(DAY, 1, Ngay)
                    FROM Days
                    WHERE Ngay < EOMONTH(DATEFROMPARTS(?, ?, 1))
                )
                SELECT d.Ngay,
                       ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS DoanhThu,
                       ISNULL(SUM(cthd.soLuong * sp.giaNhap), 0) AS Von
                FROM Days d
                LEFT JOIN HoaDon hd ON CAST(hd.thoiGian AS DATE) = d.Ngay
                LEFT JOIN CTHoaDon cthd ON hd.maHD = cthd.maHD
                LEFT JOIN SanPham sp ON cthd.maSP = sp.maSP
                GROUP BY d.Ngay
                ORDER BY d.Ngay
                OPTION (MAXRECURSION 0);
                """;

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nam);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ps.setInt(4, thang);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("Ngay");
                double doanhThu = rs.getDouble("DoanhThu");
                double von = rs.getDouble("Von");
                list.add(new ThongKe(ngay, doanhThu, von));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 3️⃣ Thống kê theo năm (12 tháng)
     */
    public List<ThongKeTheoThang> getThongKeTheoNam(int nam) {
        List<ThongKeTheoThang> list = new ArrayList<>();
        String sql = """
                ;WITH Months AS (
                    SELECT 1 AS Thang
                    UNION ALL
                    SELECT Thang + 1 FROM Months WHERE Thang < 12
                )
                SELECT m.Thang,
                       ISNULL(SUM(cthd.soLuong * cthd.donGia),0) AS DoanhThu,
                       ISNULL(SUM(cthd.soLuong * sp.giaNhap),0) AS Von
                FROM Months m
                LEFT JOIN HoaDon hd ON YEAR(hd.thoiGian) = ? AND MONTH(hd.thoiGian) = m.Thang
                LEFT JOIN CTHoaDon cthd ON hd.maHD = cthd.maHD
                LEFT JOIN SanPham sp ON cthd.maSP = sp.maSP
                GROUP BY m.Thang
                ORDER BY m.Thang
                OPTION (MAXRECURSION 0);
                """;

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nam);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("Thang");
                double doanhThu = rs.getDouble("DoanhThu");
                double von = rs.getDouble("Von");
                list.add(new ThongKeTheoThang(thang, doanhThu, von));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
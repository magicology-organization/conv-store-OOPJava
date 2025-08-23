/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author ADMIN
 */
import java.util.Objects;

public class CTHoaDon {
    private String maHD;   // nvarchar(10)  (PK phần 1, FK -> HoaDon.maHD)
    private String maSP;   // nvarchar(10)  (PK phần 2, FK -> SanPham.maSP)
    private int soLuong;   // int NOT NULL
    private double donGia; // float (SQL Server) -> double

    public CTHoaDon() {}

    public CTHoaDon(String maHD, String maSP, int soLuong, double donGia) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters & Setters
    public String getMaHD() {
        return maHD;
    }

    public String getMaSP() {
        return maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }


    // (tuỳ chọn) tính thành tiền
    public double getThanhTien() {
        return soLuong * donGia;
    }

    // equals/hashCode dựa trên khóa ghép maHD + maSP
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CTHoaDon)) return false;
        CTHoaDon that = (CTHoaDon) o;
        return Objects.equals(maHD, that.maHD) &&
               Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD, maSP);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{maHD='" + maHD + "', maSP='" + maSP +
               "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.HoaDon;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private java.time.LocalDateTime thoiGian;
    private String kieuThanhToan; // "Tiền mặt" | "Chuyển khoản"

    public HoaDon() {
    }

    public HoaDon(String maHD, String maNV, String maKH,
            java.time.LocalDateTime thoiGian, String kieuThanhToan) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.thoiGian = thoiGian;
        this.kieuThanhToan = kieuThanhToan;
    }

    // Getters & Setters

    public String getMaHD() {
        return maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getKieuThanhToan() {
        return kieuThanhToan;
    }

    public void setKieuThanhToan(String ktt) {
        this.kieuThanhToan = ktt;
    }

    // equals/hashCode dựa trên PK maHD
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon)) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{maHD='" + maHD + "', maNV='" + maNV + "', maKH='" + maKH + "'}";
    }
}

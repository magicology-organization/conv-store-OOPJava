/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;
import java.util.Objects;

public class HoaDon {
    private String maHD;            // PK nvarchar(10)
    private String maNV;            // nvarchar(10) NOT NULL
    private String maKH;            // nvarchar(10) NOT NULL
    private LocalDateTime thoiGian; // datetime NOT NULL

    public HoaDon() {}

    public HoaDon(String maHD, String maNV, String maKH, LocalDateTime thoiGian) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.thoiGian = thoiGian;
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

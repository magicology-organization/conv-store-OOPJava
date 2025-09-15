/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.MaGiamGia;

/**
 *
 * @author ADMIN
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class MaGiamGiaUsage {
    private String maUsage; // nvarchar(10) - PK part 1
    private String maCoupon; // nvarchar(10) - PK part 2
    private String maKH; // nvarchar(10) - PK part 3
    private String maHD; // nvarchar(10) - PK part 4
    private LocalDateTime thoiGian; // datetime NOT NULL
    private BigDecimal soTienGiam; // decimal(18,2) NOT NULL

    public MaGiamGiaUsage() {
    }

    public MaGiamGiaUsage(String maUsage, String maCoupon, String maKH, String maHD,
            LocalDateTime thoiGian, BigDecimal soTienGiam) {
        this.maUsage = maUsage;
        this.maCoupon = maCoupon;
        this.maKH = maKH;
        this.maHD = maHD;
        this.thoiGian = thoiGian;
        this.soTienGiam = soTienGiam;
    }

    public String getMaUsage() {
        return maUsage;
    }

    public void setMaUsage(String maUsage) {
        this.maUsage = maUsage;
    }

    public String getMaCoupon() {
        return maCoupon;
    }

    public void setMaCoupon(String maCoupon) {
        this.maCoupon = maCoupon;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public BigDecimal getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(BigDecimal soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    // equals/hashCode dựa trên PK ghép
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaGiamGiaUsage))
            return false;
        MaGiamGiaUsage that = (MaGiamGiaUsage) o;
        return Objects.equals(maUsage, that.maUsage)
                && Objects.equals(maCoupon, that.maCoupon)
                && Objects.equals(maKH, that.maKH)
                && Objects.equals(maHD, that.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maUsage, maCoupon, maKH, maHD);
    }

    @Override
    public String toString() {
        return "MaGiamGiaUsage{maUsage='" + maUsage + "', maCoupon='" + maCoupon +
                "', maKH='" + maKH + "', maHD='" + maHD + "', thoiGian=" + thoiGian +
                ", soTienGiam=" + soTienGiam + "}";
    }
}

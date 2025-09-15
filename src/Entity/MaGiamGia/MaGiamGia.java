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

public class MaGiamGia {
    private String maCoupon; // PK
    private String kieuCoupon; // "PERCENT" | "FIXED"
    private BigDecimal giaTriCoupon; // DECIMAL(18,2)
    private BigDecimal donHangToiThieu; // DECIMAL(18,2)
    private BigDecimal giamToiDa; // DECIMAL(18,2) - có thể null
    private LocalDateTime hieuLucTu; // DATETIME
    private LocalDateTime hieuLucDen; // DATETIME
    private int luotDung; // INT
    private int luotDungKH; // INT
    private boolean hoatDong; // BIT

    public MaGiamGia() {
    }

    public MaGiamGia(String maCoupon,
            String kieuCoupon,
            BigDecimal giaTriCoupon,
            BigDecimal donHangToiThieu,
            BigDecimal giamToiDa,
            LocalDateTime hieuLucTu,
            LocalDateTime hieuLucDen,
            int luotDung,
            int luotDungKH,
            boolean hoatDong) {
        this.maCoupon = maCoupon;
        this.kieuCoupon = kieuCoupon;
        this.giaTriCoupon = giaTriCoupon;
        this.donHangToiThieu = donHangToiThieu;
        this.giamToiDa = giamToiDa;
        this.hieuLucTu = hieuLucTu;
        this.hieuLucDen = hieuLucDen;
        this.luotDung = luotDung;
        this.luotDungKH = luotDungKH;
        this.hoatDong = hoatDong;
    }

    public String getMaCoupon() {
        return maCoupon;
    }

    public void setMaCoupon(String maCoupon) {
        this.maCoupon = maCoupon;
    }

    public String getKieuCoupon() {
        return kieuCoupon;
    }

    public void setKieuCoupon(String kieuCoupon) {
        this.kieuCoupon = kieuCoupon;
    }

    public BigDecimal getGiaTriCoupon() {
        return giaTriCoupon;
    }

    public void setGiaTriCoupon(BigDecimal giaTriCoupon) {
        this.giaTriCoupon = giaTriCoupon;
    }

    public BigDecimal getDonHangToiThieu() {
        return donHangToiThieu;
    }

    public void setDonHangToiThieu(BigDecimal donHangToiThieu) {
        this.donHangToiThieu = donHangToiThieu;
    }

    public BigDecimal getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(BigDecimal giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public LocalDateTime getHieuLucTu() {
        return hieuLucTu;
    }

    public void setHieuLucTu(LocalDateTime hieuLucTu) {
        this.hieuLucTu = hieuLucTu;
    }

    public LocalDateTime getHieuLucDen() {
        return hieuLucDen;
    }

    public void setHieuLucDen(LocalDateTime hieuLucDen) {
        this.hieuLucDen = hieuLucDen;
    }

    public int getLuotDung() {
        return luotDung;
    }

    public void setLuotDung(int luotDung) {
        this.luotDung = luotDung;
    }

    public int getLuotDungKH() {
        return luotDungKH;
    }

    public void setLuotDungKH(int luotDungKH) {
        this.luotDungKH = luotDungKH;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaGiamGia))
            return false;
        MaGiamGia that = (MaGiamGia) o;
        return Objects.equals(maCoupon, that.maCoupon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCoupon);
    }

    @Override
    public String toString() {
        return "MaGiamGia{maCoupon='" + maCoupon + "', kieuCoupon='" + kieuCoupon + "'}";
    }
}

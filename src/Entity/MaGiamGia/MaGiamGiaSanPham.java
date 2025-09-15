/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.MaGiamGia;

/**
 *
 * @author ADMIN
 */

import java.util.Objects;

public class MaGiamGiaSanPham {
    private String maCoupon; // PK part 1
    private String maSP; // PK part 2

    public MaGiamGiaSanPham() {
    }

    public MaGiamGiaSanPham(String maCoupon, String maSP) {
        this.maCoupon = maCoupon;
        this.maSP = maSP;
    }

    public String getMaCoupon() {
        return maCoupon;
    }

    public void setMaCoupon(String maCoupon) {
        this.maCoupon = maCoupon;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaGiamGiaSanPham))
            return false;
        MaGiamGiaSanPham that = (MaGiamGiaSanPham) o;
        return Objects.equals(maCoupon, that.maCoupon) &&
                Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCoupon, maSP);
    }
}

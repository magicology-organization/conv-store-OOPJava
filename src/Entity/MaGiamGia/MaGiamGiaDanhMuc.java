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

public class MaGiamGiaDanhMuc {
    private String maCoupon; // PK part 1
    private String maDM; // PK part 2

    public MaGiamGiaDanhMuc() {
    }

    public MaGiamGiaDanhMuc(String maCoupon, String maDM) {
        this.maCoupon = maCoupon;
        this.maDM = maDM;
    }

    public String getMaCoupon() {
        return maCoupon;
    }

    public void setMaCoupon(String maCoupon) {
        this.maCoupon = maCoupon;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaGiamGiaDanhMuc))
            return false;
        MaGiamGiaDanhMuc that = (MaGiamGiaDanhMuc) o;
        return Objects.equals(maCoupon, that.maCoupon) &&
                Objects.equals(maDM, that.maDM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCoupon, maDM);
    }
}

package Entity.Phieu;

import java.math.BigDecimal;
import java.util.Objects;

public class CTPhieuTra {
    private String maPT; // PK part 1
    private String maSP; // PK part 2
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTPhieuTra() {
    }

    public CTPhieuTra(String maPT, String maSP, int soLuong, BigDecimal donGia) {
        this.maPT = maPT;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPT() {
        return maPT;
    }

    public void setMaPT(String maPT) {
        this.maPT = maPT;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    // thanhTien = soLuong * donGia
    public BigDecimal getThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CTPhieuTra))
            return false;
        CTPhieuTra that = (CTPhieuTra) o;
        return Objects.equals(maPT, that.maPT) &&
                Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPT, maSP);
    }

    @Override
    public String toString() {
        return "CTPhieuTra{maPT='" + maPT + "', maSP='" + maSP +
                "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

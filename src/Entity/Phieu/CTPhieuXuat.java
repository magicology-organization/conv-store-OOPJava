package Entity.Phieu;

import java.math.BigDecimal;
import java.util.Objects;

public class CTPhieuXuat {
    private String maPX; // PK part 1
    private String maSP; // PK part 2
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTPhieuXuat() {
    }

    public CTPhieuXuat(String maPX, String maSP, int soLuong, BigDecimal donGia) {
        this.maPX = maPX;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPX() {
        return maPX;
    }

    public void setMaPX(String maPX) {
        this.maPX = maPX;
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

    // thành tiền = soLuong * donGia
    public BigDecimal getThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CTPhieuXuat))
            return false;
        CTPhieuXuat that = (CTPhieuXuat) o;
        return Objects.equals(maPX, that.maPX) &&
                Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPX, maSP);
    }

    @Override
    public String toString() {
        return "CTPhieuXuat{maPX='" + maPX + "', maSP='" + maSP +
                "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

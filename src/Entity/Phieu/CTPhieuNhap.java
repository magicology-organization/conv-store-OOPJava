package Entity.Phieu;

import java.math.BigDecimal;
import java.util.Objects;

public class CTPhieuNhap {
    private String maPN; // PK part 1
    private String maSP; // PK part 2
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTPhieuNhap() {
    }

    public CTPhieuNhap(String maPN, String maSP, int soLuong, BigDecimal donGia) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
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

    // Tính thành tiền = soLuong * donGia
    public BigDecimal getThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CTPhieuNhap))
            return false;
        CTPhieuNhap that = (CTPhieuNhap) o;
        return Objects.equals(maPN, that.maPN) &&
                Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPN, maSP);
    }

    @Override
    public String toString() {
        return "CTPhieuNhap{maPN='" + maPN + "', maSP='" + maSP +
                "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

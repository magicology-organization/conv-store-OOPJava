package Entity.HoaDon;

import java.math.BigDecimal;
import java.util.Objects;

public class CTHoaDon {
    private String maHD; // nvarchar(10) PK phần 1
    private String maSP; // nvarchar(10) PK phần 2
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTHoaDon() {
    }

    public CTHoaDon(String maHD, String maSP, int soLuong, BigDecimal donGia) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters & Setters
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
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

    // equals/hashCode dựa trên khóa ghép maHD + maSP
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CTHoaDon))
            return false;
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
        return "CTHoaDon{maHD='" + maHD + "', maSP='" + maSP +
                "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

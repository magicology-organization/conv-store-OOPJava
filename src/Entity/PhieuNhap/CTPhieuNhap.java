package Entity.PhieuNhap;

import Entity.SanPham.SanPham;
import java.math.BigDecimal;
import java.util.Objects;

public class CTPhieuNhap {
    private PhieuNhap phieuNhap; // PK part 1
    private SanPham sanPham; // PK part 2
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTPhieuNhap() {
    }

    public CTPhieuNhap(PhieuNhap phieuNhap, SanPham sanPham, int soLuong, BigDecimal donGia) {
        this.phieuNhap = phieuNhap;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
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
        return Objects.equals(phieuNhap, that.phieuNhap) &&
                Objects.equals(sanPham, that.sanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuNhap, sanPham);
    }

    @Override
    public String toString() {
        return "CTPhieuNhap{phieuNhap='" + phieuNhap + "', sanPham='" + sanPham +
                "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

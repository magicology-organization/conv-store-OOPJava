package Entity.HoaDon;

import java.math.BigDecimal;
import java.util.Objects;
import Entity.SanPham.SanPham;

public class CTHoaDon {
    private HoaDon hoaDon; // FK -> HoaDon
    private SanPham sanPham; // FK -> SanPham
    private int soLuong; // int NOT NULL
    private BigDecimal donGia; // DECIMAL(18,2) NOT NULL

    public CTHoaDon() {
        this.donGia = BigDecimal.ZERO;
    }

    public CTHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, BigDecimal donGia) {
        this.hoaDon = hoaDon;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
    }

    // Constructor tiện dụng khi chỉ có mã (phục vụ DAO hoặc test)
    public CTHoaDon(String maHD, String maSP) {
        this.hoaDon = new HoaDon(maHD);
        this.sanPham = new SanPham(maSP);
        this.soLuong = 0;
        this.donGia = BigDecimal.ZERO;
    }

    // --- Getters & Setters ---
    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
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
        this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
    }

    // --- equals & hashCode (dựa trên khóa ghép: maHD + maSP) ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CTHoaDon))
            return false;
        CTHoaDon that = (CTHoaDon) o;
        return Objects.equals(
                hoaDon != null ? hoaDon.getMaHD() : null,
                that.hoaDon != null ? that.hoaDon.getMaHD() : null)
                && Objects.equals(
                        sanPham != null ? sanPham.getMaSP() : null,
                        that.sanPham != null ? that.sanPham.getMaSP() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                hoaDon != null ? hoaDon.getMaHD() : null,
                sanPham != null ? sanPham.getMaSP() : null);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "CTHoaDon{" +
                "hoaDon=" + (hoaDon != null ? hoaDon.getMaHD() : "null") +
                ", sanPham=" + (sanPham != null ? sanPham.getTenSP() : "null") +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                '}';
    }
}

package Entity.HoaDon;

import java.time.LocalDateTime;
import java.util.Objects;
import Entity.KhachHang.KhachHang;
import Entity.NhanVien.NhanVien;

public class HoaDon {
    private String maHD; // PK
    private NhanVien nhanVien; // FK -> NhanVien
    private KhachHang khachHang; // FK -> KhachHang
    private LocalDateTime thoiGian; // thời điểm mua
    private String kieuThanhToan; // "Tiền mặt" | "Chuyển khoản" | ...

    public HoaDon() {
    }

    // Constructor đầy đủ
    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang,
            LocalDateTime thoiGian, String kieuThanhToan) {
        this.maHD = maHD;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.thoiGian = thoiGian;
        this.kieuThanhToan = kieuThanhToan;
    }

    // Constructor tiện dụng (chỉ cần mã)
    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    // --- Getters & Setters ---
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getKieuThanhToan() {
        return kieuThanhToan;
    }

    public void setKieuThanhToan(String kieuThanhToan) {
        this.kieuThanhToan = kieuThanhToan;
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HoaDon))
            return false;
        HoaDon that = (HoaDon) o;
        return Objects.equals(maHD, that.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getTenNV() : "null") +
                ", khachHang=" + (khachHang != null ? khachHang.getTenKH() : "null") +
                ", thoiGian=" + thoiGian +
                ", kieuThanhToan='" + kieuThanhToan + '\'' +
                '}';
    }
}

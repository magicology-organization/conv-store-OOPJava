/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.PhieuNhap;

/**
 *
 * @author ADMIN
 */
import Entity.NhanVien.NhanVien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PhieuNhap {
    private String maPN; // PK nvarchar(10)
    private NhanVien nhanVien; // nvarchar(10) NOT NULL (FK -> NhanVien)
    private NhaCungCap ncc; // nvarchar(10) NOT NULL (FK -> NhaCungCap)
    private LocalDateTime thoiGian; // datetime NOT NULL
    private List<CTPhieuNhap> chiTietPhieuNhap;

    public PhieuNhap(String maPN) {
        this.maPN = maPN;
    }

    public PhieuNhap() {
    }

    public PhieuNhap(String maPN, NhanVien nhanVien, NhaCungCap ncc, LocalDateTime thoiGian) {
        this.maPN = maPN;
        this.nhanVien = nhanVien;
        this.ncc = ncc;
        this.thoiGian = thoiGian;
    }

    public String getMaPN() {
        return maPN;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public NhaCungCap getNCC() {
        return ncc;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setNCC(NhaCungCap ncc) {
        this.ncc = ncc;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
    public List<CTPhieuNhap> getChiTietPhieuNhap() {
        return chiTietPhieuNhap;
    }

    public void setChiTietPhieuNhap(List<CTPhieuNhap> chiTietPhieuNhap) {
        this.chiTietPhieuNhap = chiTietPhieuNhap;
    }

    // --- getTongTien gọn gàng ---
    public BigDecimal getTongTien() {
        return chiTietPhieuNhap == null ? BigDecimal.ZERO
            : chiTietPhieuNhap.stream()
                .map(ct -> ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PhieuNhap))
            return false;
        PhieuNhap that = (PhieuNhap) o;
        return Objects.equals(maPN, that.maPN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPN);
    }

    @Override
    public String toString() {
        return "PhieuNhap{maPN='" + maPN + "', maNCC='" + ncc + "'}";
    }
}

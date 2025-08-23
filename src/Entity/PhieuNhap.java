/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuNhap {
    private String maPN;           // PK nvarchar(10)
    private String maNV;           // nvarchar(10) NOT NULL (FK -> NhanVien)
    private String maNCC;          // nvarchar(10) NOT NULL (FK -> NhaCungCap)
    private LocalDateTime thoiGian; // datetime NOT NULL

    public PhieuNhap() {}

    public PhieuNhap(String maPN, String maNV, String maNCC, LocalDateTime thoiGian) {
        this.maPN = maPN;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.thoiGian = thoiGian;
    }

    public String getMaPN() {
        return maPN;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }


    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhieuNhap)) return false;
        PhieuNhap that = (PhieuNhap) o;
        return Objects.equals(maPN, that.maPN);
    }
    @Override public int hashCode() { return Objects.hash(maPN); }

    @Override public String toString() {
        return "PhieuNhap{maPN='" + maPN + "', maNCC='" + maNCC + "'}";
    }
}

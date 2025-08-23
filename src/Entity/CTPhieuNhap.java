/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author ADMIN
 */
import java.util.Objects;

public class CTPhieuNhap {
    private String maPN;   // PK part 1, nvarchar(10) (FK -> PhieuNhap)
    private String maSP;   // PK part 2, nvarchar(10) (FK -> SanPham)
    private int soLuong;   // int NOT NULL
    private double donGia; // float -> double NOT NULL

    public CTPhieuNhap() {}

    public CTPhieuNhap(String maPN, String maSP, int soLuong, double donGia) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaPN() { return maPN; }
    public void setMaPN(String maPN) { this.maPN = maPN; }
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public double getThanhTien() { return soLuong * donGia; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CTPhieuNhap)) return false;
        CTPhieuNhap that = (CTPhieuNhap) o;
        return Objects.equals(maPN, that.maPN) &&
               Objects.equals(maSP, that.maSP);
    }
    @Override public int hashCode() { return Objects.hash(maPN, maSP); }

    @Override public String toString() {
        return "ChiTietPhieuNhap{maPN='" + maPN + "', maSP='" + maSP +
               "', soLuong=" + soLuong + ", donGia=" + donGia + "}";
    }
}

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

public class SanPham {
    private String maSP;         // PK: nvarchar(10)
    private String tenSP;        // nvarchar(50)
    private byte[] anhSP;        // varbinary(max)
    private String moTaSP;       // nvarchar(50)
    private String maDM;         // FK -> DanhMuc.maDM (nvarchar(10) NOT NULL)
    private String maDVT;        // FK -> DonViTinh.maDVT (nvarchar(10) NOT NULL)
    private String maXX;         // FK -> XuatXu.maXX (nvarchar(10) NOT NULL)
    private int soLuong;         // int NOT NULL
    private double giaNhap;      // float NOT NULL
    private double donGia;       // float NOT NULL
    private LocalDateTime hsd;   // datetime NOT NULL

    public SanPham() {}

    public SanPham(String maSP, String tenSP, byte[] anhSP, String moTaSP,
                   String maDM, String maDVT, String maXX,
                   int soLuong, double giaNhap, double donGia, LocalDateTime hsd) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.moTaSP = moTaSP;
        this.maDM = maDM;
        this.maDVT = maDVT;
        this.maXX = maXX;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.donGia = donGia;
        this.hsd = hsd;
    }

    public String getMaSP() {
        return maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public byte[] getAnhSP() {
        return anhSP;
    }

    public String getMoTaSP() {
        return moTaSP;
    }

    public String getMaDM() {
        return maDM;
    }

    public String getMaDVT() {
        return maDVT;
    }

    public String getMaXX() {
        return maXX;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public double getDonGia() {
        return donGia;
    }

    public LocalDateTime getHsd() {
        return hsd;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setAnhSP(byte[] anhSP) {
        this.anhSP = anhSP;
    }

    public void setMoTaSP(String moTaSP) {
        this.moTaSP = moTaSP;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public void setMaDVT(String maDVT) {
        this.maDVT = maDVT;
    }

    public void setMaXX(String maXX) {
        this.maXX = maXX;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setHsd(LocalDateTime hsd) {
        this.hsd = hsd;
    }
    // equals/hashCode dựa trên khóa chính maSP
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SanPham)) return false;
        SanPham that = (SanPham) o;
        return Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSP);
    }

    @Override
    public String toString() {
        return "SanPham{maSP='" + maSP + "', tenSP='" + tenSP + "'}";
    }
}    
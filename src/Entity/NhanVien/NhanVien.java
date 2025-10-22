/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.NhanVien;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;
import java.util.Objects;

public class NhanVien {
    private String maNV;         // PK nvarchar(10)
    private String tenNV;        // nvarchar(50) NOT NULL
    private String sdt;          // nvarchar(12) NOT NULL
    private String gioiTinh;     // nvarchar(10) NOT NULL
    private LocalDateTime ngaySinh;   // datetime NOT NULL
    private LocalDateTime ngayVaoLam; // datetime NOT NULL
    private String chucVu;       // nvarchar(50) NOT NULL
    private String cccd;         // nvarchar(12) NOT NULL
    private String trangThai;    // nvarchar(20) NOT NULL

    public NhanVien() {}

    public NhanVien(String maNV, String tenNV, String sdt, String gioiTinh,
                    LocalDateTime ngaySinh, LocalDateTime ngayVaoLam,
                    String chucVu, String cccd, String trangThai) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.chucVu = chucVu;
        this.cccd = cccd;
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public LocalDateTime getNgaySinh() {
        return ngaySinh;
    }

    public LocalDateTime getNgayVaoLam() {
        return ngayVaoLam;
    }

    public String getChucVu() {
        return chucVu;
    }

    public String getCccd() {
        return cccd;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNgaySinh(LocalDateTime ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setNgayVaoLam(LocalDateTime ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }



    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        NhanVien that = (NhanVien) o;
        return Objects.equals(maNV, that.maNV);
    }
    @Override public int hashCode() { return Objects.hash(maNV); }

    @Override public String toString() {
        return "NhanVien{maNV='" + maNV + "', tenNV='" + tenNV + "'}";
    }
}

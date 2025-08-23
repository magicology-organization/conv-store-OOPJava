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

public class KhachHang {
    private String maKH;     // PK nvarchar(10)
    private String tenKH;    // nvarchar(50) NULL
    private String gioiTinh; // nvarchar(10) NULL
    private String sdt;      // nvarchar(10) NULL
    private Integer tuoi;    // int NULL

    public KhachHang() {}
    public KhachHang(String maKH, String tenKH, String gioiTinh, String sdt, Integer tuoi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.tuoi = tuoi;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public Integer getTuoi() {
        return tuoi;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setTuoi(Integer tuoi) {
        this.tuoi = tuoi;
    }



    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KhachHang)) return false;
        KhachHang that = (KhachHang) o;
        return Objects.equals(maKH, that.maKH);
    }
    @Override public int hashCode() { return Objects.hash(maKH); }

    @Override public String toString() {
        return "KhachHang{maKH='" + maKH + "', tenKH='" + tenKH + "'}";
    }
}

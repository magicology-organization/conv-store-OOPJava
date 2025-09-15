package Entity.KhachHang;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String gioiTinh; // ví dụ: "Nam" / "Nữ" / "Khác"
    private String sdt;

    public KhachHang() {}

    public KhachHang(String maKH, String tenKH, String gioiTinh, String sdt) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KhachHang)) return false;
        KhachHang that = (KhachHang) o;
        return Objects.equals(maKH, that.maKH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }

    @Override
    public String toString() {
        return maKH + " - " + tenKH;
    }
}

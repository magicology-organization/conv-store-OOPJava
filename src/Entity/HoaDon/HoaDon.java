package Entity.HoaDon;

import java.time.LocalDateTime;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private LocalDateTime thoiGian; // thời điểm mua
    private String kieuThanhToan; // "Tiền mặt" | "Chuyển khoản" | ...

    public HoaDon() {
    }

    public HoaDon(String maHD, String maNV, String maKH,
            LocalDateTime thoiGian, String kieuThanhToan) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.thoiGian = thoiGian;
        this.kieuThanhToan = kieuThanhToan;
    }


    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
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


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HoaDon))
            return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{maHD='" + maHD + "', maNV='" + maNV + "', maKH='" + maKH + "', kieuThanhToan='" + kieuThanhToan
                + "'}";
    }
}

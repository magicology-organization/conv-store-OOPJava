/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.Phieu;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuTra {
    private String maPT;           // PK nvarchar(10)
    private String maNV;           // nvarchar(10) NOT NULL (FK -> NhanVien)
    private String maKH;           // nvarchar(10) NOT NULL (FK -> KhachHang)
    private String maHD;           // nvarchar(10) NOT NULL (FK -> HoaDon)
    private LocalDateTime thoiGian; // datetime NOT NULL
    private String lyDo;           // nvarchar(100) NOT NULL

    public PhieuTra() {}

    public PhieuTra(String maPT, String maNV, String maKH, String maHD,
                    LocalDateTime thoiGian, String lyDo) {
        this.maPT = maPT;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maHD = maHD;
        this.thoiGian = thoiGian;
        this.lyDo = lyDo;
    }

    public String getMaPT() {
        return maPT;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getMaHD() {
        return maHD;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setMaPT(String maPT) {
        this.maPT = maPT;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }



    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhieuTra)) return false;
        PhieuTra that = (PhieuTra) o;
        return Objects.equals(maPT, that.maPT);
    }
    @Override public int hashCode() { return Objects.hash(maPT); }

    @Override public String toString() {
        return "PhieuTra{maPT='" + maPT + "', maHD='" + maHD + "'}";
    }
}

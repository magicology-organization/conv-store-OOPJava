package Entity.Phieu;

import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuXuat {
    private String maPX; // PK: nvarchar(10)
    private String maNV; // FK -> NhanVien.maNV
    private LocalDateTime thoiGian; // datetime NOT NULL
    private String lyDo; // nvarchar(100)

    public PhieuXuat() {
    }

    public PhieuXuat(String maPX, String maNV, LocalDateTime thoiGian, String lyDo) {
        this.maPX = maPX;
        this.maNV = maNV;
        this.thoiGian = thoiGian;
        this.lyDo = lyDo;
    }

    public String getMaPX() {
        return maPX;
    }

    public void setMaPX(String maPX) {
        this.maPX = maPX;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PhieuXuat))
            return false;
        PhieuXuat that = (PhieuXuat) o;
        return Objects.equals(maPX, that.maPX);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPX);
    }

    @Override
    public String toString() {
        return "PhieuXuat{maPX='" + maPX + "', maNV='" + maNV +
                "', thoiGian=" + thoiGian + ", lyDo='" + lyDo + "'}";
    }
}

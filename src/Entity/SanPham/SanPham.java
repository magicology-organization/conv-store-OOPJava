package Entity.SanPham;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class SanPham {
    private String maSP;         // PK: nvarchar(10)
    private String tenSP;        // nvarchar(50)
    private byte[] anhSP;        // varbinary(max)
    private String moTaSP;       // nvarchar(50)
    private String maDM;         // FK -> DanhMuc
    private String maDVT;        // FK -> DonViTinh
    private String maXX;         // FK -> XuatXu
    private int soLuong;         // int NOT NULL
    private BigDecimal giaNhap;  // DECIMAL(18,2) NOT NULL
    private BigDecimal donGia;   // DECIMAL(18,2) NOT NULL
    private LocalDateTime hsd;   // datetime NOT NULL

    public SanPham() {}

    public SanPham(String maSP, String tenSP, byte[] anhSP, String moTaSP,
                   String maDM, String maDVT, String maXX,
                   int soLuong, BigDecimal giaNhap, BigDecimal donGia, LocalDateTime hsd) {
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

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public byte[] getAnhSP() { return anhSP; }
    public void setAnhSP(byte[] anhSP) { this.anhSP = anhSP; }

    public String getMoTaSP() { return moTaSP; }
    public void setMoTaSP(String moTaSP) { this.moTaSP = moTaSP; }

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public String getMaDVT() { return maDVT; }
    public void setMaDVT(String maDVT) { this.maDVT = maDVT; }

    public String getMaXX() { return maXX; }
    public void setMaXX(String maXX) { this.maXX = maXX; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public BigDecimal getGiaNhap() { return giaNhap; }
    public void setGiaNhap(BigDecimal giaNhap) { this.giaNhap = giaNhap; }

    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public LocalDateTime getHsd() { return hsd; }
    public void setHsd(LocalDateTime hsd) { this.hsd = hsd; }

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

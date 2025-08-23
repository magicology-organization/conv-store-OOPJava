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
import java.util.logging.Logger;

public class NhaCungCap {
    private String maNCC;     // PK nvarchar(10)
    private String tenNCC;    // nvarchar(50) NOT NULL
    private String diaChiNCC; // nvarchar(50) NOT NULL
    private String sdt;       // nvarchar(12) NOT NULL

    public NhaCungCap() {}
    public NhaCungCap(String maNCC, String tenNCC, String diaChiNCC, String sdt) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChiNCC = diaChiNCC;
        this.sdt = sdt;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public String getDiaChiNCC() {
        return diaChiNCC;
    }

    public String getSdt() {
        return sdt;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public void setDiaChiNCC(String diaChiNCC) {
        this.diaChiNCC = diaChiNCC;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }




    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhaCungCap)) return false;
        NhaCungCap that = (NhaCungCap) o;
        return Objects.equals(maNCC, that.maNCC);
    }
    @Override public int hashCode() { return Objects.hash(maNCC); }

    @Override public String toString() {
        return "NhaCungCap{maNCC='" + maNCC + "', tenNCC='" + tenNCC + "'}";
    }
}

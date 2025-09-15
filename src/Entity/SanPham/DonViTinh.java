/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.SanPham;

/**
 *
 * @author ADMIN
 */
import java.util.Objects;

public class DonViTinh {
    private String maDVT;  // PK
    private String tenDVT;

    public DonViTinh() {}
    public DonViTinh(String maDVT, String tenDVT) {
        this.maDVT = maDVT;
        this.tenDVT = tenDVT;
    }

    public String getMaDVT() {
        return maDVT;
    }

    public String getTenDVT() {
        return tenDVT;
    }

    public void setMaDVT(String maDVT) {
        this.maDVT = maDVT;
    }

    public void setTenDVT(String tenDVT) {
        this.tenDVT = tenDVT;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonViTinh)) return false;
        DonViTinh that = (DonViTinh) o;
        return Objects.equals(maDVT, that.maDVT);
    }

    @Override
    public int hashCode() { return Objects.hash(maDVT); }

    @Override
    public String toString() {
        return "DonViTinh{maDVT='" + maDVT + "', tenDVT='" + tenDVT + "'}";
    }
}

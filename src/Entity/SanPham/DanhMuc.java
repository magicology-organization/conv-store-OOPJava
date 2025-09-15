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

public class DanhMuc {
    private String maDM;   // PK
    private String tenDM;

    public DanhMuc() {}
    public DanhMuc(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    public String getMaDM() {
        return maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DanhMuc)) return false;
        DanhMuc that = (DanhMuc) o;
        return Objects.equals(maDM, that.maDM);
    }

    @Override
    public int hashCode() { return Objects.hash(maDM); }

    @Override
    public String toString() {
        return "DanhMuc{maDM='" + maDM + "', tenDM='" + tenDM + "'}";
    }
}

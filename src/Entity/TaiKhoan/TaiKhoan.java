/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.TaiKhoan;

/**
 *
 * @author ADMIN
 */
import java.util.Objects;

public class TaiKhoan {
    private String maTK;       // PK nvarchar(10)
    private String tenTK;      // nvarchar(100) NOT NULL
    private String matKhauTK;  // nvarchar(100) NOT NULL
    private String maNV;       // nvarchar(10) NULL (FK -> NhanVien.maNV)

    public TaiKhoan() {}
    public TaiKhoan(String maTK, String tenTK, String matKhauTK, String maNV) {
        this.maTK = maTK;
        this.tenTK = tenTK;
        this.matKhauTK = matKhauTK;
        this.maNV = maNV;
    }

    public String getMaTK() {
        return maTK;
    }

    public String getTenTK() {
        return tenTK;
    }

    public String getMatKhauTK() {
        return matKhauTK;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public void setTenTK(String tenTK) {
        this.tenTK = tenTK;
    }

    public void setMatKhauTK(String matKhauTK) {
        this.matKhauTK = matKhauTK;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }



    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiKhoan)) return false;
        TaiKhoan that = (TaiKhoan) o;
        return Objects.equals(maTK, that.maTK);
    }
    @Override public int hashCode() { return Objects.hash(maTK); }

    @Override public String toString() {
        return "TaiKhoan{maTK='" + maTK + "', tenTK='" + tenTK + "'}";
    }
}

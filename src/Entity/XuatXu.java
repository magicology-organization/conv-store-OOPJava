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

public class XuatXu {
    private String maXX;  // PK
    private String tenXX;

    public XuatXu() {}
    public XuatXu(String maXX, String tenXX) {
        this.maXX = maXX;
        this.tenXX = tenXX;
    }

    public String getMaXX() {
        return maXX;
    }

    public String getTenXX() {
        return tenXX;
    }

    public void setMaXX(String maXX) {
        this.maXX = maXX;
    }

    public void setTenXX(String tenXX) {
        this.tenXX = tenXX;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XuatXu)) return false;
        XuatXu that = (XuatXu) o;
        return Objects.equals(maXX, that.maXX);
    }

    @Override
    public int hashCode() { return Objects.hash(maXX); }

    @Override
    public String toString() {
        return "XuatXu{maXX='" + maXX + "', tenXX='" + tenXX + "'}";
    }
}

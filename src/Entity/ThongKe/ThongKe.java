/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.ThongKe;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ADMIN
 */
public class ThongKe {
    private Date thoiGian;
    private BigDecimal doanhThu;
    private BigDecimal von;

    public ThongKe(Date thoiGian, double doanhThu, double von) {
        this.thoiGian = thoiGian;
        this.doanhThu = BigDecimal.valueOf(doanhThu);
        this.von = BigDecimal.valueOf(von);
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }

    public double getDoanhThu() {
        return doanhThu.doubleValue();
    }

    public void setDoanhThu(double doanhThu) {
        this.doanhThu = BigDecimal.valueOf(doanhThu);
    }

    public double getVon() {
        return von.doubleValue();
    }

    public void setVon(double von) {
        this.von = BigDecimal.valueOf(von);
    }

    public double getLoiNhuan() {
        return doanhThu.subtract(von).doubleValue();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.thoiGian);
        hash = 71 * hash + Objects.hashCode(this.doanhThu);
        hash = 71 * hash + Objects.hashCode(this.von);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThongKe other = (ThongKe) obj;
        if (!Objects.equals(this.doanhThu, other.doanhThu)) {
            return false;
        }
        if (!Objects.equals(this.von, other.von)) {
            return false;
        }
        return Objects.equals(this.thoiGian, other.thoiGian);
    }

    @Override
    public String toString() {
        return "ThongKe{" + "thoiGian=" + thoiGian + ", doanhThu=" + doanhThu + ", von=" + von + '}';
    }
}
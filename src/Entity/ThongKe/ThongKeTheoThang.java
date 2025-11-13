/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity.ThongKe;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author ADMIN
 */
public class ThongKeTheoThang {
    private int thang;
    private BigDecimal doanhThu;
    private BigDecimal von;

    public ThongKeTheoThang(int thang, double doanhThu, double von) {
        this.thang = thang;
        this.doanhThu = BigDecimal.valueOf(doanhThu);
        this.von = BigDecimal.valueOf(von);
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
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
        int hash = 3;
        hash = 97 * hash + this.thang;
        hash = 97 * hash + Objects.hashCode(this.doanhThu);
        hash = 97 * hash + Objects.hashCode(this.von);
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
        final ThongKeTheoThang other = (ThongKeTheoThang) obj;
        if (this.thang != other.thang) {
            return false;
        }
        if (!Objects.equals(this.doanhThu, other.doanhThu)) {
            return false;
        }
        return Objects.equals(this.von, other.von);
    }

    @Override
    public String toString() {
        return "ThongKeTheoThang{" + "thang=" + thang + ", doanhThu=" + doanhThu + ", von=" + von + '}';
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.frame.ThongKe;

import DAO.ThongKe.ThongKeDAO;
import Entity.ThongKe.ThongKe;
import Entity.ThongKe.ThongKeTheoThang;
import GUI.barchart.ModelChart;
import GUI.barchart.chart;
import Utils.Formatter;
import Utils.MessageDialog;
import Utils.TableSorter;
import java.awt.Color;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author roxan
 */
public class frmThongKeThang extends javax.swing.JPanel {

    private final int currentYear = LocalDate.now().getYear();
    private List<ThongKeTheoThang> listTK;
    private DefaultTableModel modal;

    public frmThongKeThang() {
        initComponents(); // Khởi tạo component trước
        chartLayout(); // Thiết lập legend biểu đồ
        tableLayout(); // Thiết lập bảng
        loadDataset(); // Load dữ liệu ban đầu
    }

    // Thiết lập legend cho biểu đồ
    private void chartLayout() {
        pThongKe.addLegend("Doanh thu", new Color(135, 189, 245));
        pThongKe.addLegend("Vốn", new Color(245, 189, 135));
        pThongKe.addLegend("Lợi nhuận", new Color(139, 225, 196));
    }

    // Tải dữ liệu vào biểu đồ
    private void loadChart() {
        pThongKe.clear();
        if (listTK == null || listTK.isEmpty())
            return;
        for (ThongKeTheoThang e : listTK) {
            pThongKe.addData(new ModelChart("Tháng " + e.getThang(),
                    new double[] { e.getDoanhThu(), e.getVon(), e.getLoiNhuan() }));

        }
        pThongKe.start();
    }

    // Thiết lập bảng
    private void tableLayout() {
        String[] header = { "Ngày", "Doanh thu", "Vốn", "Lợi nhuận" };
        modal = new DefaultTableModel();
        modal.setColumnIdentifiers(header);
        table.setModel(modal);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);

        sortTable();
    }

    // Sắp xếp bảng
    private void sortTable() {
        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.STRING_COMPARATOR);
    }

    // Load dữ liệu vào bảng
    private void loadTable() {
        modal.setRowCount(0);
        if (listTK == null || listTK.isEmpty())
            return;

        for (ThongKeTheoThang e : listTK) {
            modal.addRow(new Object[] {
                    e.getThang() + "",
                    Formatter.FormatVND(e.getDoanhThu()),
                    Formatter.FormatVND(e.getVon()),
                    Formatter.FormatVND(e.getLoiNhuan())
            });
        }
    }

    // Tính tổng doanh thu, chi phí, lợi nhuận
    private void tinhTong() {
        int colDoanhThu = 1;
        int colChiPhi = 2;
        double tongDoanhThu = 0;
        double tongVon = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            Object valDT = table.getValueAt(i, colDoanhThu);
            if (valDT != null) {
                String sDT = valDT.toString().replaceAll("[^0-9\\-]", "");
                if (!sDT.isEmpty()) {
                    try {
                        tongDoanhThu += Double.parseDouble(sDT);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            Object valCP = table.getValueAt(i, colChiPhi);
            if (valCP != null) {
                String sCP = valCP.toString().replaceAll("[^0-9\\-]", "");
                if (!sCP.isEmpty()) {
                    try {
                        tongVon += Double.parseDouble(sCP);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        double loiNhuan = tongDoanhThu - tongVon;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

        lblTongDoanhThu.setText(nf.format(tongDoanhThu) + "đ");
        lblTongVon.setText(nf.format(tongVon) + "đ");
        txtTongLoiNhuan.setText(nf.format(loiNhuan) + "đ");
    }

    private void loadDataset() {
        int month = txtThang.getMonth() + 1; // JMonthChooser trả về 0-11
        int year = txtNam.getValue();

        // Lấy thống kê tất cả các ngày trong tháng
        List<ThongKe> listNgay = new ThongKeDAO().getThongKeTheoThang(month, year);

        // Chuyển đổi dữ liệu sang ThongKeTheoThang để hiển thị biểu đồ và bảng
        listTK = new java.util.ArrayList<>();
        for (ThongKe tk : listNgay) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(tk.getThoiGian());
            int day = cal.get(Calendar.DAY_OF_MONTH); // Lấy ngày trong tháng

            // Thêm vào listTK với day là "tháng" trong ThongKeTheoThang
            listTK.add(new ThongKeTheoThang(day, tk.getDoanhThu(), tk.getVon()));
        }

        // Cập nhật biểu đồ
        pThongKe.clear();
        if (listTK != null && !listTK.isEmpty()) {
            for (ThongKeTheoThang tk : listTK) {
                pThongKe.addData(new ModelChart("Ngày " + tk.getThang(),
                        new double[] { tk.getDoanhThu(), tk.getVon(), tk.getLoiNhuan() }));
            }
            pThongKe.start();
        }

        // Cập nhật bảng
        loadTable();
        tinhTong();
    }

    // Kiểm tra input năm hợp lệ
    private boolean isValidFilterFields() {
        int year = txtNam.getValue();
        if (year <= 1900 || year > currentYear) {
            MessageDialog.warring(this, "Số năm phải từ 1900 đến " + currentYear);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pNorth = new javax.swing.JPanel();
        lblThang = new javax.swing.JLabel();
        txtThang = new com.toedter.calendar.JMonthChooser();
        lblNam = new javax.swing.JLabel();
        txtNam = new com.toedter.components.JSpinField();
        btnThongKe = new javax.swing.JButton();
        pSouth = new javax.swing.JPanel();
        pSouth_all = new javax.swing.JPanel();
        pTong = new javax.swing.JPanel();
        plblTong = new javax.swing.JPanel();
        lblTong = new javax.swing.JLabel();
        pDoanhThu = new javax.swing.JPanel();
        lblDoanhThu = new javax.swing.JLabel();
        lblTongDoanhThu = new javax.swing.JLabel();
        pVon = new javax.swing.JPanel();
        lblVon = new javax.swing.JLabel();
        lblTongVon = new javax.swing.JLabel();
        pLoiNhuan = new javax.swing.JPanel();
        txtLoiNhuan = new javax.swing.JLabel();
        txtTongLoiNhuan = new javax.swing.JLabel();
        jspTable = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jspThongKe = new javax.swing.JScrollPane();
        pThongKe = new GUI.barchart.chart();

        setMinimumSize(new java.awt.Dimension(1200, 600));
        setPreferredSize(new java.awt.Dimension(1200, 600));
        setLayout(new java.awt.BorderLayout());

        pNorth.setBackground(new java.awt.Color(247, 247, 247));
        pNorth.setPreferredSize(new java.awt.Dimension(1200, 80));
        pNorth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 30));

        lblThang.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblThang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblThang.setText("Tháng:");
        lblThang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblThang.setPreferredSize(new java.awt.Dimension(60, 30));
        pNorth.add(lblThang);

        txtThang.setPreferredSize(new java.awt.Dimension(130, 26));
        pNorth.add(txtThang);

        lblNam.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblNam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNam.setText("Năm:");
        lblNam.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblNam.setPreferredSize(new java.awt.Dimension(40, 30));
        pNorth.add(lblNam);

        txtNam.setPreferredSize(new java.awt.Dimension(80, 26));
        txtNam.setValue(2025);
        pNorth.add(txtNam);

        btnThongKe.setBackground(new java.awt.Color(51, 153, 255));
        btnThongKe.setForeground(new java.awt.Color(204, 255, 255));
        btnThongKe.setText("Thống kê");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });
        pNorth.add(btnThongKe);

        add(pNorth, java.awt.BorderLayout.NORTH);

        pSouth.setMinimumSize(new java.awt.Dimension(1200, 250));
        pSouth.setPreferredSize(new java.awt.Dimension(1200, 300));
        pSouth.setLayout(new java.awt.BorderLayout());

        pSouth_all.setMinimumSize(new java.awt.Dimension(1200, 300));
        pSouth_all.setPreferredSize(new java.awt.Dimension(1200, 300));
        pSouth_all.setLayout(new java.awt.BorderLayout());

        plblTong.setMaximumSize(new java.awt.Dimension(230, 30));
        plblTong.setMinimumSize(new java.awt.Dimension(230, 30));
        plblTong.setPreferredSize(new java.awt.Dimension(230, 30));

        lblTong.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        lblTong.setForeground(new java.awt.Color(255, 0, 51));
        lblTong.setText("TỔNG:");
        lblTong.setMaximumSize(new java.awt.Dimension(60, 25));
        lblTong.setMinimumSize(new java.awt.Dimension(60, 25));
        lblTong.setPreferredSize(new java.awt.Dimension(60, 25));
        lblTong.setRequestFocusEnabled(false);
        plblTong.add(lblTong);

        pTong.add(plblTong);

        pDoanhThu.setPreferredSize(new java.awt.Dimension(250, 30));
        pDoanhThu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDoanhThu.setText("Doanh thu:");
        pDoanhThu.add(lblDoanhThu);

        lblTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongDoanhThu.setForeground(new java.awt.Color(102, 102, 102));
        lblTongDoanhThu.setPreferredSize(new java.awt.Dimension(150, 30));
        pDoanhThu.add(lblTongDoanhThu);

        pTong.add(pDoanhThu);

        pVon.setMinimumSize(new java.awt.Dimension(250, 30));
        pVon.setPreferredSize(new java.awt.Dimension(250, 30));
        pVon.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblVon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblVon.setText("Vốn:");
        pVon.add(lblVon);

        lblTongVon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongVon.setForeground(new java.awt.Color(102, 102, 102));
        lblTongVon.setMaximumSize(new java.awt.Dimension(150, 30));
        lblTongVon.setMinimumSize(new java.awt.Dimension(150, 30));
        lblTongVon.setPreferredSize(new java.awt.Dimension(150, 30));
        pVon.add(lblTongVon);

        pTong.add(pVon);

        pLoiNhuan.setMinimumSize(new java.awt.Dimension(250, 30));
        pLoiNhuan.setPreferredSize(new java.awt.Dimension(250, 30));

        txtLoiNhuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtLoiNhuan.setText("Lợi nhuận:");
        pLoiNhuan.add(txtLoiNhuan);

        txtTongLoiNhuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTongLoiNhuan.setForeground(new java.awt.Color(102, 102, 102));
        txtTongLoiNhuan.setMaximumSize(new java.awt.Dimension(150, 30));
        txtTongLoiNhuan.setMinimumSize(new java.awt.Dimension(150, 30));
        txtTongLoiNhuan.setPreferredSize(new java.awt.Dimension(150, 30));
        pLoiNhuan.add(txtTongLoiNhuan);

        pTong.add(pLoiNhuan);

        pSouth_all.add(pTong, java.awt.BorderLayout.SOUTH);

        jspTable.setMinimumSize(new java.awt.Dimension(1200, 250));
        jspTable.setPreferredSize(new java.awt.Dimension(1200, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Doanh thu", "Vốn", "Lợi nhuận"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setFocusable(false);
        table.setRowHeight(40);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        jspTable.setViewportView(table);

        pSouth_all.add(jspTable, java.awt.BorderLayout.CENTER);

        pSouth.add(pSouth_all, java.awt.BorderLayout.SOUTH);

        add(pSouth, java.awt.BorderLayout.SOUTH);

        jspThongKe.setPreferredSize(new java.awt.Dimension(1200, 110));

        pThongKe.setMinimumSize(new java.awt.Dimension(2000, 100));
        pThongKe.setPreferredSize(new java.awt.Dimension(2700, 350));
        jspThongKe.setViewportView(pThongKe);

        add(jspThongKe, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThongKeActionPerformed
        if (isValidFilterFields()) {
            loadDataset();
        }
    }// GEN-LAST:event_btnThongKeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThongKe;
    private javax.swing.JScrollPane jspTable;
    private javax.swing.JScrollPane jspThongKe;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblNam;
    private javax.swing.JLabel lblThang;
    private javax.swing.JLabel lblTong;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JLabel lblTongVon;
    private javax.swing.JLabel lblVon;
    private javax.swing.JPanel pDoanhThu;
    private javax.swing.JPanel pLoiNhuan;
    private javax.swing.JPanel pNorth;
    private javax.swing.JPanel pSouth;
    private javax.swing.JPanel pSouth_all;
    private GUI.barchart.chart pThongKe;
    private javax.swing.JPanel pTong;
    private javax.swing.JPanel pVon;
    private javax.swing.JPanel plblTong;
    private javax.swing.JTable table;
    private javax.swing.JLabel txtLoiNhuan;
    private com.toedter.components.JSpinField txtNam;
    private com.toedter.calendar.JMonthChooser txtThang;
    private javax.swing.JLabel txtTongLoiNhuan;
    // End of variables declaration//GEN-END:variables
}

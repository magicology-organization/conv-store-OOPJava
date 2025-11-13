/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.frame.ThongKe;

import Entity.ThongKe.ThongKe;
import GUI.barchart.ModelChart;
import java.time.LocalDate;
import java.util.List;
import GUI.barchart.chart;
import Utils.Formatter;
import Utils.TableSorter;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import DAO.ThongKe.ThongKeDAO;
import javax.swing.JTable;

/**
 *
 * @author roxan
 */
public class frmThongKeTheoNgay extends javax.swing.JPanel {
    private List<ThongKe> listTK; // Danh sách thống kê theo ngày
    private DefaultTableModel modal;
    private ThongKeDAO thongKeDAO; // Đối tượng ThongKeDAO để truy vấn dữ liệu

    public frmThongKeTheoNgay() {
        initComponents();
        thongKeDAO = new ThongKeDAO(); // Khởi tạo ThongKeDAO
        chartLayout();
        tableLayout();
        loadDataset();
        tinhTong();
    }

    private void tinhTong() {
        int colDoanhThu = 1;
        int colChiPhi = 2;
        long tongDoanhThu = 0;
        long tongChiPhi = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            // Tính doanh thu
            Object valDT = table.getValueAt(i, colDoanhThu);
            if (valDT != null) {
                String sDT = valDT.toString().trim().replaceAll("[^0-9\\-]", "");
                if (!sDT.isEmpty()) {
                    try {
                        tongDoanhThu += Long.parseLong(sDT);
                    } catch (NumberFormatException e) {
                    }
                }
            }

            // Tính chi phí
            Object valCP = table.getValueAt(i, colChiPhi);
            if (valCP != null) {
                String sCP = valCP.toString().trim().replaceAll("[^0-9\\-]", "");
                if (!sCP.isEmpty()) {
                    try {
                        tongChiPhi += Long.parseLong(sCP);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        // Lợi nhuận
        long loiNhuan = tongDoanhThu - tongChiPhi;

        // Định dạng số với dấu phân cách và đính kèm "đ"
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

        lblTongDoanhThu.setText(nf.format(tongDoanhThu) + "đ");
        lblTongVon.setText(nf.format(tongChiPhi) + "đ");
        lblTongLoiNhuan.setText(nf.format(loiNhuan) + "đ");
    }

    // Thiết lập layout cho biểu đồ
    private void chartLayout() {
        // Đặt ngày mặc định là hôm nay
        txtChonNgay.setDate(java.sql.Date.valueOf(LocalDate.now()));

        // Thêm legend cho biểu đồ
        pThongKe.addLegend("Doanh thu", new Color(135, 189, 245));
        pThongKe.addLegend("Vốn", new Color(245, 189, 135));
        pThongKe.addLegend("Lợi nhuận", new Color(139, 225, 196));

        pThongKe.start();
    }

    // Tải dữ liệu vào biểu đồ (hiển thị từng giờ)
    private void loadChart() {
        for (int hour = 0; hour < listTK.size(); hour++) {
            ThongKe tk = listTK.get(hour);
            int gio = tk.getThoiGian().getHours(); // lấy giờ từ Date
            double doanhThu = tk.getDoanhThu();
            double von = tk.getVon();
            double loiNhuan = tk.getLoiNhuan();

            pThongKe.addData(new ModelChart(
                    String.format("%02d:00-%02d:59", gio, gio),
                    new double[] { doanhThu, von, loiNhuan }));
        }
        pThongKe.updateChartWidth();
    }

    // Thiết lập layout cho bảng
    private void tableLayout() {
        String[] header = new String[] { "Thời gian", "Doanh thu", "Vốn", "Lợi nhuận" };
        modal = new DefaultTableModel();
        modal.setColumnIdentifiers(header);
        table.setModel(modal);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        table.setDefaultRenderer(Object.class, centerRenderer);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);

        sortTable();
    }

    // Sắp xếp bảng
    private void sortTable() {
        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.STRING_COMPARATOR);
    }

    // Tải dữ liệu vào bảng
    private void loadTable() {
        modal.setRowCount(0);
        int gio = 0;
        for (ThongKe e : listTK) {
            modal.addRow(new Object[] {
                    String.format("%02d:00 - %02d:59", gio, gio),
                    Formatter.FormatVND(e.getDoanhThu()),
                    Formatter.FormatVND(e.getVon()),
                    Formatter.FormatVND(e.getLoiNhuan())
            });
            gio++;
        }
    }

    // Tải dữ liệu (biểu đồ và bảng)
    private void loadDataset() {
        pThongKe.clear();

        // Lấy ngày được chọn, nếu null thì lấy ngày hiện tại
        java.util.Date ngayChon = txtChonNgay.getDate();
        if (ngayChon == null) {
            ngayChon = java.sql.Date.valueOf(LocalDate.now());
        }

        // Gọi DAO để lấy thống kê theo giờ trong ngày được chọn
        listTK = thongKeDAO.getThongKeTheoGio(new java.sql.Date(ngayChon.getTime()));

        loadChart();
        loadTable();
        tinhTong();
        pThongKe.start();
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
        lblChart1 = new javax.swing.JLabel();
        txtChonNgay = new com.toedter.calendar.JDateChooser();
        btnThongKe = new javax.swing.JButton();
        pSouth = new javax.swing.JPanel();
        pSouth_all = new javax.swing.JPanel();
        jspTable = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        pTong = new javax.swing.JPanel();
        plblTong = new javax.swing.JPanel();
        lblTong = new javax.swing.JLabel();
        pDoanhThu = new javax.swing.JPanel();
        lblDoanhThu = new javax.swing.JLabel();
        lblTongDoanhThu = new javax.swing.JLabel();
        pVon = new javax.swing.JPanel();
        lblChiPhi = new javax.swing.JLabel();
        lblTongVon = new javax.swing.JLabel();
        pLoiNhuan = new javax.swing.JPanel();
        lblLoiNhuan = new javax.swing.JLabel();
        lblTongLoiNhuan = new javax.swing.JLabel();
        jspThongKe = new javax.swing.JScrollPane();
        pThongKe = new GUI.barchart.chart();

        setMinimumSize(new java.awt.Dimension(829, 624));
        setPreferredSize(new java.awt.Dimension(1200, 600));
        setLayout(new java.awt.BorderLayout());

        pNorth.setBackground(new java.awt.Color(247, 247, 247));
        pNorth.setMinimumSize(new java.awt.Dimension(1200, 40));
        pNorth.setPreferredSize(new java.awt.Dimension(1200, 80));
        pNorth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 30));

        lblChart1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblChart1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChart1.setText("Ngày:");
        lblChart1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblChart1.setPreferredSize(new java.awt.Dimension(40, 30));
        pNorth.add(lblChart1);

        txtChonNgay.setMinimumSize(new java.awt.Dimension(182, 22));
        txtChonNgay.setPreferredSize(new java.awt.Dimension(150, 30));
        pNorth.add(txtChonNgay);

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

        pSouth_all.setPreferredSize(new java.awt.Dimension(1200, 300));
        pSouth_all.setLayout(new java.awt.BorderLayout());

        jspTable.setMinimumSize(new java.awt.Dimension(1200, 250));
        jspTable.setPreferredSize(new java.awt.Dimension(1200, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thời gian", "Doanh thu", "Vốn", "Lợi nhuận"
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
        table.setPreferredSize(null);
        table.setRowHeight(40);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        jspTable.setViewportView(table);

        pSouth_all.add(jspTable, java.awt.BorderLayout.CENTER);

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

        lblChiPhi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblChiPhi.setText("Vốn:");
        pVon.add(lblChiPhi);

        lblTongVon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongVon.setForeground(new java.awt.Color(102, 102, 102));
        lblTongVon.setMaximumSize(new java.awt.Dimension(150, 30));
        lblTongVon.setMinimumSize(new java.awt.Dimension(150, 30));
        lblTongVon.setPreferredSize(new java.awt.Dimension(150, 30));
        pVon.add(lblTongVon);

        pTong.add(pVon);

        pLoiNhuan.setMinimumSize(new java.awt.Dimension(250, 30));
        pLoiNhuan.setPreferredSize(new java.awt.Dimension(250, 30));

        lblLoiNhuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLoiNhuan.setText("Lợi nhuận:");
        pLoiNhuan.add(lblLoiNhuan);

        lblTongLoiNhuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongLoiNhuan.setForeground(new java.awt.Color(102, 102, 102));
        lblTongLoiNhuan.setMaximumSize(new java.awt.Dimension(150, 30));
        lblTongLoiNhuan.setMinimumSize(new java.awt.Dimension(150, 30));
        lblTongLoiNhuan.setPreferredSize(new java.awt.Dimension(150, 30));
        pLoiNhuan.add(lblTongLoiNhuan);

        pTong.add(pLoiNhuan);

        pSouth_all.add(pTong, java.awt.BorderLayout.SOUTH);

        pSouth.add(pSouth_all, java.awt.BorderLayout.SOUTH);

        add(pSouth, java.awt.BorderLayout.SOUTH);

        jspThongKe.setPreferredSize(new java.awt.Dimension(1200, 110));

        pThongKe.setMinimumSize(new java.awt.Dimension(2000, 100));
        pThongKe.setPreferredSize(new java.awt.Dimension(2000, 350));
        jspThongKe.setViewportView(pThongKe);

        add(jspThongKe, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private boolean isValidFilterFields() {
        return true;
    }

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThongKeActionPerformed
        if (isValidFilterFields()) {
            loadDataset();
        }
    }// GEN-LAST:event_btnThongKeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThongKe;
    private javax.swing.JScrollPane jspTable;
    private javax.swing.JScrollPane jspThongKe;
    private javax.swing.JLabel lblChart1;
    private javax.swing.JLabel lblChiPhi;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblLoiNhuan;
    private javax.swing.JLabel lblTong;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JLabel lblTongLoiNhuan;
    private javax.swing.JLabel lblTongVon;
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
    private com.toedter.calendar.JDateChooser txtChonNgay;
    // End of variables declaration//GEN-END:variables
}

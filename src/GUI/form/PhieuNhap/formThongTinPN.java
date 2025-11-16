/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.form.PhieuNhap;

import Entity.NhanVien.NhanVien;
import Entity.PhieuNhap.CTPhieuNhap;
import Entity.PhieuNhap.NhaCungCap;
import Entity.PhieuNhap.PhieuNhap;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class formThongTinPN extends javax.swing.JDialog {
    private final DAO.PhieuNhap.NhaCungCapDAO nccDao = new DAO.PhieuNhap.NhaCungCapDAO();
    private final DAO.NhanVien.NhanVienDAO nvDao = new DAO.NhanVien.NhanVienDAO();
    private final DAO.PhieuNhap.CTPhieuNhapDAO ctDao = new DAO.PhieuNhap.CTPhieuNhapDAO();
    private final DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();

    /**
     * Creates new form formThongTinPN
     * 
     * @param parent
     * @param modal
     * @param pn
     */
    public formThongTinPN(java.awt.Frame parent, boolean modal, PhieuNhap pn) {
        initComponents();
        configureTable();
        setThongTinPN(pn);
        addTableMouseListener();
    }

    private void configureTable() {
        // ====== Không cho sửa nội dung ======
        table.setDefaultEditor(Object.class, null);

        // ====== Căn giữa nội dung tất cả các cột ======
        for (int i = 0; i < table.getColumnCount(); i++) {
            javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // ====== Chỉ cho phép chọn 1 dòng tại 1 thời điểm ======
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ====== Chỉ cho chọn dòng (không chọn cột) ======
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // ====== Tùy chọn giao diện ======
        table.getTableHeader().setReorderingAllowed(false); // Không cho kéo thả cột
        table.setRowHeight(40);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
    }

    private void setThongTinPN(PhieuNhap pn) {
        if (pn == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thông tin phiếu nhập!",
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Lấy đầy đủ thông tin NCC và NV
            NhaCungCap ncc = nccDao.findById(pn.getNCC() != null ? pn.getNCC().getMaNCC() : "").orElse(null);
            NhanVien nv = nvDao.findById(pn.getNhanVien() != null ? pn.getNhanVien().getMaNV() : "").orElse(null);
            pn.setNCC(ncc);
            pn.setNhanVien(nv);

            // ===== Gán thông tin cơ bản =====
            txtMaPN.setText(pn.getMaPN());
            txtTenNCC.setText(pn.getNCC() != null ? pn.getNCC().getTenNCC() : "");
            txtSDTNCC.setText(pn.getNCC() != null ? pn.getNCC().getSdt() : "");
            txtTenNV.setText(pn.getNhanVien() != null ? pn.getNhanVien().getTenNV() : "");

            // ===== Ngày nhập =====
            java.time.LocalDateTime ngayNhap = pn.getThoiGian();
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            txtNgayNhap.setText(ngayNhap != null ? ngayNhap.format(fmt) : "");

            // ===== Tổng tiền =====
            List<CTPhieuNhap> cts = ctDao.findAllByMaPN(pn.getMaPN());
            pn.setChiTietPhieuNhap(cts);
            BigDecimal tongTien = pn.getTongTien();
            txtTong.setText(tongTien != null ? String.format("%,.0f", tongTien) : "");

            // ===== Nạp chi tiết phiếu nhập =====
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
            model.setRowCount(0);

            int stt = 1;
            for (CTPhieuNhap ct : cts) {
                var sp = spDao.findById(ct.getSanPham().getMaSP()).orElse(null);
                String tenSP = sp != null ? sp.getTenSP() : "";
                int soLuong = ct.getSoLuong();
                java.math.BigDecimal donGia = ct.getDonGia();
                java.math.BigDecimal thanhTien = donGia.multiply(java.math.BigDecimal.valueOf(soLuong));

                model.addRow(new Object[] {
                        stt++,
                        ct.getSanPham().getMaSP(),
                        tenSP,
                        soLuong,
                        String.format("%,.0f", donGia),
                        String.format("%,.0f", thanhTien)
                });
            }

            // ===== Reset ảnh sản phẩm về icon mặc định =====
            lblAnhSP.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("./icon/image.svg"));

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải thông tin phiếu nhập!",
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableMouseListener() {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int r = table.getSelectedRow();
                if (r < 0)
                    return;

                String maSP = String.valueOf(table.getValueAt(r, 1));
                try {
                    Entity.SanPham.SanPham sp = spDao.findById(maSP).orElse(null);
                    if (sp != null && sp.getAnhSP() != null) {
                        byte[] anh = sp.getAnhSP();
                        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(anh);
                        java.awt.Image img = icon.getImage().getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
                        lblAnhSP.setIcon(new javax.swing.ImageIcon(img));
                    } else {
                        lblAnhSP.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("./icon/image.svg"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    lblAnhSP.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("./icon/image.svg"));
                }
            }
        });
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
        pTilte = new javax.swing.JPanel();
        lblTilte1 = new javax.swing.JLabel();
        pThongTinHD = new javax.swing.JPanel();
        pThongTinNorth = new javax.swing.JPanel();
        pMaPN = new javax.swing.JPanel();
        lblMaPN = new javax.swing.JLabel();
        txtMaPN = new javax.swing.JTextField();
        pTenNCC = new javax.swing.JPanel();
        lblTenNCC = new javax.swing.JLabel();
        txtTenNCC = new javax.swing.JTextField();
        pSDTNCC = new javax.swing.JPanel();
        lblSDTNCC = new javax.swing.JLabel();
        txtSDTNCC = new javax.swing.JTextField();
        pThongTinCenter = new javax.swing.JPanel();
        pNgayNhap = new javax.swing.JPanel();
        lblNgayNhap = new javax.swing.JLabel();
        txtNgayNhap = new javax.swing.JTextField();
        pTenNV = new javax.swing.JPanel();
        lblTenNV = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        pCenter = new javax.swing.JPanel();
        pAnh = new javax.swing.JPanel();
        anhSP = new javax.swing.JPanel();
        lblAnhSP = new javax.swing.JLabel();
        tableSP = new javax.swing.JPanel();
        pTitleCenter = new javax.swing.JPanel();
        lblTitle2 = new javax.swing.JLabel();
        spTableSP = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        pTongPN = new javax.swing.JPanel();
        pTongPhieuNhap = new javax.swing.JPanel();
        lblTongPN = new javax.swing.JLabel();
        txtTong = new javax.swing.JTextField();
        pSouth = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 600));

        pNorth.setBackground(new java.awt.Color(204, 255, 204));
        pNorth.setMinimumSize(new java.awt.Dimension(1200, 170));
        pNorth.setPreferredSize(new java.awt.Dimension(1200, 170));
        pNorth.setLayout(new java.awt.BorderLayout());

        pTilte.setBackground(new java.awt.Color(204, 255, 204));
        pTilte.setMinimumSize(new java.awt.Dimension(1200, 50));
        pTilte.setPreferredSize(new java.awt.Dimension(1200, 50));
        pTilte.setLayout(new java.awt.BorderLayout());

        lblTilte1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTilte1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTilte1.setText("THÔNG TIN PHIẾU NHẬP");
        lblTilte1.setPreferredSize(new java.awt.Dimension(149, 40));
        pTilte.add(lblTilte1, java.awt.BorderLayout.CENTER);

        pNorth.add(pTilte, java.awt.BorderLayout.NORTH);

        pThongTinHD.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinHD.setMinimumSize(new java.awt.Dimension(1200, 70));
        pThongTinHD.setPreferredSize(new java.awt.Dimension(1200, 170));
        pThongTinHD.setLayout(new java.awt.BorderLayout());

        pThongTinNorth.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinNorth.setPreferredSize(new java.awt.Dimension(1200, 60));
        pThongTinNorth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        pMaPN.setBackground(new java.awt.Color(255, 255, 255));
        pMaPN.setPreferredSize(new java.awt.Dimension(340, 40));
        pMaPN.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblMaPN.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblMaPN.setText("Mã phiếu nhập:");
        lblMaPN.setPreferredSize(new java.awt.Dimension(120, 40));
        pMaPN.add(lblMaPN);

        txtMaPN.setEditable(false);
        txtMaPN.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtMaPN.setPreferredSize(new java.awt.Dimension(200, 40));
        pMaPN.add(txtMaPN);

        pThongTinNorth.add(pMaPN);

        pTenNCC.setBackground(new java.awt.Color(255, 255, 255));
        pTenNCC.setPreferredSize(new java.awt.Dimension(440, 40));
        pTenNCC.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTenNCC.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTenNCC.setText("Tên khách hàng:");
        lblTenNCC.setPreferredSize(new java.awt.Dimension(120, 40));
        pTenNCC.add(lblTenNCC);

        txtTenNCC.setEditable(false);
        txtTenNCC.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtTenNCC.setPreferredSize(new java.awt.Dimension(300, 40));
        pTenNCC.add(txtTenNCC);

        pThongTinNorth.add(pTenNCC);

        pSDTNCC.setBackground(new java.awt.Color(255, 255, 255));
        pSDTNCC.setPreferredSize(new java.awt.Dimension(340, 40));
        pSDTNCC.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblSDTNCC.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblSDTNCC.setText("Số điện thoại:");
        lblSDTNCC.setPreferredSize(new java.awt.Dimension(120, 40));
        pSDTNCC.add(lblSDTNCC);

        txtSDTNCC.setEditable(false);
        txtSDTNCC.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtSDTNCC.setPreferredSize(new java.awt.Dimension(200, 40));
        pSDTNCC.add(txtSDTNCC);

        pThongTinNorth.add(pSDTNCC);

        pThongTinHD.add(pThongTinNorth, java.awt.BorderLayout.NORTH);

        pThongTinCenter.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinCenter.setPreferredSize(new java.awt.Dimension(1200, 60));
        pThongTinCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        pNgayNhap.setBackground(new java.awt.Color(255, 255, 255));
        pNgayNhap.setPreferredSize(new java.awt.Dimension(340, 40));
        pNgayNhap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblNgayNhap.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblNgayNhap.setText("Ngày nhập:");
        lblNgayNhap.setPreferredSize(new java.awt.Dimension(90, 40));
        pNgayNhap.add(lblNgayNhap);

        txtNgayNhap.setEditable(false);
        txtNgayNhap.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtNgayNhap.setPreferredSize(new java.awt.Dimension(200, 40));
        pNgayNhap.add(txtNgayNhap);

        pThongTinCenter.add(pNgayNhap);

        pTenNV.setBackground(new java.awt.Color(255, 255, 255));
        pTenNV.setPreferredSize(new java.awt.Dimension(440, 40));
        pTenNV.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTenNV.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTenNV.setText("Tên nhân viên:");
        lblTenNV.setPreferredSize(new java.awt.Dimension(120, 40));
        pTenNV.add(lblTenNV);

        txtTenNV.setEditable(false);
        txtTenNV.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtTenNV.setPreferredSize(new java.awt.Dimension(300, 40));
        pTenNV.add(txtTenNV);

        pThongTinCenter.add(pTenNV);

        pThongTinHD.add(pThongTinCenter, java.awt.BorderLayout.PAGE_END);

        pNorth.add(pThongTinHD, java.awt.BorderLayout.CENTER);

        getContentPane().add(pNorth, java.awt.BorderLayout.NORTH);

        pCenter.setBackground(new java.awt.Color(255, 255, 255));
        pCenter.setLayout(new java.awt.BorderLayout());

        pAnh.setBackground(new java.awt.Color(255, 255, 255));
        pAnh.setPreferredSize(new java.awt.Dimension(400, 100));

        anhSP.setBackground(new java.awt.Color(255, 255, 255));
        anhSP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(237, 237, 237), 2, true));
        anhSP.setPreferredSize(new java.awt.Dimension(300, 300));
        anhSP.setLayout(new java.awt.BorderLayout());

        lblAnhSP.setBackground(new java.awt.Color(255, 255, 255));
        lblAnhSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnhSP.setIcon(new FlatSVGIcon("./icon/image.svg"));
        lblAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnhSP.setPreferredSize(new java.awt.Dimension(200, 100));
        anhSP.add(lblAnhSP, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pAnhLayout = new javax.swing.GroupLayout(pAnh);
        pAnh.setLayout(pAnhLayout);
        pAnhLayout.setHorizontalGroup(
            pAnhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pAnhLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(anhSP, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        pAnhLayout.setVerticalGroup(
            pAnhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pAnhLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(anhSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );

        pCenter.add(pAnh, java.awt.BorderLayout.WEST);

        tableSP.setPreferredSize(new java.awt.Dimension(800, 400));
        tableSP.setLayout(new java.awt.BorderLayout());

        pTitleCenter.setBackground(new java.awt.Color(204, 255, 204));
        pTitleCenter.setMinimumSize(new java.awt.Dimension(100, 60));
        pTitleCenter.setPreferredSize(new java.awt.Dimension(500, 30));
        pTitleCenter.setLayout(new java.awt.BorderLayout());

        lblTitle2.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        lblTitle2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle2.setText("Chi tiết phiếu nhập");
        pTitleCenter.add(lblTitle2, java.awt.BorderLayout.CENTER);

        tableSP.add(pTitleCenter, java.awt.BorderLayout.NORTH);

        spTableSP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 1, true));
        spTableSP.setPreferredSize(new java.awt.Dimension(452, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        spTableSP.setViewportView(table);

        tableSP.add(spTableSP, java.awt.BorderLayout.CENTER);

        pTongPN.setBackground(new java.awt.Color(255, 255, 255));
        pTongPN.setPreferredSize(new java.awt.Dimension(800, 60));
        pTongPN.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        pTongPhieuNhap.setBackground(new java.awt.Color(255, 255, 255));
        pTongPhieuNhap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTongPN.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblTongPN.setForeground(new java.awt.Color(255, 51, 0));
        lblTongPN.setText("Tổng phiếu:");
        lblTongPN.setPreferredSize(new java.awt.Dimension(120, 40));
        pTongPhieuNhap.add(lblTongPN);

        txtTong.setEditable(false);
        txtTong.setFont(new java.awt.Font("Roboto Mono Medium", 0, 14)); // NOI18N
        txtTong.setForeground(new java.awt.Color(255, 51, 0));
        txtTong.setFocusable(false);
        txtTong.setPreferredSize(new java.awt.Dimension(200, 40));
        pTongPhieuNhap.add(txtTong);

        pTongPN.add(pTongPhieuNhap);

        tableSP.add(pTongPN, java.awt.BorderLayout.PAGE_END);

        pCenter.add(tableSP, java.awt.BorderLayout.CENTER);

        getContentPane().add(pCenter, java.awt.BorderLayout.CENTER);

        pSouth.setBackground(new java.awt.Color(255, 255, 255));
        pSouth.setPreferredSize(new java.awt.Dimension(1200, 50));
        pSouth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 5));

        btnHuy.setBackground(new java.awt.Color(255, 102, 102));
        btnHuy.setFont(new java.awt.Font("Roboto Mono Medium", 0, 16)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("HỦY BỎ");
        btnHuy.setBorderPainted(false);
        btnHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuy.setFocusPainted(false);
        btnHuy.setFocusable(false);
        btnHuy.setPreferredSize(new java.awt.Dimension(200, 40));
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });
        pSouth.add(btnHuy);

        btnPrint.setBackground(new java.awt.Color(15, 204, 102));
        btnPrint.setFont(new java.awt.Font("Roboto Mono Medium", 0, 16)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("IN PHIẾU");
        btnPrint.setBorderPainted(false);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.setFocusPainted(false);
        btnPrint.setFocusable(false);
        btnPrint.setPreferredSize(new java.awt.Dimension(200, 40));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        pSouth.add(btnPrint);

        getContentPane().add(pSouth, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
    }// GEN-LAST:event_tableMouseClicked

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        dispose();
    }// GEN-LAST:event_btnHuyActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        try {
            String maPN = txtMaPN.getText().trim();
            if (maPN.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không có mã phiếu nhập!", "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Khởi tạo DAO
            DAO.PhieuNhap.PhieuNhapDAO pnDao = new DAO.PhieuNhap.PhieuNhapDAO();
            DAO.PhieuNhap.CTPhieuNhapDAO ctDao = new DAO.PhieuNhap.CTPhieuNhapDAO();

            // Lấy thông tin phiếu nhập
            PhieuNhap pn = pnDao.thongTinIn(maPN).orElse(null);
            if (pn == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu nhập!", "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy chi tiết phiếu nhập
            List<CTPhieuNhap> listCTPN = ctDao.thongTinChiTietIn(maPN);
            if (listCTPN == null || listCTPN.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không có sản phẩm trong phiếu nhập!", "Thông báo",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // In PDF
            Utils.InPDF pdf = new Utils.InPDF();
            pdf.printPhieuNhap(pn, listCTPN);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi in phiếu nhập!\n" + e.getMessage(),
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // /* Set the Nimbus look and feel */
        // //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        // /* If Nimbus (introduced in Java SE 6) is not available, stay with the
        // default look and feel.
        // * For details see
        // http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
        // */
        // try {
        // for (javax.swing.UIManager.LookAndFeelInfo info :
        // javax.swing.UIManager.getInstalledLookAndFeels()) {
        // if ("Nimbus".equals(info.getName())) {
        // javax.swing.UIManager.setLookAndFeel(info.getClassName());
        // break;
        // }
        // }
        // } catch (ClassNotFoundException ex) {
        // java.util.logging.Logger.getLogger(formThongTinPN.class.getName()).log(java.util.logging.Level.SEVERE,
        // null, ex);
        // } catch (InstantiationException ex) {
        // java.util.logging.Logger.getLogger(formThongTinPN.class.getName()).log(java.util.logging.Level.SEVERE,
        // null, ex);
        // } catch (IllegalAccessException ex) {
        // java.util.logging.Logger.getLogger(formThongTinPN.class.getName()).log(java.util.logging.Level.SEVERE,
        // null, ex);
        // } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        // java.util.logging.Logger.getLogger(formThongTinPN.class.getName()).log(java.util.logging.Level.SEVERE,
        // null, ex);
        // }
        // //</editor-fold>
        //
        // /* Create and display the dialog */
        // java.awt.EventQueue.invokeLater(new Runnable() {
        // public void run() {
        // formThongTinPN dialog = new formThongTinPN(new javax.swing.JFrame(), true);
        // dialog.addWindowListener(new java.awt.event.WindowAdapter() {
        // @Override
        // public void windowClosing(java.awt.event.WindowEvent e) {
        // System.exit(0);
        // }
        // });
        // dialog.setVisible(true);
        // }
        // });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel anhSP;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnPrint;
    private javax.swing.JLabel lblAnhSP;
    private javax.swing.JLabel lblMaPN;
    private javax.swing.JLabel lblNgayNhap;
    private javax.swing.JLabel lblSDTNCC;
    private javax.swing.JLabel lblTenNCC;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblTilte1;
    private javax.swing.JLabel lblTitle2;
    private javax.swing.JLabel lblTongPN;
    private javax.swing.JPanel pAnh;
    private javax.swing.JPanel pCenter;
    private javax.swing.JPanel pMaPN;
    private javax.swing.JPanel pNgayNhap;
    private javax.swing.JPanel pNorth;
    private javax.swing.JPanel pSDTNCC;
    private javax.swing.JPanel pSouth;
    private javax.swing.JPanel pTenNCC;
    private javax.swing.JPanel pTenNV;
    private javax.swing.JPanel pThongTinCenter;
    private javax.swing.JPanel pThongTinHD;
    private javax.swing.JPanel pThongTinNorth;
    private javax.swing.JPanel pTilte;
    private javax.swing.JPanel pTitleCenter;
    private javax.swing.JPanel pTongPN;
    private javax.swing.JPanel pTongPhieuNhap;
    private javax.swing.JScrollPane spTableSP;
    private javax.swing.JTable table;
    private javax.swing.JPanel tableSP;
    private javax.swing.JTextField txtMaPN;
    private javax.swing.JTextField txtNgayNhap;
    private javax.swing.JTextField txtSDTNCC;
    private javax.swing.JTextField txtTenNCC;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTong;
    // End of variables declaration//GEN-END:variables
}

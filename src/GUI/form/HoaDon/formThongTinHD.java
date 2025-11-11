/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.form.HoaDon;

import Entity.HoaDon.CTHoaDon;
import Entity.HoaDon.HoaDon;
import Utils.InPDF;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class formThongTinHD extends javax.swing.JDialog {

    /**
     * Creates new form formThongTinHD
     */
    public formThongTinHD(java.awt.Frame parent, boolean modal, String maHD) {
        super(parent, modal);
        initComponents();
        configureTable();
        loadThongTinHoaDon(maHD);
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

    private void loadThongTinHoaDon(String maHD) {
        try {
            DAO.HoaDon.HoaDonDAO hdDao = new DAO.HoaDon.HoaDonDAO();
            DAO.HoaDon.CTHoaDonDAO ctDao = new DAO.HoaDon.CTHoaDonDAO();
            DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();

            // --- Lấy thông tin hóa đơn ---
            var list = hdDao.findAllWithDetails();
            Object[] hdInfo = list.stream()
                    .filter(o -> o[0].equals(maHD))
                    .findFirst()
                    .orElse(null);

            if (hdInfo == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin hóa đơn!",
                        "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            /*
             * Cấu trúc trả về từ findAllWithDetails():
             * [0] = maHD
             * [1] = tenKH
             * [2] = SDT
             * [3] = thoiGian
             * [4] = tenNV
             * [5] = tongHoaDon
             * [6] = tongTien
             * [7] = kieuThanhToan
             */

            // --- Gán thông tin cơ bản ---
            txtMaHD.setText(String.valueOf(hdInfo[0])); // Mã hóa đơn
            txtTenKH.setText(String.valueOf(hdInfo[1])); // Tên khách hàng
            txtSDT.setText(String.valueOf(hdInfo[2])); // Số điện thoại KH
            txtTenNV.setText(String.valueOf(hdInfo[4])); // Tên nhân viên
            txtKieuThanhToan.setText(String.valueOf(hdInfo[7])); // Kiểu thanh toán

            // --- Ngày bán (LocalDateTime -> dd/MM/yyyy HH:mm) ---
            java.time.LocalDateTime ngayBan = (java.time.LocalDateTime) hdInfo[3];
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            txtNgayMua.setText(ngayBan != null ? ngayBan.format(fmt) : "");

            // --- Tổng tiền ---
            java.math.BigDecimal tongTien = (java.math.BigDecimal) hdInfo[6];
            txtTong.setText(String.format("%,.0f", tongTien));

            // --- Nạp chi tiết hóa đơn ---
            java.util.List<Entity.HoaDon.CTHoaDon> cts = ctDao.findAllByMaHD(maHD);
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
            model.setRowCount(0);

            int stt = 1;
            for (Entity.HoaDon.CTHoaDon ct : cts) {
                String maSP = ct.getSanPham().getMaSP();
                Entity.SanPham.SanPham sp = spDao.findById(maSP).orElse(null);
                String tenSP = sp != null ? sp.getTenSP() : "";
                int soLuong = ct.getSoLuong();
                java.math.BigDecimal donGia = ct.getDonGia();
                java.math.BigDecimal thanhTien = donGia.multiply(new java.math.BigDecimal(soLuong));

                model.addRow(new Object[] {
                        stt++, maSP, tenSP,
                        soLuong,
                        String.format("%,.0f", donGia),
                        String.format("%,.0f", thanhTien)
                });
            }

            // --- Reset ảnh sản phẩm ---
            lblAnhSP.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("./icon/image.svg"));

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải thông tin hóa đơn!",
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableMouseListener() {
        DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();

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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pNorth = new javax.swing.JPanel();
        pTilte = new javax.swing.JPanel();
        lblTilte1 = new javax.swing.JLabel();
        pThongTinHD = new javax.swing.JPanel();
        pThongTinNorth = new javax.swing.JPanel();
        pMaHD = new javax.swing.JPanel();
        lblMaHD = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        pTenKH = new javax.swing.JPanel();
        lblTenKH = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        pSDT = new javax.swing.JPanel();
        lblSDT = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        pThongTinCenter = new javax.swing.JPanel();
        pNgayMua = new javax.swing.JPanel();
        lblNgayMua = new javax.swing.JLabel();
        txtNgayMua = new javax.swing.JTextField();
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
        pTongHD = new javax.swing.JPanel();
        pKieuThanhToan = new javax.swing.JPanel();
        lblKieuThanhToan = new javax.swing.JLabel();
        txtKieuThanhToan = new javax.swing.JTextField();
        pCachDong = new javax.swing.JPanel();
        pTongHoaDon = new javax.swing.JPanel();
        lblTongHD = new javax.swing.JLabel();
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
        lblTilte1.setText("THÔNG TIN HÓA ĐƠN");
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

        pMaHD.setBackground(new java.awt.Color(255, 255, 255));
        pMaHD.setPreferredSize(new java.awt.Dimension(340, 40));
        pMaHD.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblMaHD.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblMaHD.setText("Mã hóa đơn:");
        lblMaHD.setPreferredSize(new java.awt.Dimension(120, 40));
        pMaHD.add(lblMaHD);

        txtMaHD.setEditable(false);
        txtMaHD.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtMaHD.setFocusable(false);
        txtMaHD.setPreferredSize(new java.awt.Dimension(200, 40));
        pMaHD.add(txtMaHD);

        pThongTinNorth.add(pMaHD);

        pTenKH.setBackground(new java.awt.Color(255, 255, 255));
        pTenKH.setPreferredSize(new java.awt.Dimension(440, 40));
        pTenKH.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTenKH.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTenKH.setText("Tên khách hàng:");
        lblTenKH.setPreferredSize(new java.awt.Dimension(120, 40));
        pTenKH.add(lblTenKH);

        txtTenKH.setEditable(false);
        txtTenKH.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtTenKH.setFocusable(false);
        txtTenKH.setPreferredSize(new java.awt.Dimension(300, 40));
        pTenKH.add(txtTenKH);

        pThongTinNorth.add(pTenKH);

        pSDT.setBackground(new java.awt.Color(255, 255, 255));
        pSDT.setPreferredSize(new java.awt.Dimension(340, 40));
        pSDT.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblSDT.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblSDT.setText("Số điện thoại:");
        lblSDT.setPreferredSize(new java.awt.Dimension(120, 40));
        pSDT.add(lblSDT);

        txtSDT.setEditable(false);
        txtSDT.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtSDT.setFocusable(false);
        txtSDT.setPreferredSize(new java.awt.Dimension(200, 40));
        pSDT.add(txtSDT);

        pThongTinNorth.add(pSDT);

        pThongTinHD.add(pThongTinNorth, java.awt.BorderLayout.NORTH);

        pThongTinCenter.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinCenter.setPreferredSize(new java.awt.Dimension(1200, 60));
        pThongTinCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        pNgayMua.setBackground(new java.awt.Color(255, 255, 255));
        pNgayMua.setPreferredSize(new java.awt.Dimension(330, 40));
        pNgayMua.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblNgayMua.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblNgayMua.setText("Ngày mua:");
        lblNgayMua.setPreferredSize(new java.awt.Dimension(80, 40));
        pNgayMua.add(lblNgayMua);

        txtNgayMua.setEditable(false);
        txtNgayMua.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtNgayMua.setFocusable(false);
        txtNgayMua.setPreferredSize(new java.awt.Dimension(200, 40));
        pNgayMua.add(txtNgayMua);

        pThongTinCenter.add(pNgayMua);

        pTenNV.setBackground(new java.awt.Color(255, 255, 255));
        pTenNV.setPreferredSize(new java.awt.Dimension(440, 40));
        pTenNV.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTenNV.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTenNV.setText("Tên nhân viên:");
        lblTenNV.setPreferredSize(new java.awt.Dimension(110, 40));
        pTenNV.add(lblTenNV);

        txtTenNV.setEditable(false);
        txtTenNV.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtTenNV.setFocusable(false);
        txtTenNV.setPreferredSize(new java.awt.Dimension(300, 40));
        pTenNV.add(txtTenNV);

        pThongTinCenter.add(pTenNV);

        pThongTinHD.add(pThongTinCenter, java.awt.BorderLayout.PAGE_END);

        pNorth.add(pThongTinHD, java.awt.BorderLayout.CENTER);

        getContentPane().add(pNorth, java.awt.BorderLayout.NORTH);

        pCenter.setBackground(new java.awt.Color(255, 255, 255));
        pCenter.setPreferredSize(new java.awt.Dimension(1200, 400));
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
        lblTitle2.setText("Chi tiết hóa đơn");
        pTitleCenter.add(lblTitle2, java.awt.BorderLayout.CENTER);

        tableSP.add(pTitleCenter, java.awt.BorderLayout.NORTH);

        spTableSP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(240, 240, 240), 1, true));
        spTableSP.setPreferredSize(new java.awt.Dimension(452, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"
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
        spTableSP.setViewportView(table);

        tableSP.add(spTableSP, java.awt.BorderLayout.CENTER);

        pTongHD.setBackground(new java.awt.Color(255, 255, 255));
        pTongHD.setPreferredSize(new java.awt.Dimension(800, 60));
        pTongHD.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        pKieuThanhToan.setBackground(new java.awt.Color(255, 255, 255));
        pKieuThanhToan.setPreferredSize(new java.awt.Dimension(400, 40));
        pKieuThanhToan.setLayout(new java.awt.BorderLayout());

        lblKieuThanhToan.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblKieuThanhToan.setText("Kiểu thanh toán:");
        lblKieuThanhToan.setPreferredSize(new java.awt.Dimension(120, 40));
        pKieuThanhToan.add(lblKieuThanhToan, java.awt.BorderLayout.WEST);

        txtKieuThanhToan.setEditable(false);
        txtKieuThanhToan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtKieuThanhToan.setEnabled(false);
        txtKieuThanhToan.setFocusable(false);
        txtKieuThanhToan.setRequestFocusEnabled(false);
        txtKieuThanhToan.setVerifyInputWhenFocusTarget(false);
        txtKieuThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKieuThanhToanActionPerformed(evt);
            }
        });
        pKieuThanhToan.add(txtKieuThanhToan, java.awt.BorderLayout.CENTER);

        pCachDong.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pCachDongLayout = new javax.swing.GroupLayout(pCachDong);
        pCachDong.setLayout(pCachDongLayout);
        pCachDongLayout.setHorizontalGroup(
            pCachDongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        pCachDongLayout.setVerticalGroup(
            pCachDongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        pKieuThanhToan.add(pCachDong, java.awt.BorderLayout.LINE_END);

        pTongHD.add(pKieuThanhToan);

        pTongHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        pTongHoaDon.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTongHD.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblTongHD.setForeground(new java.awt.Color(255, 51, 0));
        lblTongHD.setText("Tổng hóa đơn:");
        lblTongHD.setPreferredSize(new java.awt.Dimension(120, 40));
        pTongHoaDon.add(lblTongHD);

        txtTong.setEditable(false);
        txtTong.setFont(new java.awt.Font("Roboto Mono Medium", 0, 14)); // NOI18N
        txtTong.setForeground(new java.awt.Color(255, 51, 0));
        txtTong.setFocusable(false);
        txtTong.setPreferredSize(new java.awt.Dimension(200, 40));
        pTongHoaDon.add(txtTong);

        pTongHD.add(pTongHoaDon);

        tableSP.add(pTongHD, java.awt.BorderLayout.PAGE_END);

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
        btnPrint.setText("IN HÓA ĐƠN");
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

    private void txtKieuThanhToanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtKieuThanhToanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtKieuThanhToanActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }// GEN-LAST:event_btnHuyActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        try {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không có mã hóa đơn!", "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Khởi tạo DAO 
            DAO.HoaDon.HoaDonDAO hdDao = new DAO.HoaDon.HoaDonDAO();
            DAO.HoaDon.CTHoaDonDAO ctDao = new DAO.HoaDon.CTHoaDonDAO();

            // Lấy thông tin hóa đơn đầy đủ 
            HoaDon hd = hdDao.thongTinIn(maHD).orElse(null);
            if (hd == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!", "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy danh sách chi tiết hóa đơn để in
            List<CTHoaDon> listCTHD = ctDao.thongTinChiTietIn(maHD);
            if (listCTHD == null || listCTHD.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không có sản phẩm trong hóa đơn!", "Thông báo",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Gọi in PDF 
            InPDF pdf = new InPDF();
            pdf.printHoaDon(hd, listCTHD);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn!\n" + e.getMessage(),
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        // try {
        // for (javax.swing.UIManager.LookAndFeelInfo info :
        // javax.swing.UIManager.getInstalledLookAndFeels()) {
        // if ("Nimbus".equals(info.getName())) {
        // javax.swing.UIManager.setLookAndFeel(info.getClassName());
        // break;
        // }
        // }
        // } catch (ClassNotFoundException ex) {
        // java.util.logging.Logger.getLogger(formThongTinHD.class.getName()).log(java.util.logging.Level.SEVERE,
        // null,
        // ex);
        // } catch (InstantiationException ex) {
        // java.util.logging.Logger.getLogger(formThongTinHD.class.getName()).log(java.util.logging.Level.SEVERE,
        // null,
        // ex);
        // } catch (IllegalAccessException ex) {
        // java.util.logging.Logger.getLogger(formThongTinHD.class.getName()).log(java.util.logging.Level.SEVERE,
        // null,
        // ex);
        // } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        // java.util.logging.Logger.getLogger(formThongTinHD.class.getName()).log(java.util.logging.Level.SEVERE,
        // null,
        // ex);
        // }
        // // </editor-fold>

        // /* Create and display the dialog */
        // java.awt.EventQueue.invokeLater(new Runnable() {
        // public void run() {
        // formThongTinHD dialog = new formThongTinHD(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel lblKieuThanhToan;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblNgayMua;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblTilte1;
    private javax.swing.JLabel lblTitle2;
    private javax.swing.JLabel lblTongHD;
    private javax.swing.JPanel pAnh;
    private javax.swing.JPanel pCachDong;
    private javax.swing.JPanel pCenter;
    private javax.swing.JPanel pKieuThanhToan;
    private javax.swing.JPanel pMaHD;
    private javax.swing.JPanel pNgayMua;
    private javax.swing.JPanel pNorth;
    private javax.swing.JPanel pSDT;
    private javax.swing.JPanel pSouth;
    private javax.swing.JPanel pTenKH;
    private javax.swing.JPanel pTenNV;
    private javax.swing.JPanel pThongTinCenter;
    private javax.swing.JPanel pThongTinHD;
    private javax.swing.JPanel pThongTinNorth;
    private javax.swing.JPanel pTilte;
    private javax.swing.JPanel pTitleCenter;
    private javax.swing.JPanel pTongHD;
    private javax.swing.JPanel pTongHoaDon;
    private javax.swing.JScrollPane spTableSP;
    private javax.swing.JTable table;
    private javax.swing.JPanel tableSP;
    private javax.swing.JTextField txtKieuThanhToan;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtNgayMua;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTong;
    // End of variables declaration//GEN-END:variables
}

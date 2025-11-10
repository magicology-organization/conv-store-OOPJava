/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.form.PhieuNhap;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class formThemPN extends javax.swing.JPanel {
    private final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,### VND");
    private final java.time.format.DateTimeFormatter hsdFmt = java.time.format.DateTimeFormatter
            .ofPattern("dd/MM/yyyy");
    private String currentMaNV = null;

    private static final class NCCItem {
        final String maNCC;
        final String tenNCC;

        NCCItem(String maNCC, String tenNCC) {
            this.maNCC = maNCC;
            this.tenNCC = tenNCC;
        }

        @Override
        public String toString() {
            return tenNCC;
        }
    }

    /**
     * Creates new form formThemHD
     */
    public formThemPN() {
        initComponents();
        configureTables();
        initCombosAndDefaults();
        initEvents();
        // Tải danh sách sản phẩm lần đầu (nếu muốn)
        loadSanPham(null);
        try {
            DAO.PhieuNhap.PhieuNhapDAO dao = new DAO.PhieuNhap.PhieuNhapDAO();
            txtMaPN.setText(dao.taoMaPhieuNhap()); // ⚠️ txtMaHD đang dùng chung UI, giữ nguyên field nhưng giá trị là
                                                   // mã PN
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Không thể tạo mã phiếu nhập tự động!");
        }
    }

    // ====== UI setup ======
    private void configureTables() {
        // ====== Bảng Sản phẩm ======
        tableSP.setDefaultEditor(Object.class, null); // không cho sửa
        for (int i = 0; i < tableSP.getColumnCount(); i++) {
            javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            tableSP.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // ====== Bảng Chi tiết ======
        tableChiTiet.setDefaultEditor(Object.class, null); // không cho sửa
        for (int i = 0; i < tableChiTiet.getColumnCount(); i++) {
            javax.swing.table.DefaultTableCellRenderer renderer = new javax.swing.table.DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // ====== Chọn 1 dòng mỗi lần ======
        tableSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChiTiet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ====== Cho phép chọn dòng ======
        tableSP.setRowSelectionAllowed(true);
        tableSP.setColumnSelectionAllowed(false);
        tableChiTiet.setRowSelectionAllowed(true);
        tableChiTiet.setColumnSelectionAllowed(false);
        javax.swing.UIManager.put("Table.alwaysShowCellSelection", true);

    }

    private void initCombosAndDefaults() {
        // NẠP NCC
        loadNhaCungCap();
        // Thời gian hiện tại
        txtThoiGian.setText(dateFormat.format(new java.util.Date()));
        // Tổng ban đầu
        txtTong.setText(currencyFormat.format(0));
        loadDanhMuc();
    }

    private void loadSanPhamDanhMuc(String tenDM) {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableSP.getModel();
        m.setRowCount(0);

        try {
            DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();
            java.util.List<Object[]> list = spDao.findAllWithDetailsByDanhMuc(tenDM);

            int stt = 1;
            for (Object[] r : list) {
                String hsdStr = "";
                if (r[9] instanceof java.sql.Timestamp ts) {
                    hsdStr = ts.toLocalDateTime()
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }

                m.addRow(new Object[] {
                        stt++,
                        r[0], // Mã SP
                        r[1], // Tên SP
                        r[2], // Thành phần
                        r[6], // Giá nhập
                        r[7], // Giá bán
                        hsdStr, // HSD
                        r[5], // ĐVT
                        r[4], // Xuất xứ
                        r[8] // Tồn kho
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi lọc sản phẩm theo danh mục!");
        }
    }

    // ====== Event wiring ======
    private void initEvents() {
        // Enter trong ô mã NV -> tra nhân viên
        txtTenNV.addActionListener(e -> lookupNhanVienByMa());
        // Nút tìm NV
        btnSearchNV.addActionListener(e -> lookupNhanVienByMa());

        // Enter trong ô mã SP -> tra sản phẩm
        txtMaSP.addActionListener(e -> lookupProductByCode());

        // Nút "Tìm" trên khu vực SP -> filter bảng SP theo keyword
        btnTimKiem.addActionListener(e -> loadSanPham(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadSanPham(txtTimKiem.getText().trim()));

        // Nút "Thêm" ở khu vực số lượng -> thêm dòng chi tiết
        btnThem.setText("Thêm"); // đã set sẵn
        btnThem.addActionListener(e -> themCTPhieu());

        // Bảng Sản phẩm: click dòng nào → chọn dòng đó, clear bên Chi tiết
        tableSP.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int r = tableSP.rowAtPoint(e.getPoint());
                if (r >= 0) {
                    tableSP.setRowSelectionInterval(r, r);
                    tableSP.requestFocusInWindow();
                    tableChiTiet.clearSelection(); // luôn clear bảng còn lại
                    chonSanPham(); // nếu cần auto đổ dữ liệu ngay khi click
                }
            }
        });
        // Bảng Chi tiết: click 1 lần → chọn dòng, clear bảng SP
        tableChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int r = tableChiTiet.rowAtPoint(e.getPoint());
                if (r >= 0) {
                    tableChiTiet.setRowSelectionInterval(r, r);
                    tableSP.clearSelection();

                    // Ép bảng nhận focus để FlatLaf vẽ highlight
                    tableChiTiet.requestFocusInWindow();

                    // Gọi lại repaint sau khi Swing xử lý nội bộ
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        tableChiTiet.repaint();
                        tableChiTiet.getParent().repaint();
                    });
                }
            }
        });

        // Tạo phiếu
        btnTaoPhieu.setText("Lập phiếu");
        btnTaoPhieu.addActionListener(e -> taoPhieu());
        // Nút lọc theo danh mục
        cboDanhMuc.addActionListener(e -> {
            String selected = (String) cboDanhMuc.getSelectedItem();
            if (selected == null)
                return;

            if (selected.equalsIgnoreCase("Tất cả")) {
                loadSanPham(null);
            } else {
                loadSanPhamDanhMuc(selected);
            }
        });
    }

    // ====== Helpers ======
    private java.math.BigDecimal parseMoney(String s) {
        if (s == null)
            return java.math.BigDecimal.ZERO;
        s = s.replaceAll("[^0-9.]", "");
        if (s.isBlank())
            return java.math.BigDecimal.ZERO;
        try {
            return new java.math.BigDecimal(s);
        } catch (Exception ex) {
            return java.math.BigDecimal.ZERO;
        }
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    // ==================== Tra cứu nhân viên ====================
    private void lookupNhanVienByMa() {
        String maNV = txtTenNV.getText().trim();
        if (maNV.isEmpty()) {
            currentMaNV = null;
            return;
        }

        try {
            DAO.NhanVien.NhanVienDAO dao = new DAO.NhanVien.NhanVienDAO();
            java.util.Optional<Entity.NhanVien.NhanVien> onv = dao.findById(maNV);

            if (onv.isPresent()) {
                Entity.NhanVien.NhanVien nv = onv.get();
                txtTenNV.setText(nv.getTenNV());
                currentMaNV = nv.getMaNV(); // ✅ lưu mã ẩn
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên có mã: " + maNV);
                txtTenNV.setText("");
                currentMaNV = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi tra cứu nhân viên!");
            currentMaNV = null;
        }
    }

    private void lookupProductByCode() {
        String code = txtMaSP.getText().trim();
        if (code.isEmpty())
            return;
        try {
            DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();
            java.util.Optional<Entity.SanPham.SanPham> osp = spDao.findById(code);
            if (osp.isPresent()) {
                Entity.SanPham.SanPham sp = osp.get();
                txtTenSP.setText(sp.getTenSP());
                txtDonGia.setText(sp.getDonGia() == null ? "" : sp.getDonGia().toPlainString());
                txtSoLuong.requestFocus();
                txtSoLuong.selectAll();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tra cứu sản phẩm!");
        }
    }

    private void loadDanhMuc() {
        try {
            DAO.SanPham.DanhMucDAO dmDao = new DAO.SanPham.DanhMucDAO();
            java.util.List<Entity.SanPham.DanhMuc> list = dmDao.findAll();

            cboDanhMuc.removeAllItems();
            cboDanhMuc.addItem("Tất cả"); // ✅ đầu tiên là “Tất cả”

            for (Entity.SanPham.DanhMuc dm : list) {
                cboDanhMuc.addItem(dm.getTenDM());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải danh mục!");
        }
    }

    private void loadNhaCungCap() {
        try {
            // ĐÚNG package:
            DAO.PhieuNhap.NhaCungCapDAO dao = new DAO.PhieuNhap.NhaCungCapDAO();
            java.util.List<Entity.PhieuNhap.NhaCungCap> list = dao.findAll();

            javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel();
            for (var ncc : list) {
                model.addElement(new NCCItem(ncc.getMaNCC(), ncc.getTenNCC()));
            }
            cboNhaCungCap.setModel(model);

            if (cboNhaCungCap.getItemCount() > 0) {
                cboNhaCungCap.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải danh sách nhà cung cấp!");
        }
    }

    // lấy mã NCC đang chọn
    private String getSelectedMaNCC() {
        Object o = cboNhaCungCap.getSelectedItem();
        return (o instanceof NCCItem) ? ((NCCItem) o).maNCC : null;
    }

    private void loadSanPham(String keyword) {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableSP.getModel();
        m.setRowCount(0);

        try {
            DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();
            java.util.List<Object[]> list = (keyword == null || keyword.isBlank())
                    ? spDao.findAllWithDetails()
                    : spDao.findAllWithDetailsByName(keyword);

            int stt = 1;
            for (Object[] r : list) {
                // r[9] = Timestamp HSD (có thể null)
                String hsdStr = "";
                Object hsdObj = r[9];
                if (hsdObj instanceof java.sql.Timestamp) {
                    java.time.LocalDateTime ldt = ((java.sql.Timestamp) hsdObj).toLocalDateTime();
                    hsdStr = ldt.format(hsdFmt);
                }

                m.addRow(new Object[] {
                        stt++, // STT
                        r[0], // Mã SP
                        r[1], // Tên SP
                        r[2], // Thành phần/Mô tả
                        r[6], // Giá nhập
                        r[7], // Giá bán
                        hsdStr, // HSD <-- đã bổ sung
                        r[5], // ĐVT
                        r[4], // Xuất xứ
                        r[8] // Tồn kho
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải sản phẩm!");
        }
    }

    private void chonSanPham() {
        int r = tableSP.getSelectedRow();
        if (r < 0)
            return;

        String ma = String.valueOf(tableSP.getValueAt(r, 1)); // Mã SP
        String ten = String.valueOf(tableSP.getValueAt(r, 2)); // Tên SP
        String moTa = String.valueOf(tableSP.getValueAt(r, 3)); // Thành phần/Mô tả
        String giaNhap = String.valueOf(tableSP.getValueAt(r, 4)); // ⚠️ GIÁ NHẬP (cột 4)

        txtMaSP.setText(ma);
        txtTenSP.setText(ten);
        txtMoTa.setText(moTa);
        txtDonGia.setText(parseMoney(giaNhap).toPlainString());
        txtSoLuong.requestFocus();
        txtSoLuong.selectAll();
    }

    private void themCTPhieu() {
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        int soLuong = parseInt(txtSoLuong.getText());
        java.math.BigDecimal donGia = parseMoney(txtDonGia.getText());

        // Kiểm tra đầu vào
        if (maSP.isEmpty() || tenSP.isEmpty() || soLuong <= 0 || donGia.signum() <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã SP, Tên SP, Số lượng và Đơn giá hợp lệ!");
            return;
        }

        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        boolean daTonTai = false;

        // Duyệt toàn bộ bảng để xem SP đã có chưa
        for (int i = 0; i < m.getRowCount(); i++) {
            String maHienTai = String.valueOf(m.getValueAt(i, 1));
            if (maSP.equalsIgnoreCase(maHienTai)) {
                // Nếu sản phẩm trùng → cộng thêm số lượng
                int soLuongCu = parseInt(String.valueOf(m.getValueAt(i, 3)));
                int soLuongMoi = soLuongCu + soLuong;
                m.setValueAt(soLuongMoi, i, 3);

                // Tính lại thành tiền = đơn giá × số lượng mới
                java.math.BigDecimal thanhTien = donGia.multiply(java.math.BigDecimal.valueOf(soLuongMoi));
                m.setValueAt(currencyFormat.format(thanhTien), i, 5);

                daTonTai = true;
                break;
            }
        }

        // Nếu chưa có SP này trong bảng → thêm mới
        if (!daTonTai) {
            int stt = m.getRowCount() + 1;
            java.math.BigDecimal thanhTien = donGia.multiply(java.math.BigDecimal.valueOf(soLuong));
            m.addRow(new Object[] {
                    stt, // 0: STT
                    maSP, // 1: Mã SP
                    tenSP, // 2: Tên SP
                    soLuong, // 3: Số lượng
                    currencyFormat.format(donGia), // 4: Đơn giá
                    currencyFormat.format(thanhTien) // 5: Thành tiền
            });
        }

        // Cập nhật tổng tiền
        tinhTong();

        // Xóa input để nhập SP khác
        xoaRong();
    }

    private void xoaRong() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        txtMoTa.setText("");
        txtSoLuong.setText("1");
    }

    private void tinhTong() {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        java.math.BigDecimal tong = java.math.BigDecimal.ZERO;

        for (int i = 0; i < m.getRowCount(); i++) {
            int sl = parseInt(String.valueOf(m.getValueAt(i, 3))); // cột Số lượng
            java.math.BigDecimal donGia = parseMoney(String.valueOf(m.getValueAt(i, 4))); // cột Đơn giá

            // Thành tiền = đơn giá × số lượng
            java.math.BigDecimal line = donGia.multiply(java.math.BigDecimal.valueOf(sl));
            tong = tong.add(line);

            // Cập nhật lại cột "Thành tiền" realtime
            m.setValueAt(currencyFormat.format(line), i, 5);
        }

        // Cập nhật tổng tiền
        txtTong.setText(currencyFormat.format(tong));
    }

    // Lập phiếu nhập
    private void taoPhieu() {
        String maPN = txtMaPN.getText().trim(); // txtMaPN đang dùng như "Mã phiếu"
        if (maPN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có Mã phiếu nhập!");
            return;
        }
        if (tableChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm!");
            return;
        }

        // Kiểm tra nhân viên
        if (currentMaNV == null || currentMaNV.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Chưa xác định nhân viên hợp lệ! Hãy nhấn nút Tìm NV trước khi lập phiếu.");
            return;
        }
        String maNV = currentMaNV;

        // Lấy mã NCC từ combobox
        String maNCC = getSelectedMaNCC();
        if (maNCC == null || maNCC.isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
            return;
        }

        // Lạo entity & set dữ liệu phiếu nhập
        Entity.PhieuNhap.PhieuNhap pn = new Entity.PhieuNhap.PhieuNhap();
        pn.setMaPN(maPN);
        pn.setMaNV(maNV);
        pn.setMaNCC(maNCC);
        pn.setThoiGian(java.time.LocalDateTime.now());

        // Lưu phiếu nhập
        DAO.PhieuNhap.PhieuNhapDAO pnDao = new DAO.PhieuNhap.PhieuNhapDAO();
        if (!pnDao.insert(pn)) {
            JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thất bại (trùng mã?)");
            return;
        }

        // Lưu chi tiết phiếu nhập & TĂNG tồn kho
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        DAO.PhieuNhap.CTPhieuNhapDAO ctDao = new DAO.PhieuNhap.CTPhieuNhapDAO();
        DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();

        for (int i = 0; i < m.getRowCount(); i++) {
            String maSP = String.valueOf(m.getValueAt(i, 1));
            int soLuong = parseInt(String.valueOf(m.getValueAt(i, 3)));
            java.math.BigDecimal donGia = parseMoney(String.valueOf(m.getValueAt(i, 4)));

            Entity.PhieuNhap.CTPhieuNhap ct = new Entity.PhieuNhap.CTPhieuNhap();
            ct.setMaPN(maPN);
            ct.setMaSP(maSP);
            ct.setSoLuong(soLuong);
            ct.setDonGia(donGia);

            if (!ctDao.insert(ct)) {
                JOptionPane.showMessageDialog(this, "Lưu chi tiết thất bại tại SP: " + maSP);
                return;
            }

            // TĂNG tồn kho
            boolean ok = spDao.capNhatTonKhoSauNhap(maSP, soLuong); // đảm bảo có hàm này
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Không thể cập nhật tồn kho (tăng) cho sản phẩm: " + maSP);
            }
        }

        JOptionPane.showMessageDialog(this, "Đã lập phiếu nhập thành công!");

        // Quay về danh sách phiếu nhập
        try {
            GUI.Main mainFrame = (GUI.Main) javax.swing.SwingUtilities.getWindowAncestor(this);
            GUI.frame.PhieuNhap.frmPhieuNhap frm = new GUI.frame.PhieuNhap.frmPhieuNhap();
            mainFrame.replaceMainPanel(frm);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể quay lại danh sách phiếu nhập!");
        }
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

        Panel = new javax.swing.JPanel();
        pCenter = new javax.swing.JPanel();
        pCenterTop = new javax.swing.JPanel();
        pTilte = new javax.swing.JPanel();
        lblTilteThongTin = new javax.swing.JLabel();
        pThongTin = new javax.swing.JPanel();
        pAnh = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        pChiTietSP = new javax.swing.JPanel();
        pMaSP = new javax.swing.JPanel();
        lblMaSP = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        pTenSP = new javax.swing.JPanel();
        lblTenSP = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        pThanhPhan = new javax.swing.JPanel();
        lblMoTa = new javax.swing.JLabel();
        jspThanhPhan = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        pDonGia = new javax.swing.JPanel();
        lblDonGia = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        pCenterDown = new javax.swing.JPanel();
        pTopTableSP = new javax.swing.JPanel();
        pTimKiem = new javax.swing.JPanel();
        cboDanhMuc = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        pThemSP = new javax.swing.JPanel();
        lblSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        pDownTableSP = new javax.swing.JPanel();
        jspTableSP = new javax.swing.JScrollPane();
        tableSP = new javax.swing.JTable();
        pCapNhatSP = new javax.swing.JPanel();
        btnThemSP = new javax.swing.JButton();
        pEast = new javax.swing.JPanel();
        pEastTop = new javax.swing.JPanel();
        pTopTableCT = new javax.swing.JPanel();
        pTilteChiTiet = new javax.swing.JPanel();
        lblTilteChiTiet = new javax.swing.JLabel();
        pDownTableCT = new javax.swing.JPanel();
        jspTableCT = new javax.swing.JScrollPane();
        tableChiTiet = new javax.swing.JTable();
        pBtnCTHD = new javax.swing.JPanel();
        btnXoa = new javax.swing.JButton();
        btnXoaHet = new javax.swing.JButton();
        pEastDown = new javax.swing.JPanel();
        pThongTinPhieu = new javax.swing.JPanel();
        pTilteThongTinPhieu = new javax.swing.JPanel();
        lblTilteThongTinPhieu = new javax.swing.JLabel();
        pThongTinHD = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pThongTinDien = new javax.swing.JPanel();
        pMa = new javax.swing.JPanel();
        lblMa = new javax.swing.JLabel();
        txtMaPN = new javax.swing.JTextField();
        pNhaCungCap = new javax.swing.JPanel();
        lblNhaCungCap = new javax.swing.JLabel();
        cboNhaCungCap = new javax.swing.JComboBox<>();
        btnAddNCC = new javax.swing.JButton();
        pThoiGian = new javax.swing.JPanel();
        lblThoiGian = new javax.swing.JLabel();
        txtThoiGian = new javax.swing.JTextField();
        pNhanVien = new javax.swing.JPanel();
        lblNhanVien = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        btnSearchNV = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        pTong = new javax.swing.JPanel();
        lblTong = new javax.swing.JLabel();
        txtTong = new javax.swing.JTextField();
        pButton = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();
        btnTaoPhieu = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(1200, 600));
        setPreferredSize(new java.awt.Dimension(1200, 600));
        setLayout(new java.awt.BorderLayout());

        Panel.setLayout(new java.awt.BorderLayout());

        pCenter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));
        pCenter.setMinimumSize(new java.awt.Dimension(600, 600));
        pCenter.setPreferredSize(new java.awt.Dimension(600, 600));
        pCenter.setLayout(new java.awt.BorderLayout());

        pCenterTop.setBackground(new java.awt.Color(255, 255, 255));
        pCenterTop.setLayout(new java.awt.BorderLayout());

        pTilte.setBackground(new java.awt.Color(204, 255, 204));
        pTilte.setLayout(new java.awt.BorderLayout());

        lblTilteThongTin.setBackground(new java.awt.Color(0, 0, 0));
        lblTilteThongTin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTilteThongTin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTilteThongTin.setText("Thông tin sản phẩm");
        lblTilteThongTin.setMaximumSize(new java.awt.Dimension(600, 50));
        lblTilteThongTin.setMinimumSize(new java.awt.Dimension(600, 50));
        lblTilteThongTin.setPreferredSize(new java.awt.Dimension(600, 50));
        pTilte.add(lblTilteThongTin, java.awt.BorderLayout.CENTER);

        pCenterTop.add(pTilte, java.awt.BorderLayout.NORTH);

        pThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pThongTin.setMinimumSize(new java.awt.Dimension(600, 250));
        pThongTin.setPreferredSize(new java.awt.Dimension(600, 250));
        pThongTin.setLayout(new java.awt.BorderLayout());

        pAnh.setBackground(new java.awt.Color(255, 255, 255));
        pAnh.setMinimumSize(new java.awt.Dimension(300, 300));
        pAnh.setPreferredSize(new java.awt.Dimension(300, 300));

        lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
        lblAnh.setMaximumSize(new java.awt.Dimension(300, 250));
        lblAnh.setMinimumSize(new java.awt.Dimension(300, 250));
        lblAnh.setPreferredSize(new java.awt.Dimension(300, 250));
        pAnh.add(lblAnh);

        pThongTin.add(pAnh, java.awt.BorderLayout.WEST);

        pChiTietSP.setBackground(new java.awt.Color(255, 255, 255));
        pChiTietSP.setMinimumSize(new java.awt.Dimension(300, 250));
        pChiTietSP.setPreferredSize(new java.awt.Dimension(300, 250));

        pMaSP.setBackground(new java.awt.Color(255, 255, 255));
        pMaSP.setEnabled(false);
        pMaSP.setMinimumSize(new java.awt.Dimension(300, 30));
        pMaSP.setPreferredSize(new java.awt.Dimension(300, 30));
        pMaSP.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblMaSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMaSP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaSP.setText("Mã sản phẩm:");
        lblMaSP.setMaximumSize(new java.awt.Dimension(100, 20));
        lblMaSP.setMinimumSize(new java.awt.Dimension(100, 20));
        lblMaSP.setPreferredSize(new java.awt.Dimension(100, 20));
        pMaSP.add(lblMaSP);

        txtMaSP.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMaSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtMaSP.setMinimumSize(new java.awt.Dimension(180, 25));
        txtMaSP.setName(""); // NOI18N
        txtMaSP.setPreferredSize(new java.awt.Dimension(180, 25));
        pMaSP.add(txtMaSP);

        pChiTietSP.add(pMaSP);

        pTenSP.setBackground(new java.awt.Color(255, 255, 255));
        pTenSP.setEnabled(false);
        pTenSP.setMinimumSize(new java.awt.Dimension(300, 30));
        pTenSP.setPreferredSize(new java.awt.Dimension(300, 30));
        pTenSP.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblTenSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTenSP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTenSP.setText("Tên sản phẩm:");
        lblTenSP.setMaximumSize(new java.awt.Dimension(100, 20));
        lblTenSP.setMinimumSize(new java.awt.Dimension(100, 20));
        lblTenSP.setPreferredSize(new java.awt.Dimension(100, 20));
        pTenSP.add(lblTenSP);

        txtTenSP.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTenSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtTenSP.setMinimumSize(new java.awt.Dimension(180, 25));
        txtTenSP.setName(""); // NOI18N
        txtTenSP.setPreferredSize(new java.awt.Dimension(180, 25));
        txtTenSP.setRequestFocusEnabled(false);
        pTenSP.add(txtTenSP);

        pChiTietSP.add(pTenSP);

        pThanhPhan.setBackground(new java.awt.Color(255, 255, 255));
        pThanhPhan.setMinimumSize(new java.awt.Dimension(300, 100));
        pThanhPhan.setPreferredSize(new java.awt.Dimension(300, 100));
        pThanhPhan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblMoTa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoTa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMoTa.setText("Mô tả:");
        lblMoTa.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblMoTa.setMaximumSize(new java.awt.Dimension(100, 20));
        lblMoTa.setMinimumSize(new java.awt.Dimension(100, 20));
        lblMoTa.setName(""); // NOI18N
        lblMoTa.setPreferredSize(new java.awt.Dimension(100, 20));
        pThanhPhan.add(lblMoTa);

        jspThanhPhan.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspThanhPhan.setMinimumSize(new java.awt.Dimension(180, 80));
        jspThanhPhan.setPreferredSize(new java.awt.Dimension(180, 80));

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        txtMoTa.setMinimumSize(new java.awt.Dimension(180, 80));
        txtMoTa.setPreferredSize(new java.awt.Dimension(180, 80));
        jspThanhPhan.setViewportView(txtMoTa);

        pThanhPhan.add(jspThanhPhan);

        pChiTietSP.add(pThanhPhan);

        pDonGia.setBackground(new java.awt.Color(255, 255, 255));
        pDonGia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pDonGia.setEnabled(false);
        pDonGia.setMinimumSize(new java.awt.Dimension(300, 90));
        pDonGia.setPreferredSize(new java.awt.Dimension(300, 90));
        pDonGia.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));

        lblDonGia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDonGia.setForeground(new java.awt.Color(255, 51, 51));
        lblDonGia.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDonGia.setText("Đơn giá:");
        lblDonGia.setMaximumSize(new java.awt.Dimension(100, 30));
        lblDonGia.setMinimumSize(new java.awt.Dimension(100, 30));
        lblDonGia.setPreferredSize(new java.awt.Dimension(100, 30));
        pDonGia.add(lblDonGia);

        txtDonGia.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDonGia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtDonGia.setMinimumSize(new java.awt.Dimension(110, 40));
        txtDonGia.setName(""); // NOI18N
        txtDonGia.setPreferredSize(new java.awt.Dimension(110, 40));
        pDonGia.add(txtDonGia);

        pChiTietSP.add(pDonGia);

        pThongTin.add(pChiTietSP, java.awt.BorderLayout.CENTER);

        pCenterTop.add(pThongTin, java.awt.BorderLayout.CENTER);

        pCenter.add(pCenterTop, java.awt.BorderLayout.NORTH);

        pCenterDown.setBackground(new java.awt.Color(204, 204, 255));
        pCenterDown.setPreferredSize(new java.awt.Dimension(600, 300));
        pCenterDown.setLayout(new java.awt.BorderLayout());

        pTopTableSP.setBackground(new java.awt.Color(255, 255, 255));
        pTopTableSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));
        pTopTableSP.setMinimumSize(new java.awt.Dimension(600, 50));
        pTopTableSP.setName(""); // NOI18N
        pTopTableSP.setPreferredSize(new java.awt.Dimension(600, 50));
        pTopTableSP.setLayout(new java.awt.BorderLayout());

        pTimKiem.setMinimumSize(new java.awt.Dimension(350, 50));
        pTimKiem.setPreferredSize(new java.awt.Dimension(330, 50));

        cboDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboDanhMuc.setMinimumSize(new java.awt.Dimension(80, 30));
        cboDanhMuc.setPreferredSize(new java.awt.Dimension(80, 30));
        pTimKiem.add(cboDanhMuc);

        txtTimKiem.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTimKiem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtTimKiem.setMinimumSize(new java.awt.Dimension(150, 30));
        txtTimKiem.setName(""); // NOI18N
        txtTimKiem.setPreferredSize(new java.awt.Dimension(150, 30));
        pTimKiem.add(txtTimKiem);

        btnTimKiem.setBackground(new java.awt.Color(0, 204, 51));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm");
        btnTimKiem.setMaximumSize(new java.awt.Dimension(80, 30));
        btnTimKiem.setMinimumSize(new java.awt.Dimension(80, 30));
        btnTimKiem.setPreferredSize(new java.awt.Dimension(80, 30));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });
        pTimKiem.add(btnTimKiem);

        pTopTableSP.add(pTimKiem, java.awt.BorderLayout.WEST);

        pThemSP.setMinimumSize(new java.awt.Dimension(270, 50));
        pThemSP.setPreferredSize(new java.awt.Dimension(270, 50));

        lblSoLuong.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblSoLuong.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSoLuong.setText("Số lượng:");
        lblSoLuong.setMaximumSize(new java.awt.Dimension(60, 20));
        lblSoLuong.setMinimumSize(new java.awt.Dimension(60, 20));
        lblSoLuong.setName(""); // NOI18N
        lblSoLuong.setPreferredSize(new java.awt.Dimension(60, 20));
        pThemSP.add(lblSoLuong);

        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSoLuong.setText("1");
        txtSoLuong.setMinimumSize(new java.awt.Dimension(50, 30));
        txtSoLuong.setPreferredSize(new java.awt.Dimension(50, 30));
        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });
        pThemSP.add(txtSoLuong);

        btnThem.setBackground(new java.awt.Color(0, 204, 51));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.setMaximumSize(new java.awt.Dimension(100, 30));
        btnThem.setMinimumSize(new java.awt.Dimension(100, 30));
        btnThem.setPreferredSize(new java.awt.Dimension(100, 30));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pThemSP.add(btnThem);

        pTopTableSP.add(pThemSP, java.awt.BorderLayout.CENTER);

        pCenterDown.add(pTopTableSP, java.awt.BorderLayout.NORTH);

        pDownTableSP.setBackground(new java.awt.Color(255, 255, 255));
        pDownTableSP.setMinimumSize(new java.awt.Dimension(600, 250));
        pDownTableSP.setPreferredSize(new java.awt.Dimension(600, 250));
        pDownTableSP.setLayout(new java.awt.BorderLayout());

        jspTableSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspTableSP.setMinimumSize(null);
        jspTableSP.setPreferredSize(new java.awt.Dimension(600, 200));

        tableSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Mô tả", "Giá nhập", "Giá bán", "HSD", "ĐVT", "Xuất xứ", "Tồn kho"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSP.setMinimumSize(null);
        tableSP.setPreferredSize(null);
        jspTableSP.setViewportView(tableSP);

        pDownTableSP.add(jspTableSP, java.awt.BorderLayout.CENTER);

        pCapNhatSP.setMinimumSize(new java.awt.Dimension(600, 50));
        pCapNhatSP.setPreferredSize(new java.awt.Dimension(600, 50));

        btnThemSP.setBackground(new java.awt.Color(15, 204, 102));
        btnThemSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP.setText("Thêm sản phẩm mới");
        btnThemSP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP.setMaximumSize(new java.awt.Dimension(85, 35));
        btnThemSP.setMinimumSize(new java.awt.Dimension(85, 35));
        btnThemSP.setPreferredSize(new java.awt.Dimension(200, 35));
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });
        pCapNhatSP.add(btnThemSP);

        pDownTableSP.add(pCapNhatSP, java.awt.BorderLayout.PAGE_END);

        pCenterDown.add(pDownTableSP, java.awt.BorderLayout.CENTER);

        pCenter.add(pCenterDown, java.awt.BorderLayout.CENTER);

        Panel.add(pCenter, java.awt.BorderLayout.CENTER);

        pEast.setBackground(new java.awt.Color(255, 255, 255));
        pEast.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));
        pEast.setMinimumSize(new java.awt.Dimension(600, 600));
        pEast.setPreferredSize(new java.awt.Dimension(600, 600));
        pEast.setLayout(new java.awt.BorderLayout());

        pEastTop.setBackground(new java.awt.Color(255, 204, 204));
        pEastTop.setMinimumSize(new java.awt.Dimension(500, 250));
        pEastTop.setLayout(new java.awt.BorderLayout());

        pTopTableCT.setBackground(new java.awt.Color(255, 255, 255));
        pTopTableCT.setMinimumSize(new java.awt.Dimension(500, 50));
        pTopTableCT.setLayout(new java.awt.BorderLayout());

        pTilteChiTiet.setBackground(new java.awt.Color(204, 255, 204));
        pTilteChiTiet.setMinimumSize(new java.awt.Dimension(600, 50));
        pTilteChiTiet.setPreferredSize(new java.awt.Dimension(600, 50));
        pTilteChiTiet.setLayout(new java.awt.BorderLayout());

        lblTilteChiTiet.setBackground(new java.awt.Color(0, 0, 0));
        lblTilteChiTiet.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTilteChiTiet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTilteChiTiet.setText("Chi tiết phiếu nhập");
        lblTilteChiTiet.setMaximumSize(new java.awt.Dimension(600, 30));
        lblTilteChiTiet.setMinimumSize(new java.awt.Dimension(600, 30));
        lblTilteChiTiet.setPreferredSize(new java.awt.Dimension(600, 30));
        pTilteChiTiet.add(lblTilteChiTiet, java.awt.BorderLayout.CENTER);

        pTopTableCT.add(pTilteChiTiet, java.awt.BorderLayout.NORTH);

        pEastTop.add(pTopTableCT, java.awt.BorderLayout.PAGE_START);

        pDownTableCT.setBackground(new java.awt.Color(255, 255, 255));
        pDownTableCT.setLayout(new java.awt.BorderLayout());

        jspTableCT.setMinimumSize(new java.awt.Dimension(600, 150));
        jspTableCT.setPreferredSize(new java.awt.Dimension(600, 150));

        tableChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Số lượng", "Giá nhập", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableChiTiet.setMinimumSize(new java.awt.Dimension(600, 150));
        tableChiTiet.setPreferredSize(new java.awt.Dimension(600, 150));
        jspTableCT.setViewportView(tableChiTiet);

        pDownTableCT.add(jspTableCT, java.awt.BorderLayout.CENTER);

        btnXoa.setBackground(new java.awt.Color(255, 0, 0));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.setMaximumSize(new java.awt.Dimension(80, 30));
        btnXoa.setMinimumSize(new java.awt.Dimension(80, 30));
        btnXoa.setPreferredSize(new java.awt.Dimension(80, 30));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pBtnCTHD.add(btnXoa);

        btnXoaHet.setBackground(new java.awt.Color(153, 153, 153));
        btnXoaHet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoaHet.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaHet.setText("Xóa hết");
        btnXoaHet.setMaximumSize(new java.awt.Dimension(80, 30));
        btnXoaHet.setMinimumSize(new java.awt.Dimension(80, 30));
        btnXoaHet.setPreferredSize(new java.awt.Dimension(100, 30));
        btnXoaHet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHetActionPerformed(evt);
            }
        });
        pBtnCTHD.add(btnXoaHet);

        pDownTableCT.add(pBtnCTHD, java.awt.BorderLayout.SOUTH);

        pEastTop.add(pDownTableCT, java.awt.BorderLayout.CENTER);

        pEast.add(pEastTop, java.awt.BorderLayout.NORTH);

        pEastDown.setLayout(new java.awt.BorderLayout());

        pThongTinPhieu.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinPhieu.setMinimumSize(new java.awt.Dimension(600, 350));
        pThongTinPhieu.setPreferredSize(new java.awt.Dimension(600, 350));
        pThongTinPhieu.setLayout(new java.awt.BorderLayout());

        pTilteThongTinPhieu.setBackground(new java.awt.Color(204, 255, 204));
        pTilteThongTinPhieu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pTilteThongTinPhieu.setForeground(new java.awt.Color(255, 255, 255));
        pTilteThongTinPhieu.setMinimumSize(new java.awt.Dimension(600, 50));
        pTilteThongTinPhieu.setPreferredSize(new java.awt.Dimension(600, 50));
        pTilteThongTinPhieu.setLayout(new java.awt.BorderLayout());

        lblTilteThongTinPhieu.setBackground(new java.awt.Color(255, 255, 255));
        lblTilteThongTinPhieu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTilteThongTinPhieu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTilteThongTinPhieu.setText("Thông tin phiếu nhập");
        lblTilteThongTinPhieu.setMaximumSize(new java.awt.Dimension(600, 50));
        lblTilteThongTinPhieu.setMinimumSize(new java.awt.Dimension(600, 50));
        lblTilteThongTinPhieu.setPreferredSize(new java.awt.Dimension(600, 50));
        pTilteThongTinPhieu.add(lblTilteThongTinPhieu, java.awt.BorderLayout.CENTER);

        pThongTinPhieu.add(pTilteThongTinPhieu, java.awt.BorderLayout.NORTH);

        pThongTinHD.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinHD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));
        pThongTinHD.setMinimumSize(new java.awt.Dimension(600, 220));
        pThongTinHD.setPreferredSize(new java.awt.Dimension(600, 220));
        pThongTinHD.setLayout(new java.awt.BorderLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(600, 220));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 220));
        jPanel2.setLayout(new java.awt.BorderLayout());

        pThongTinDien.setBackground(new java.awt.Color(255, 255, 255));
        pThongTinDien.setMinimumSize(new java.awt.Dimension(600, 220));
        pThongTinDien.setPreferredSize(new java.awt.Dimension(600, 220));

        pMa.setBackground(new java.awt.Color(255, 255, 255));
        pMa.setMinimumSize(new java.awt.Dimension(500, 30));
        pMa.setPreferredSize(new java.awt.Dimension(500, 30));
        pMa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblMa.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblMa.setText("Mã phiếu nhập:");
        lblMa.setMaximumSize(new java.awt.Dimension(120, 30));
        lblMa.setMinimumSize(new java.awt.Dimension(120, 30));
        lblMa.setPreferredSize(new java.awt.Dimension(120, 30));
        pMa.add(lblMa);

        txtMaPN.setEditable(false);
        txtMaPN.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtMaPN.setFocusable(false);
        txtMaPN.setMinimumSize(new java.awt.Dimension(200, 25));
        txtMaPN.setPreferredSize(new java.awt.Dimension(200, 30));
        txtMaPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPNActionPerformed(evt);
            }
        });
        pMa.add(txtMaPN);

        pThongTinDien.add(pMa);

        pNhaCungCap.setBackground(new java.awt.Color(255, 255, 255));
        pNhaCungCap.setMinimumSize(new java.awt.Dimension(500, 45));
        pNhaCungCap.setPreferredSize(new java.awt.Dimension(500, 45));
        pNhaCungCap.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblNhaCungCap.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblNhaCungCap.setText("Nhà cung cấp:");
        lblNhaCungCap.setMaximumSize(new java.awt.Dimension(120, 30));
        lblNhaCungCap.setMinimumSize(new java.awt.Dimension(120, 30));
        lblNhaCungCap.setPreferredSize(new java.awt.Dimension(120, 30));
        pNhaCungCap.add(lblNhaCungCap);

        cboNhaCungCap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNhaCungCap.setPreferredSize(new java.awt.Dimension(200, 30));
        pNhaCungCap.add(cboNhaCungCap);

        btnAddNCC.setIcon(new FlatSVGIcon("./Icon/add-customer.svg"));
        btnAddNCC.setBorderPainted(false);
        btnAddNCC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddNCC.setFocusPainted(false);
        btnAddNCC.setFocusable(false);
        btnAddNCC.setMaximumSize(new java.awt.Dimension(40, 40));
        btnAddNCC.setMinimumSize(new java.awt.Dimension(40, 40));
        btnAddNCC.setPreferredSize(new java.awt.Dimension(40, 40));
        btnAddNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNCCActionPerformed(evt);
            }
        });
        pNhaCungCap.add(btnAddNCC);

        pThongTinDien.add(pNhaCungCap);

        pThoiGian.setBackground(new java.awt.Color(255, 255, 255));
        pThoiGian.setMinimumSize(new java.awt.Dimension(500, 30));
        pThoiGian.setPreferredSize(new java.awt.Dimension(500, 30));
        pThoiGian.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblThoiGian.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblThoiGian.setText("Thời gian:");
        lblThoiGian.setMaximumSize(new java.awt.Dimension(120, 30));
        lblThoiGian.setMinimumSize(new java.awt.Dimension(120, 30));
        lblThoiGian.setPreferredSize(new java.awt.Dimension(120, 30));
        pThoiGian.add(lblThoiGian);

        txtThoiGian.setEditable(false);
        txtThoiGian.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtThoiGian.setFocusable(false);
        txtThoiGian.setMinimumSize(new java.awt.Dimension(200, 25));
        txtThoiGian.setPreferredSize(new java.awt.Dimension(200, 30));
        pThoiGian.add(txtThoiGian);

        pThongTinDien.add(pThoiGian);

        pNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pNhanVien.setMinimumSize(new java.awt.Dimension(600, 45));
        pNhanVien.setPreferredSize(new java.awt.Dimension(500, 45));
        pNhanVien.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblNhanVien.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblNhanVien.setText("Nhân viên:");
        lblNhanVien.setMaximumSize(new java.awt.Dimension(120, 30));
        lblNhanVien.setMinimumSize(new java.awt.Dimension(120, 30));
        lblNhanVien.setPreferredSize(new java.awt.Dimension(120, 30));
        pNhanVien.add(lblNhanVien);

        txtTenNV.setMinimumSize(new java.awt.Dimension(200, 25));
        txtTenNV.setPreferredSize(new java.awt.Dimension(200, 30));
        pNhanVien.add(txtTenNV);

        btnSearchNV.setIcon(new FlatSVGIcon("./Icon/search.svg"));
        btnSearchNV.setBorderPainted(false);
        btnSearchNV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchNV.setFocusPainted(false);
        btnSearchNV.setFocusable(false);
        btnSearchNV.setMaximumSize(new java.awt.Dimension(40, 40));
        btnSearchNV.setMinimumSize(new java.awt.Dimension(40, 40));
        btnSearchNV.setPreferredSize(new java.awt.Dimension(40, 40));
        pNhanVien.add(btnSearchNV);

        pThongTinDien.add(pNhanVien);

        jSeparator4.setToolTipText("");
        jSeparator4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jSeparator4.setPreferredSize(new java.awt.Dimension(400, 10));
        pThongTinDien.add(jSeparator4);

        pTong.setBackground(new java.awt.Color(255, 255, 255));
        pTong.setMinimumSize(new java.awt.Dimension(500, 50));
        pTong.setPreferredSize(new java.awt.Dimension(500, 50));
        pTong.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        lblTong.setFont(new java.awt.Font("Roboto", 1, 17)); // NOI18N
        lblTong.setForeground(new java.awt.Color(255, 51, 0));
        lblTong.setText("Tổng phiếu:");
        lblTong.setMaximumSize(new java.awt.Dimension(131, 30));
        lblTong.setMinimumSize(new java.awt.Dimension(131, 30));
        lblTong.setPreferredSize(new java.awt.Dimension(131, 30));
        pTong.add(lblTong);

        txtTong.setEditable(false);
        txtTong.setFont(new java.awt.Font("Roboto Mono Medium", 0, 14)); // NOI18N
        txtTong.setForeground(new java.awt.Color(255, 51, 0));
        txtTong.setFocusable(false);
        txtTong.setMinimumSize(new java.awt.Dimension(200, 50));
        txtTong.setPreferredSize(new java.awt.Dimension(200, 50));
        pTong.add(txtTong);

        pThongTinDien.add(pTong);

        jPanel2.add(pThongTinDien, java.awt.BorderLayout.CENTER);

        pThongTinHD.add(jPanel2, java.awt.BorderLayout.LINE_END);

        pThongTinPhieu.add(pThongTinHD, java.awt.BorderLayout.CENTER);

        pButton.setBackground(new java.awt.Color(255, 255, 255));
        pButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pButton.setMinimumSize(new java.awt.Dimension(600, 50));
        pButton.setPreferredSize(new java.awt.Dimension(600, 50));

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
        pButton.add(btnHuy);

        btnTaoPhieu.setBackground(new java.awt.Color(0, 204, 51));
        btnTaoPhieu.setFont(new java.awt.Font("Roboto Mono Medium", 0, 16)); // NOI18N
        btnTaoPhieu.setForeground(new java.awt.Color(255, 255, 255));
        btnTaoPhieu.setText("TẠO PHIẾU");
        btnTaoPhieu.setBorderPainted(false);
        btnTaoPhieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTaoPhieu.setFocusPainted(false);
        btnTaoPhieu.setFocusable(false);
        btnTaoPhieu.setPreferredSize(new java.awt.Dimension(200, 40));
        pButton.add(btnTaoPhieu);

        pThongTinPhieu.add(pButton, java.awt.BorderLayout.SOUTH);

        pEastDown.add(pThongTinPhieu, java.awt.BorderLayout.CENTER);

        pEast.add(pEastDown, java.awt.BorderLayout.CENTER);

        Panel.add(pEast, java.awt.BorderLayout.EAST);

        add(Panel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNCCActionPerformed
        // TODO add your handling code here:
        // Lấy frame cha hiện tại
        javax.swing.JFrame parentFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        // Tạo dialog (formThemSP)
        GUI.form.NhaCungCap.formThemNCC dialog = new GUI.form.NhaCungCap.formThemNCC(parentFrame, true); // true = modal

        // Căn giữa form
        dialog.setLocationRelativeTo(this);

        // Hiển thị form thêm sản phẩm
        dialog.setVisible(true);
    }//GEN-LAST:event_btnAddNCCActionPerformed

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemSPActionPerformed
        // TODO add your handling code here:
        // Lấy frame cha hiện tại
        javax.swing.JFrame parentFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

        // Tạo dialog (formThemSP)
        GUI.form.SanPham.formThemSP dialog = new GUI.form.SanPham.formThemSP(parentFrame, true); // true = modal

        // Căn giữa form
        dialog.setLocationRelativeTo(this);

        // Hiển thị form thêm sản phẩm
        dialog.setVisible(true);

        // Sau khi đóng formThemSP → làm mới lại bảng sản phẩm
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tableSP.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        loadSanPham(null); // Gọi lại hàm tải danh sách sản phẩm
    }// GEN-LAST:event_btnThemSPActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this, "Hủy và quay lại danh sách hóa đơn?", "Xác nhận",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION)
            return;

        try {
            GUI.Main parentFrame = (GUI.Main) javax.swing.SwingUtilities.getWindowAncestor(this);
            GUI.frame.HoaDon.frmHoaDon frm = new GUI.frame.HoaDon.frmHoaDon(); // ✅ QUAY VỀ frmHoaDon
            parentFrame.replaceMainPanel(frm);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                    this, "Không thể quay lại danh sách hóa đơn: " + ex.getMessage(),
                    "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnHuyActionPerformed

    private void txtMaPNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtMaPNActionPerformed
        // TODO add your handling code here:
        try {
            DAO.PhieuNhap.PhieuNhapDAO dao = new DAO.PhieuNhap.PhieuNhapDAO();
            String maMoi = dao.taoMaPhieuNhap();
            txtMaPN.setText(maMoi);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã phiếu nhập!");
        }
    }// GEN-LAST:event_txtMaPNActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnThemActionPerformed

    private void btnAddKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddKHActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnAddKHActionPerformed

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtSoLuongActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        int row = tableChiTiet.getSelectedRow();
        if (row < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa dòng đã chọn không?",
                "Xác nhận xóa",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION)
            return;

        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        m.removeRow(row);

        // Cập nhật lại STT cho các dòng còn lại
        for (int i = 0; i < m.getRowCount(); i++) {
            m.setValueAt(i + 1, i, 0);
        }

        // Cập nhật tổng tiền sau khi xóa
        tinhTong();

        // Xóa selection
        tableChiTiet.clearSelection();
    }// GEN-LAST:event_btnXoaActionPerformed

    private void btnXoaHetActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaHetActionPerformed
        // TODO add your handling code here:
        if (tableChiTiet.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Không có dữ liệu để xóa!");
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa TẤT CẢ dòng chi tiết không?",
                "Xác nhận xóa hết",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm != javax.swing.JOptionPane.YES_OPTION)
            return;

        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        m.setRowCount(0); // Xóa toàn bộ dòng

        // Reset tổng tiền
        txtTong.setText(currencyFormat.format(0));

        // Xóa selection
        tableChiTiet.clearSelection();
    }// GEN-LAST:event_btnXoaHetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel;
    private javax.swing.JButton btnAddNCC;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSearchNV;
    private javax.swing.JButton btnTaoPhieu;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaHet;
    private javax.swing.JComboBox<String> cboDanhMuc;
    private javax.swing.JComboBox<String> cboNhaCungCap;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JScrollPane jspTableCT;
    private javax.swing.JScrollPane jspTableSP;
    private javax.swing.JScrollPane jspThanhPhan;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblMaSP;
    private javax.swing.JLabel lblMoTa;
    private javax.swing.JLabel lblNhaCungCap;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblThoiGian;
    private javax.swing.JLabel lblTilteChiTiet;
    private javax.swing.JLabel lblTilteThongTin;
    private javax.swing.JLabel lblTilteThongTinPhieu;
    private javax.swing.JLabel lblTong;
    private javax.swing.JPanel pAnh;
    private javax.swing.JPanel pBtnCTHD;
    private javax.swing.JPanel pButton;
    private javax.swing.JPanel pCapNhatSP;
    private javax.swing.JPanel pCenter;
    private javax.swing.JPanel pCenterDown;
    private javax.swing.JPanel pCenterTop;
    private javax.swing.JPanel pChiTietSP;
    private javax.swing.JPanel pDonGia;
    private javax.swing.JPanel pDownTableCT;
    private javax.swing.JPanel pDownTableSP;
    private javax.swing.JPanel pEast;
    private javax.swing.JPanel pEastDown;
    private javax.swing.JPanel pEastTop;
    private javax.swing.JPanel pMa;
    private javax.swing.JPanel pMaSP;
    private javax.swing.JPanel pNhaCungCap;
    private javax.swing.JPanel pNhanVien;
    private javax.swing.JPanel pTenSP;
    private javax.swing.JPanel pThanhPhan;
    private javax.swing.JPanel pThemSP;
    private javax.swing.JPanel pThoiGian;
    private javax.swing.JPanel pThongTin;
    private javax.swing.JPanel pThongTinDien;
    private javax.swing.JPanel pThongTinHD;
    private javax.swing.JPanel pThongTinPhieu;
    private javax.swing.JPanel pTilte;
    private javax.swing.JPanel pTilteChiTiet;
    private javax.swing.JPanel pTilteThongTinPhieu;
    private javax.swing.JPanel pTimKiem;
    private javax.swing.JPanel pTong;
    private javax.swing.JPanel pTopTableCT;
    private javax.swing.JPanel pTopTableSP;
    private javax.swing.JTable tableChiTiet;
    private javax.swing.JTable tableSP;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaPN;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtThoiGian;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTong;
    // End of variables declaration//GEN-END:variables
}

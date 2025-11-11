/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.form.HoaDon;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class formThemHD extends javax.swing.JPanel {
    private final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,### VND");
    private final java.time.format.DateTimeFormatter hsdFmt = java.time.format.DateTimeFormatter
            .ofPattern("dd/MM/yyyy");
    private String currentMaNV = null;

    /**
     * Creates new form formThemHD
     */
    public formThemHD() {
        initComponents();
        configureTables();
        initCombosAndDefaults();
        initEvents();
        loadSanPham(null);
        try {
            DAO.HoaDon.HoaDonDAO dao = new DAO.HoaDon.HoaDonDAO();
            txtMaHD.setText(dao.taoMaHoaDon());
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Không thể tạo mã hóa đơn tự động!");
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

    }

    private void initCombosAndDefaults() {
        // Kiểu thanh toán
        cboKieuThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Tiền mặt", "Chuyển khoản" }));
        // Thời gian hiện tại
        txtThoiGian.setText(dateFormat.format(new java.util.Date()));
        // Tổng ban đầu
        txtTong.setText(currencyFormat.format(0));
        txtTienThua.setText(currencyFormat.format(0));
        loadDanhMuc();
        updatePaymentUI();
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

    private void initEvents() {
        // Enter trong SĐT -> tra khách hàng
        txtSdt.addActionListener(e -> timKH());
        // Enter trong ô mã NV -> tra nhân viên
        txtTenNV.addActionListener(e -> timNV());
        // Nút tìm NV
        btnSearchNV.addActionListener(e -> timNV());
        // Nút tìm KH
        btnSearchKH.addActionListener(e -> timKH());

        // Enter trong ô mã SP
        txtMaSP.addActionListener(e -> timSP());

        // Nút "Tìm" trên khu vực SP
        btnTimKiem.addActionListener(e -> loadSanPham(txtTimKiem.getText().trim()));
        txtTimKiem.addActionListener(e -> loadSanPham(txtTimKiem.getText().trim()));

        // Nút "Thêm" ở khu vực số lượng
        btnThem.setText("Thêm");
        btnThem.addActionListener(e -> themCTHoaDon());

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
                        chonSanPham();
                    });
                }
            }
        });

        // Tính tiền thừa khi nhập tiền khách đưa
        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                tinhThua();
            }
        });

        // Thanh toán
        btnThanhToan.addActionListener(e -> thanhToan());
        // Theo dõi thay đổi kiểu thanh toán
        cboKieuThanhToan.addActionListener(e -> updatePaymentUI());
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

    // Format dữ liệu
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

    // Tìm nhân viên bằng mã
    private void timNV() {
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
                currentMaNV = nv.getMaNV(); // lưu mã ẩn
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

    // Tìm khách hàng bằng số điện thoại
    private void timKH() {
        String sdt = txtSdt.getText().trim();
        if (sdt.isEmpty())
            return;
        try {
            DAO.KhachHang.KhachHangDAO dao = new DAO.KhachHang.KhachHangDAO();
            java.util.Optional<Entity.KhachHang.KhachHang> okh = dao.findByPhone(sdt);
            if (okh.isPresent()) {
                Entity.KhachHang.KhachHang kh = okh.get();
                txtTenKH.setText(kh.getTenKH());
                txtGioiTinh.setText(kh.getGioiTinh()); // Entity đã là String
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có SĐT: " + sdt);
                txtTenKH.setText("");
                txtGioiTinh.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tra cứu khách hàng!");
        }
    }

    // Tìm sản phẩm bằng mã
    private void timSP() {
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

    // Load danh mục vào combo box
    private void loadDanhMuc() {
        try {
            DAO.SanPham.DanhMucDAO dmDao = new DAO.SanPham.DanhMucDAO();
            java.util.List<Entity.SanPham.DanhMuc> list = dmDao.findAll();

            cboDanhMuc.removeAllItems();
            cboDanhMuc.addItem("Tất cả"); // đầu tiên là “Tất cả”

            for (Entity.SanPham.DanhMuc dm : list) {
                cboDanhMuc.addItem(dm.getTenDM());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải danh mục!");
        }
    }

    // Load sản phẩm vào bảng
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

    // Chọn sản phẩm từ bảng
    private void chonSanPham() {
        int rSP = tableSP.getSelectedRow();
        int rCT = tableChiTiet.getSelectedRow();

        // --- Nếu chọn ở bảng Sản phẩm ---
        if (rSP >= 0) {
            String ma = String.valueOf(tableSP.getValueAt(rSP, 1)); // Mã SP
            String ten = String.valueOf(tableSP.getValueAt(rSP, 2)); // Tên SP
            String moTa = String.valueOf(tableSP.getValueAt(rSP, 3)); // Mô tả
            String giaBan = String.valueOf(tableSP.getValueAt(rSP, 5)); // Giá bán

            txtMaSP.setText(ma);
            txtTenSP.setText(ten);
            txtMoTa.setText(moTa);
            txtDonGia.setText(parseMoney(giaBan).toPlainString());
            txtSoLuong.setText("1");
            txtSoLuong.requestFocus();
            txtSoLuong.selectAll();

            // --- Cập nhật ảnh ---
            try {
                DAO.SanPham.SanPhamDAO dao = new DAO.SanPham.SanPhamDAO();
                Entity.SanPham.SanPham sp = dao.findById(ma).orElse(null);
                if (sp != null && sp.getAnhSP() != null) {
                    byte[] anh = sp.getAnhSP();
                    ImageIcon icon = new ImageIcon(anh);
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    lblAnh.setIcon(new ImageIcon(img));
                } else {
                    lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
                }
            } catch (Exception ex) {
                lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
            }
        }

        // --- Nếu chọn ở bảng Chi tiết ---
        else if (rCT >= 0) {
            String ma = String.valueOf(tableChiTiet.getValueAt(rCT, 1)); // Mã SP
            String ten = String.valueOf(tableChiTiet.getValueAt(rCT, 2)); // Tên SP
            String soLuong = String.valueOf(tableChiTiet.getValueAt(rCT, 3)); // SL
            String donGia = String.valueOf(tableChiTiet.getValueAt(rCT, 4)); // Đơn giá

            txtMaSP.setText(ma);
            txtTenSP.setText(ten);
            txtSoLuong.setText(soLuong);
            txtDonGia.setText(parseMoney(donGia).toPlainString());

            // --- Lấy mô tả & ảnh từ DAO ---
            try {
                DAO.SanPham.SanPhamDAO dao = new DAO.SanPham.SanPhamDAO();
                Entity.SanPham.SanPham sp = dao.findById(ma).orElse(null);

                if (sp != null) {
                    txtMoTa.setText(sp.getMoTaSP() != null ? sp.getMoTaSP() : "");
                    if (sp.getAnhSP() != null) {
                        byte[] anh = sp.getAnhSP();
                        ImageIcon icon = new ImageIcon(anh);
                        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                        lblAnh.setIcon(new ImageIcon(img));
                    } else {
                        lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
                    }
                } else {
                    txtMoTa.setText("");
                    lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
                }
            } catch (Exception ex) {
                lblAnh.setIcon(new FlatSVGIcon("./icon/image.svg"));
            }
        }

        // --- Cập nhật lại panel ---
        pThongTin.revalidate();
        pThongTin.repaint();
    }

    // Thêm chi tiết hóa đơn
    private void themCTHoaDon() {
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

    // Xóa input sản phẩm
    private void xoaRong() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        txtMoTa.setText("");
        txtSoLuong.setText("1");
    }

    // Tính tổng tiền và tiền thừa
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

        // Tính tiền thừa
        tinhThua();
    }

    // Tính tiền thừa
    private void tinhThua() {
        // Nếu chuyển khoản → không có tiền thừa
        if (isChuyenKhoan()) {
            txtTienThua.setText(currencyFormat.format(0));
            return;
        }

        java.math.BigDecimal tong = parseMoney(txtTong.getText());
        java.math.BigDecimal dua = parseMoney(txtTienKhachDua.getText());
        java.math.BigDecimal thua = dua.subtract(tong);
        if (thua.signum() < 0)
            thua = java.math.BigDecimal.ZERO;

        txtTienThua.setText(currencyFormat.format(thua));
    }

    // Thanh toán
    private void thanhToan() {
        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có Mã hóa đơn!");
            return;
        }
        if (tableChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin đơn hàng!");
            return;
        }

        // Kiểm tra nhân viên
        if (currentMaNV == null || currentMaNV.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Chưa xác định nhân viên hợp lệ! Hãy nhấn nút Tìm NV trước khi thanh toán.");
            return;
        }
        String maNV = currentMaNV;

        // Lấy mã khách hàng từ SĐT
        String sdt = txtSdt.getText().trim();
        String maKH = null;
        try {
            DAO.KhachHang.KhachHangDAO khDao = new DAO.KhachHang.KhachHangDAO();
            java.util.Optional<Entity.KhachHang.KhachHang> okh = khDao.findByPhone(sdt);
            if (okh.isPresent())
                maKH = okh.get().getMaKH();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (maKH == null) {
            JOptionPane.showMessageDialog(this, "Không xác định được khách hàng từ SĐT!");
            return;
        }

        Entity.HoaDon.HoaDon hd = new Entity.HoaDon.HoaDon();
        hd.setMaHD(maHD);

        Entity.NhanVien.NhanVien nv = new Entity.NhanVien.NhanVien();
        nv.setMaNV(maNV);
        hd.setNhanVien(nv);

        Entity.KhachHang.KhachHang kh = new Entity.KhachHang.KhachHang();
        kh.setMaKH(maKH);
        hd.setKhachHang(kh);

        hd.setThoiGian(java.time.LocalDateTime.now());
        hd.setKieuThanhToan(String.valueOf(cboKieuThanhToan.getSelectedItem()));

        DAO.HoaDon.HoaDonDAO hdDao = new DAO.HoaDon.HoaDonDAO();
        if (!hdDao.insert(hd)) {
            JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại (trùng mã?)");
            return;
        }

        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tableChiTiet.getModel();
        DAO.HoaDon.CTHoaDonDAO ctDao = new DAO.HoaDon.CTHoaDonDAO();
        DAO.SanPham.SanPhamDAO spDao = new DAO.SanPham.SanPhamDAO();

        for (int i = 0; i < m.getRowCount(); i++) {
            String maSP = String.valueOf(m.getValueAt(i, 1));
            int soLuong = parseInt(String.valueOf(m.getValueAt(i, 3)));
            java.math.BigDecimal donGia = parseMoney(String.valueOf(m.getValueAt(i, 4)));

            Entity.HoaDon.CTHoaDon ct = new Entity.HoaDon.CTHoaDon();

            // Gán hóa đơn
            Entity.HoaDon.HoaDon hoaDonRef = new Entity.HoaDon.HoaDon();
            hoaDonRef.setMaHD(maHD);
            ct.setHoaDon(hoaDonRef);

            // Gán sản phẩm
            Entity.SanPham.SanPham sp = new Entity.SanPham.SanPham();
            sp.setMaSP(maSP);
            ct.setSanPham(sp);

            ct.setSoLuong(soLuong);
            ct.setDonGia(donGia);

            if (!ctDao.insert(ct)) {
                JOptionPane.showMessageDialog(this, "Lưu chi tiết thất bại tại SP: " + maSP);
                return;
            }

            // Giảm tồn kho
            boolean ok = spDao.capNhatTonKhoSauBan(maSP, soLuong);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Không thể cập nhật tồn kho cho sản phẩm: " + maSP +
                                " (có thể không đủ hàng!)");
            }
        }

        JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn thành công!");
        inHoaDonPDF(maHD);
        try {
            GUI.Main mainFrame = (GUI.Main) javax.swing.SwingUtilities.getWindowAncestor(this);
            GUI.frame.HoaDon.frmHoaDon frm = new GUI.frame.HoaDon.frmHoaDon();
            mainFrame.replaceMainPanel(frm);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể quay lại danh sách hóa đơn!");
        }
    }

    // Kiểm tra kiểu thanh toán hiện chọn
    private boolean isChuyenKhoan() {
        Object sel = cboKieuThanhToan.getSelectedItem();
        return sel != null && sel.toString().equalsIgnoreCase("Chuyển khoản");
    }

    // Bật/tắt các ô tiền theo kiểu thanh toán
    private void updatePaymentUI() {
        boolean ck = isChuyenKhoan();

        // "Tiền khách đưa" chỉ dùng khi thanh toán tiền mặt
        txtTienKhachDua.setEnabled(!ck);

        // "Tiền thừa" luôn chỉ đọc/hiển thị
        txtTienThua.setEnabled(false);

        if (ck) {
            // Chuyển khoản → reset về 0 để không tính thừa
            txtTienKhachDua.setText(currencyFormat.format(0));
            txtTienThua.setText(currencyFormat.format(0));
        } else {
            // Tiền mặt → tính lại thừa ngay nếu đã có tổng
            tinhThua();
        }
    }

    private void inHoaDonPDF(String maHD) {
        try {
            if (maHD == null || maHD.isBlank()) {
                JOptionPane.showMessageDialog(this, "Không có mã hóa đơn để in!");
                return;
            }

            // Khởi tạo DAO 
            DAO.HoaDon.HoaDonDAO hdDao = new DAO.HoaDon.HoaDonDAO();
            DAO.HoaDon.CTHoaDonDAO ctDao = new DAO.HoaDon.CTHoaDonDAO();

            // Lấy thông tin hóa đơn 
            Entity.HoaDon.HoaDon hd = hdDao.thongTinIn(maHD).orElse(null);
            if (hd == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn!");
                return;
            }

            // Lấy chi tiết hóa đơn
            java.util.List<Entity.HoaDon.CTHoaDon> listCTHD = ctDao.thongTinChiTietIn(maHD);
            if (listCTHD == null || listCTHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có sản phẩm trong hóa đơn!");
                return;
            }

            // In PDF 
            Utils.InPDF pdf = new Utils.InPDF();
            pdf.printHoaDon(hd, listCTHD);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn!\n" + e.getMessage());
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
    // Code">//GEN-BEGIN:initComponents
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
        pMoTa = new javax.swing.JPanel();
        lblMoTa = new javax.swing.JLabel();
        jspMoTa = new javax.swing.JScrollPane();
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
        txtMaHD = new javax.swing.JTextField();
        pSDT = new javax.swing.JPanel();
        lblSDT = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        btnSearchKH = new javax.swing.JButton();
        btnAddKH = new javax.swing.JButton();
        pTenKH = new javax.swing.JPanel();
        lblTenKH = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        txtGioiTinh = new javax.swing.JTextField();
        pThoiGian = new javax.swing.JPanel();
        lblThoiGian = new javax.swing.JLabel();
        txtThoiGian = new javax.swing.JTextField();
        pNhanVien = new javax.swing.JPanel();
        lblNhanVien = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        btnSearchNV = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        pKieuThanhToan = new javax.swing.JPanel();
        lblKieuThanhToan = new javax.swing.JLabel();
        cboKieuThanhToan = new javax.swing.JComboBox<>();
        pTong = new javax.swing.JPanel();
        lblTong = new javax.swing.JLabel();
        txtTong = new javax.swing.JTextField();
        pTienKH = new javax.swing.JPanel();
        lblTienKH = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        pTienThua = new javax.swing.JPanel();
        lblTienThua = new javax.swing.JLabel();
        txtTienThua = new javax.swing.JTextField();
        pButton = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();

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

        txtMaSP.setEditable(false);
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

        txtTenSP.setEditable(false);
        txtTenSP.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTenSP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtTenSP.setMinimumSize(new java.awt.Dimension(180, 25));
        txtTenSP.setName(""); // NOI18N
        txtTenSP.setPreferredSize(new java.awt.Dimension(180, 25));
        txtTenSP.setRequestFocusEnabled(false);
        pTenSP.add(txtTenSP);

        pChiTietSP.add(pTenSP);

        pMoTa.setBackground(new java.awt.Color(255, 255, 255));
        pMoTa.setMinimumSize(new java.awt.Dimension(300, 100));
        pMoTa.setPreferredSize(new java.awt.Dimension(300, 100));
        pMoTa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblMoTa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoTa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMoTa.setText("Mô tả:");
        lblMoTa.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblMoTa.setMaximumSize(new java.awt.Dimension(100, 20));
        lblMoTa.setMinimumSize(new java.awt.Dimension(100, 20));
        lblMoTa.setName(""); // NOI18N
        lblMoTa.setPreferredSize(new java.awt.Dimension(100, 20));
        pMoTa.add(lblMoTa);

        jspMoTa.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspMoTa.setMinimumSize(new java.awt.Dimension(180, 80));
        jspMoTa.setPreferredSize(new java.awt.Dimension(180, 80));

        txtMoTa.setEditable(false);
        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        txtMoTa.setMinimumSize(new java.awt.Dimension(180, 80));
        txtMoTa.setPreferredSize(new java.awt.Dimension(180, 80));
        jspMoTa.setViewportView(txtMoTa);

        pMoTa.add(jspMoTa);

        pChiTietSP.add(pMoTa);

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

        txtDonGia.setEditable(false);
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

        cboDanhMuc.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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
        pDownTableSP.setLayout(new java.awt.BorderLayout());

        jspTableSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspTableSP.setMinimumSize(null);

        tableSP.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "STT", "Mã SP", "Tên SP", "Mô tả", "Giá nhập", "Giá bán", "HSD", "ĐVT", "Xuất xứ", "Tồn kho"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jspTableSP.setViewportView(tableSP);

        pDownTableSP.add(jspTableSP, java.awt.BorderLayout.CENTER);

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
        lblTilteChiTiet.setText("Chi tiết hóa đơn");
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
                new Object[][] {

                },
                new String[] {
                        "STT", "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tableChiTiet.setMinimumSize(new java.awt.Dimension(600, 150));
        tableChiTiet.setPreferredSize(new java.awt.Dimension(600, 150));
        jspTableCT.setViewportView(tableChiTiet);
        tableChiTiet.getColumnModel().getSelectionModel()
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

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
        lblTilteThongTinPhieu.setText("Thông tin hóa đơn");
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
        lblMa.setText("Mã hóa đơn:");
        lblMa.setMaximumSize(new java.awt.Dimension(120, 30));
        lblMa.setMinimumSize(new java.awt.Dimension(120, 30));
        lblMa.setPreferredSize(new java.awt.Dimension(120, 30));
        pMa.add(lblMa);

        txtMaHD.setEditable(false);
        txtMaHD.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtMaHD.setFocusable(false);
        txtMaHD.setMinimumSize(new java.awt.Dimension(200, 25));
        txtMaHD.setPreferredSize(new java.awt.Dimension(200, 30));
        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });
        pMa.add(txtMaHD);

        pThongTinDien.add(pMa);

        pSDT.setBackground(new java.awt.Color(255, 255, 255));
        pSDT.setMinimumSize(new java.awt.Dimension(500, 45));
        pSDT.setPreferredSize(new java.awt.Dimension(500, 45));
        pSDT.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblSDT.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblSDT.setText("Số điện thoại:");
        lblSDT.setMaximumSize(new java.awt.Dimension(120, 30));
        lblSDT.setMinimumSize(new java.awt.Dimension(120, 30));
        lblSDT.setPreferredSize(new java.awt.Dimension(120, 30));
        pSDT.add(lblSDT);

        txtSdt.setMinimumSize(new java.awt.Dimension(200, 25));
        txtSdt.setPreferredSize(new java.awt.Dimension(200, 30));
        pSDT.add(txtSdt);

        btnSearchKH.setIcon(new FlatSVGIcon("./Icon/search.svg"));
        btnSearchKH.setBorderPainted(false);
        btnSearchKH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchKH.setFocusPainted(false);
        btnSearchKH.setFocusable(false);
        btnSearchKH.setMaximumSize(new java.awt.Dimension(40, 40));
        btnSearchKH.setMinimumSize(new java.awt.Dimension(40, 40));
        btnSearchKH.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSearchKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchKHActionPerformed(evt);
            }
        });
        pSDT.add(btnSearchKH);

        btnAddKH.setIcon(new FlatSVGIcon("./Icon/add-customer.svg"));
        btnAddKH.setBorderPainted(false);
        btnAddKH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddKH.setFocusPainted(false);
        btnAddKH.setFocusable(false);
        btnAddKH.setMaximumSize(new java.awt.Dimension(40, 40));
        btnAddKH.setMinimumSize(new java.awt.Dimension(40, 40));
        btnAddKH.setPreferredSize(new java.awt.Dimension(40, 40));
        btnAddKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddKHActionPerformed(evt);
            }
        });
        pSDT.add(btnAddKH);

        pThongTinDien.add(pSDT);

        pTenKH.setBackground(new java.awt.Color(255, 255, 255));
        pTenKH.setMinimumSize(new java.awt.Dimension(500, 30));
        pTenKH.setPreferredSize(new java.awt.Dimension(500, 30));
        pTenKH.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTenKH.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTenKH.setText("Tên khách hàng:");
        lblTenKH.setMaximumSize(new java.awt.Dimension(120, 30));
        lblTenKH.setMinimumSize(new java.awt.Dimension(120, 30));
        lblTenKH.setPreferredSize(new java.awt.Dimension(120, 30));
        pTenKH.add(lblTenKH);

        txtTenKH.setEditable(false);
        txtTenKH.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtTenKH.setFocusable(false);
        txtTenKH.setMinimumSize(new java.awt.Dimension(200, 25));
        txtTenKH.setPreferredSize(new java.awt.Dimension(200, 30));
        pTenKH.add(txtTenKH);

        txtGioiTinh.setEditable(false);
        txtGioiTinh.setFont(new java.awt.Font("Roboto Mono", 1, 14)); // NOI18N
        txtGioiTinh.setFocusable(false);
        pTenKH.add(txtGioiTinh);

        pThongTinDien.add(pTenKH);

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

        pKieuThanhToan.setBackground(new java.awt.Color(255, 255, 255));
        pKieuThanhToan.setMinimumSize(new java.awt.Dimension(500, 30));
        pKieuThanhToan.setPreferredSize(new java.awt.Dimension(500, 30));
        pKieuThanhToan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblKieuThanhToan.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblKieuThanhToan.setText("Kiểu thanh toán:");
        lblKieuThanhToan.setMaximumSize(new java.awt.Dimension(120, 30));
        lblKieuThanhToan.setMinimumSize(new java.awt.Dimension(120, 30));
        lblKieuThanhToan.setPreferredSize(new java.awt.Dimension(120, 30));
        pKieuThanhToan.add(lblKieuThanhToan);

        cboKieuThanhToan.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKieuThanhToan.setPreferredSize(new java.awt.Dimension(200, 30));
        pKieuThanhToan.add(cboKieuThanhToan);

        pThongTinDien.add(pKieuThanhToan);

        pTong.setBackground(new java.awt.Color(255, 255, 255));
        pTong.setMinimumSize(new java.awt.Dimension(500, 50));
        pTong.setPreferredSize(new java.awt.Dimension(500, 50));
        pTong.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        lblTong.setFont(new java.awt.Font("Roboto", 1, 17)); // NOI18N
        lblTong.setForeground(new java.awt.Color(255, 51, 0));
        lblTong.setText("Tổng hóa đơn:");
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

        pTienKH.setBackground(new java.awt.Color(255, 255, 255));
        pTienKH.setPreferredSize(new java.awt.Dimension(500, 30));
        pTienKH.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTienKH.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTienKH.setText("Tiền khách đưa:");
        lblTienKH.setMaximumSize(new java.awt.Dimension(120, 30));
        lblTienKH.setMinimumSize(new java.awt.Dimension(120, 30));
        lblTienKH.setPreferredSize(new java.awt.Dimension(120, 30));
        pTienKH.add(lblTienKH);

        txtTienKhachDua.setMinimumSize(new java.awt.Dimension(200, 30));
        txtTienKhachDua.setPreferredSize(new java.awt.Dimension(200, 30));
        pTienKH.add(txtTienKhachDua);

        pThongTinDien.add(pTienKH);

        pTienThua.setBackground(new java.awt.Color(255, 255, 255));
        pTienThua.setMinimumSize(new java.awt.Dimension(500, 30));
        pTienThua.setPreferredSize(new java.awt.Dimension(500, 30));
        pTienThua.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lblTienThua.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        lblTienThua.setText("Tiền thừa:");
        lblTienThua.setMaximumSize(new java.awt.Dimension(120, 30));
        lblTienThua.setMinimumSize(new java.awt.Dimension(120, 30));
        lblTienThua.setPreferredSize(new java.awt.Dimension(120, 30));
        pTienThua.add(lblTienThua);

        txtTienThua.setEditable(false);
        txtTienThua.setFont(new java.awt.Font("Roboto Mono Medium", 0, 14)); // NOI18N
        txtTienThua.setFocusable(false);
        txtTienThua.setMinimumSize(new java.awt.Dimension(200, 30));
        txtTienThua.setPreferredSize(new java.awt.Dimension(200, 30));
        txtTienThua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienThuaActionPerformed(evt);
            }
        });
        pTienThua.add(txtTienThua);

        pThongTinDien.add(pTienThua);

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

        btnThanhToan.setBackground(new java.awt.Color(0, 204, 51));
        btnThanhToan.setFont(new java.awt.Font("Roboto Mono Medium", 0, 16)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("THANH TOÁN");
        btnThanhToan.setBorderPainted(false);
        btnThanhToan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setFocusable(false);
        btnThanhToan.setPreferredSize(new java.awt.Dimension(200, 40));
        pButton.add(btnThanhToan);

        pThongTinPhieu.add(pButton, java.awt.BorderLayout.SOUTH);

        pEastDown.add(pThongTinPhieu, java.awt.BorderLayout.CENTER);

        pEast.add(pEastDown, java.awt.BorderLayout.CENTER);

        Panel.add(pEast, java.awt.BorderLayout.EAST);

        add(Panel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

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

    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
        try {
            DAO.HoaDon.HoaDonDAO dao = new DAO.HoaDon.HoaDonDAO();
            String maMoi = dao.taoMaHoaDon();
            txtMaHD.setText(maMoi);
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã hóa đơn!");
        }
    }// GEN-LAST:event_txtMaHDActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnThemActionPerformed

    private void btnSearchKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSearchKHActionPerformed
        // TODO add your handling code here:

    }// GEN-LAST:event_btnSearchKHActionPerformed

    private void btnAddKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddKHActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnAddKHActionPerformed

    private void txtTienThuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTienThuaActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTienThuaActionPerformed

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
    private javax.swing.JButton btnAddKH;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSearchKH;
    private javax.swing.JButton btnSearchNV;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaHet;
    private javax.swing.JComboBox<String> cboDanhMuc;
    private javax.swing.JComboBox<String> cboKieuThanhToan;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JScrollPane jspMoTa;
    private javax.swing.JScrollPane jspTableCT;
    private javax.swing.JScrollPane jspTableSP;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblKieuThanhToan;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblMaSP;
    private javax.swing.JLabel lblMoTa;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblThoiGian;
    private javax.swing.JLabel lblTienKH;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lblTilteChiTiet;
    private javax.swing.JLabel lblTilteThongTin;
    private javax.swing.JLabel lblTilteThongTinPhieu;
    private javax.swing.JLabel lblTong;
    private javax.swing.JPanel pAnh;
    private javax.swing.JPanel pBtnCTHD;
    private javax.swing.JPanel pButton;
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
    private javax.swing.JPanel pKieuThanhToan;
    private javax.swing.JPanel pMa;
    private javax.swing.JPanel pMaSP;
    private javax.swing.JPanel pMoTa;
    private javax.swing.JPanel pNhanVien;
    private javax.swing.JPanel pSDT;
    private javax.swing.JPanel pTenKH;
    private javax.swing.JPanel pTenSP;
    private javax.swing.JPanel pThemSP;
    private javax.swing.JPanel pThoiGian;
    private javax.swing.JPanel pThongTin;
    private javax.swing.JPanel pThongTinDien;
    private javax.swing.JPanel pThongTinHD;
    private javax.swing.JPanel pThongTinPhieu;
    private javax.swing.JPanel pTienKH;
    private javax.swing.JPanel pTienThua;
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
    private javax.swing.JTextField txtGioiTinh;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtThoiGian;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienThua;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTong;
    // End of variables declaration//GEN-END:variables
}

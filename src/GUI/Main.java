/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import GUI.frame.HoaDon.frmHoaDon;
import GUI.frame.HoaDon.frmSearchHoaDon;
import GUI.frame.KhachHang.frmKhachHang;
import GUI.frame.KhachHang.frmSearchKhachHang;
import GUI.frame.NhaCungCap.frmNhaCungCap;
import GUI.frame.NhaCungCap.frmSearchNhaCungCap;
import GUI.frame.NhanVien.frmNhanVien;
import GUI.frame.NhanVien.frmSearchNhanVien;
import GUI.frame.PhieuNhap.frmPhieuNhap;
import GUI.frame.PhieuNhap.frmSearchPhieuNhap;
import GUI.frame.SanPham.frmSanPham;
import GUI.frame.SanPham.frmSearchSanPham;
import GUI.frame.TaiKhoan.frmSearchTaiKhoan;
import GUI.frame.TaiKhoan.frmTaiKhoan;
import Swing.RoundedMenuItem;
import Swing.RoundedPopupMenu;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import Utils.SmoothImageLabel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author ADMIN
 */
public class Main extends javax.swing.JFrame {
    private RoundedPopupMenu popupMenuSP;
    private RoundedPopupMenu popupMenuMGG;
    private RoundedPopupMenu popupMenuNCC;
    private RoundedPopupMenu popupMenuKH;
    private RoundedPopupMenu popupMenuNV;
    private RoundedPopupMenu popupMenuTK;
    private RoundedPopupMenu popupMenuHD;
    private RoundedPopupMenu popupMenuPT;
    private RoundedPopupMenu popupMenuPN;
    private RoundedPopupMenu popupMenuPX;
    /**
     * Creates new form Main
     */
public Main() {
    initComponents();
    setTitle("Phần mềm quản lý cửa hàng tiện lợi");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    addActionListeners(Arrays.asList(btnThongKe, btnHoaDon, btnKhachHang, btnNhaCungCap, btnNhanVien, btnPhieuNhap,
                btnTaiKhoan, btnSanPham, btnDangXuat));
    //---------------MENU---------------------//
        // Menu Phiếu Nhập
        popupMenuPN = new RoundedPopupMenu();
        RoundedMenuItem itemPN1 = new RoundedMenuItem("Cập nhật");
        popupMenuPN.add(itemPN1);
        popupMenuPN.add(new JSeparator());

        RoundedMenuItem itemPN2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuPN.add(itemPN2);
        popupMenuPN.add(new JSeparator());

        btnPhieuNhap.addActionListener(e -> popupMenuPN.show(btnPhieuNhap, btnPhieuNhap.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemPN1, itemPN2});
     
        // Menu Nhà Cung Cấp
        popupMenuNCC = new RoundedPopupMenu();
        RoundedMenuItem itemNCC1 = new RoundedMenuItem("Cập nhật");
        popupMenuNCC.add(itemNCC1);
        popupMenuNCC.add(new JSeparator());

        RoundedMenuItem itemNCC2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuNCC.add(itemNCC2);
        popupMenuNCC.add(new JSeparator());

        btnNhaCungCap.addActionListener(e -> popupMenuNCC.show(btnNhaCungCap, btnNhaCungCap.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemNCC1, itemNCC2});

        // Menu Hóa đơn
        popupMenuHD = new RoundedPopupMenu();
        RoundedMenuItem itemHD1 = new RoundedMenuItem("Cập nhật");
        popupMenuHD.add(itemHD1);
        popupMenuHD.add(new JSeparator());

        RoundedMenuItem itemHD2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuHD.add(itemHD2);
        popupMenuHD.add(new JSeparator());

        btnHoaDon.addActionListener(e -> popupMenuHD.show(btnHoaDon, btnHoaDon.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemHD1, itemHD2});
    
        // Menu nhân viên
        popupMenuNV = new RoundedPopupMenu();
        RoundedMenuItem itemNV1 = new RoundedMenuItem("Cập nhật");
        popupMenuNV.add(itemNV1);
        popupMenuNV.add(new JSeparator());

        RoundedMenuItem itemNV2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuNV.add(itemNV2);
        popupMenuNV.add(new JSeparator());

        btnNhanVien.addActionListener(e -> popupMenuNV.show(btnNhanVien, btnNhanVien.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemNV1, itemNV2});     

        // Menu khách hàng
        popupMenuKH = new RoundedPopupMenu();
        RoundedMenuItem itemKH1 = new RoundedMenuItem("Cập nhật");
        popupMenuKH.add(itemKH1);
        popupMenuKH.add(new JSeparator());

        RoundedMenuItem itemKH2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuKH.add(itemKH2);
        popupMenuKH.add(new JSeparator());

        btnKhachHang.addActionListener(e -> popupMenuKH.show(btnKhachHang, btnKhachHang.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemKH1, itemKH2});    
        
        // Menu tài khoản
        popupMenuTK = new RoundedPopupMenu();
        RoundedMenuItem itemTK1 = new RoundedMenuItem("Cập nhật");
        popupMenuTK.add(itemTK1);
        popupMenuTK.add(new JSeparator());

        RoundedMenuItem itemTK2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuTK.add(itemTK2);
        popupMenuTK.add(new JSeparator());

        btnTaiKhoan.addActionListener(e -> popupMenuTK.show(btnTaiKhoan, btnTaiKhoan.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemTK1, itemTK2});    

        // Menu SẢN PHẨM
        popupMenuSP = new RoundedPopupMenu();
        RoundedMenuItem itemSP1 = new RoundedMenuItem("Cập nhật");
        popupMenuSP.add(itemSP1);
        popupMenuSP.add(new JSeparator());

        RoundedMenuItem itemSP2 = new RoundedMenuItem("Tìm kiếm");
        popupMenuSP.add(itemSP2);
        popupMenuSP.add(new JSeparator());

        btnSanPham.addActionListener(e -> popupMenuSP.show(btnSanPham, btnSanPham.getWidth(), 2));
        setFontForMenuItems(new RoundedMenuItem[]{itemSP1, itemSP2});       
        
      //-------------SỰ KIỆN------------//
        // Sự kiện nhà cung cấp
        itemNCC1.addActionListener(e -> {
            frmNhaCungCap ncc1 = new frmNhaCungCap();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(ncc1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemNCC2.addActionListener(e -> {
            frmSearchNhaCungCap ncc2 = new frmSearchNhaCungCap();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(ncc2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        
        // Sự kiện Hóa đơn
        itemHD1.addActionListener(e -> {
            frmHoaDon hd1 = new frmHoaDon();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(hd1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemHD2.addActionListener(e -> {
            frmSearchHoaDon hd2 = new frmSearchHoaDon();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(hd2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        
        // Sự kiện phiếu nhập
        itemPN1.addActionListener(e -> {
            frmPhieuNhap pn1 = new frmPhieuNhap();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(pn1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemPN2.addActionListener(e -> {
            frmSearchPhieuNhap pn2 = new frmSearchPhieuNhap();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(pn2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });

        // Sự kiện Khách hàng
        itemKH1.addActionListener(e -> {
            frmKhachHang kh1 = new frmKhachHang();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(kh1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemKH2.addActionListener(e -> {
            frmSearchKhachHang kh2 = new frmSearchKhachHang();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(kh2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });    

        // Sự kiện Nhân viên
        itemNV1.addActionListener(e -> {
            frmNhanVien nv1 = new frmNhanVien();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(nv1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemNV2.addActionListener(e -> {
            frmSearchNhanVien nv2 = new frmSearchNhanVien();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(nv2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });   

        // Sự kiện Tài khoản
        itemTK1.addActionListener(e -> {
            frmTaiKhoan tk1 = new frmTaiKhoan();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(tk1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemTK2.addActionListener(e -> {
            frmSearchTaiKhoan tk2 = new frmSearchTaiKhoan();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(tk2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        }); 
        
        // Sự kiện Sản phẩm
        itemSP1.addActionListener(e -> {
            frmSanPham sp1 = new frmSanPham();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(sp1, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        });
        itemSP2.addActionListener(e -> {
            frmSearchSanPham sp2 = new frmSearchSanPham();
            // Xóa tất cả các phần cũ
            pCenter.removeAll();
            // Đặt layout cho pCenter
            pCenter.setLayout(new java.awt.BorderLayout());

            // Thêm layout cho pCenter
            pCenter.add(sp2, java.awt.BorderLayout.CENTER);

            // Cập nhật lại giao diện
            pCenter.revalidate();
            pCenter.repaint();
        }); 
        
}

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Panel = new javax.swing.JPanel();
        pWest = new javax.swing.JPanel();
        rpWest = new Swing.RoundPanel();
        pThongTin = new Swing.RoundPanel();
        anhNV = new Swing.RoundPanel();
        lblAvatar = new javax.swing.JLabel();
        thongtinNV = new Swing.RoundPanel();
        lblName = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        pMenu = new Swing.RoundPanel();
        jspMenu = new javax.swing.JScrollPane();
        Menu = new javax.swing.JPanel();
        btnThongKe = new javax.swing.JButton();
        btnPhieuNhap = new javax.swing.JButton();
        btnNhaCungCap = new javax.swing.JButton();
        btnSanPham = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        btnHoaDon = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnNhanVien = new javax.swing.JButton();
        btnTaiKhoan = new javax.swing.JButton();
        pDangXuat = new Swing.RoundPanel();
        btnDangXuat = new javax.swing.JButton();
        pCenter = new javax.swing.JPanel();
        anhCenter = new SmoothImageLabel("/Icon/mainBackGround.png") ;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Panel.setBackground(new java.awt.Color(51, 51, 51));
        Panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Panel.setMinimumSize(new java.awt.Dimension(1200, 600));
        Panel.setName(""); // NOI18N
        Panel.setLayout(new java.awt.BorderLayout());

        pWest.setBackground(new java.awt.Color(204, 204, 204));
        pWest.setMinimumSize(new java.awt.Dimension(230, 40));
        pWest.setName("[230, 600]"); // NOI18N
        pWest.setPreferredSize(new java.awt.Dimension(230, 600));
        pWest.setLayout(new java.awt.BorderLayout());

        rpWest.setBackground(new java.awt.Color(204, 204, 204));
        rpWest.setAlignmentX(0.0F);
        rpWest.setAlignmentY(0.0F);
        rpWest.setMinimumSize(new java.awt.Dimension(230, 600));
        rpWest.setPreferredSize(new java.awt.Dimension(230, 600));
        rpWest.setLayout(new java.awt.BorderLayout());

        pThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pThongTin.setMinimumSize(new java.awt.Dimension(230, 70));
        pThongTin.setPreferredSize(new java.awt.Dimension(230, 70));
        pThongTin.setLayout(new java.awt.BorderLayout());

        anhNV.setBackground(new java.awt.Color(255, 255, 255));
        anhNV.setPreferredSize(new java.awt.Dimension(60, 100));
        anhNV.setLayout(new java.awt.GridBagLayout());

        lblAvatar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAvatar.setName(""); // NOI18N
        anhNV.add(lblAvatar, new java.awt.GridBagConstraints());

        pThongTin.add(anhNV, java.awt.BorderLayout.LINE_START);

        thongtinNV.setBackground(new java.awt.Color(255, 255, 255));

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblName.setText("ADMIN");
        lblName.setAlignmentY(0.0F);

        lblRole.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRole.setForeground(new java.awt.Color(102, 102, 102));
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRole.setText("admin");
        lblRole.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout thongtinNVLayout = new javax.swing.GroupLayout(thongtinNV);
        thongtinNV.setLayout(thongtinNVLayout);
        thongtinNVLayout.setHorizontalGroup(
            thongtinNVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thongtinNVLayout.createSequentialGroup()
                .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(thongtinNVLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        thongtinNVLayout.setVerticalGroup(
            thongtinNVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thongtinNVLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        pThongTin.add(thongtinNV, java.awt.BorderLayout.CENTER);

        rpWest.add(pThongTin, java.awt.BorderLayout.NORTH);

        pMenu.setBackground(new java.awt.Color(204, 204, 204));
        pMenu.setAlignmentY(0.0F);
        pMenu.setMaximumSize(new java.awt.Dimension(230, 480));
        pMenu.setMinimumSize(new java.awt.Dimension(230, 480));
        pMenu.setPreferredSize(new java.awt.Dimension(230, 480));
        pMenu.setLayout(new javax.swing.BoxLayout(pMenu, javax.swing.BoxLayout.LINE_AXIS));

        jspMenu.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        Menu.setMaximumSize(new java.awt.Dimension(10, 1000000));
        Menu.setMinimumSize(new java.awt.Dimension(10, 10));
        Menu.setPreferredSize(new java.awt.Dimension(10, 600));

        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThongKe.setText("Thống Kê");
        btnThongKe.setAlignmentX(0.5F);
        btnThongKe.setAlignmentY(0.0F);
        btnThongKe.setBorderPainted(false);
        btnThongKe.setHideActionText(true);
        btnThongKe.setMaximumSize(new java.awt.Dimension(208, 40));
        btnThongKe.setMinimumSize(new java.awt.Dimension(208, 40));
        btnThongKe.setPreferredSize(new java.awt.Dimension(208, 40));
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });
        Menu.add(btnThongKe);

        btnPhieuNhap.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPhieuNhap.setText("Phiếu Nhập");
        btnPhieuNhap.setAlignmentX(0.5F);
        btnPhieuNhap.setAlignmentY(0.0F);
        btnPhieuNhap.setBorderPainted(false);
        btnPhieuNhap.setMaximumSize(new java.awt.Dimension(208, 40));
        btnPhieuNhap.setMinimumSize(new java.awt.Dimension(208, 40));
        btnPhieuNhap.setPreferredSize(new java.awt.Dimension(208, 40));
        Menu.add(btnPhieuNhap);

        btnNhaCungCap.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhaCungCap.setText("Nhà Cung Cấp");
        btnNhaCungCap.setAlignmentX(0.5F);
        btnNhaCungCap.setAlignmentY(0.0F);
        btnNhaCungCap.setBorderPainted(false);
        btnNhaCungCap.setMaximumSize(new java.awt.Dimension(208, 40));
        btnNhaCungCap.setMinimumSize(new java.awt.Dimension(208, 40));
        btnNhaCungCap.setPreferredSize(new java.awt.Dimension(208, 40));
        btnNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhaCungCapActionPerformed(evt);
            }
        });
        Menu.add(btnNhaCungCap);

        btnSanPham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSanPham.setText("Sản Phẩm");
        btnSanPham.setToolTipText("");
        btnSanPham.setAlignmentX(0.5F);
        btnSanPham.setAlignmentY(0.0F);
        btnSanPham.setBorderPainted(false);
        btnSanPham.setMaximumSize(new java.awt.Dimension(208, 40));
        btnSanPham.setMinimumSize(new java.awt.Dimension(208, 40));
        btnSanPham.setPreferredSize(new java.awt.Dimension(208, 40));
        btnSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSanPhamActionPerformed(evt);
            }
        });
        Menu.add(btnSanPham);

        jSeparator2.setPreferredSize(new java.awt.Dimension(100, 10));
        Menu.add(jSeparator2);

        btnHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHoaDon.setText("Hoá Đơn");
        btnHoaDon.setAlignmentX(0.5F);
        btnHoaDon.setAlignmentY(0.0F);
        btnHoaDon.setBorderPainted(false);
        btnHoaDon.setMaximumSize(new java.awt.Dimension(208, 40));
        btnHoaDon.setMinimumSize(new java.awt.Dimension(208, 40));
        btnHoaDon.setPreferredSize(new java.awt.Dimension(208, 40));
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });
        Menu.add(btnHoaDon);

        btnKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.setAlignmentX(0.5F);
        btnKhachHang.setAlignmentY(0.0F);
        btnKhachHang.setBorderPainted(false);
        btnKhachHang.setMaximumSize(new java.awt.Dimension(208, 40));
        btnKhachHang.setMinimumSize(new java.awt.Dimension(208, 40));
        btnKhachHang.setPreferredSize(new java.awt.Dimension(208, 40));
        Menu.add(btnKhachHang);

        jSeparator1.setPreferredSize(new java.awt.Dimension(100, 10));
        Menu.add(jSeparator1);

        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.setAlignmentX(0.5F);
        btnNhanVien.setBorderPainted(false);
        btnNhanVien.setMaximumSize(new java.awt.Dimension(208, 40));
        btnNhanVien.setMinimumSize(new java.awt.Dimension(208, 40));
        btnNhanVien.setPreferredSize(new java.awt.Dimension(208, 40));
        Menu.add(btnNhanVien);

        btnTaiKhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaiKhoan.setText("Tài Khoản");
        btnTaiKhoan.setAlignmentX(0.5F);
        btnTaiKhoan.setAlignmentY(0.0F);
        btnTaiKhoan.setBorderPainted(false);
        btnTaiKhoan.setMaximumSize(new java.awt.Dimension(208, 40));
        btnTaiKhoan.setMinimumSize(new java.awt.Dimension(208, 40));
        btnTaiKhoan.setPreferredSize(new java.awt.Dimension(208, 40));
        Menu.add(btnTaiKhoan);

        jspMenu.setViewportView(Menu);

        pMenu.add(jspMenu);

        rpWest.add(pMenu, java.awt.BorderLayout.LINE_END);
        pMenu.getAccessibleContext().setAccessibleName("");

        pDangXuat.setBackground(new java.awt.Color(255, 255, 255));
        pDangXuat.setMinimumSize(new java.awt.Dimension(230, 50));
        pDangXuat.setPreferredSize(new java.awt.Dimension(230, 50));

        btnDangXuat.setBackground(new java.awt.Color(204, 204, 204));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDangXuat.setIcon(new FlatSVGIcon("./icon/logout.svg"));
        btnDangXuat.setText("ĐĂNG XUẤT");
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.setMaximumSize(new java.awt.Dimension(208, 42));
        btnDangXuat.setMinimumSize(new java.awt.Dimension(208, 42));
        btnDangXuat.setPreferredSize(new java.awt.Dimension(208, 42));
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        pDangXuat.add(btnDangXuat);

        rpWest.add(pDangXuat, java.awt.BorderLayout.SOUTH);

        pWest.add(rpWest, java.awt.BorderLayout.CENTER);

        Panel.add(pWest, java.awt.BorderLayout.LINE_START);

        pCenter.setBackground(new java.awt.Color(0, 51, 51));
        pCenter.setMinimumSize(new java.awt.Dimension(0, 0));
        pCenter.setLayout(new java.awt.BorderLayout());

        anhCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/Icon/mainBackground.png"))); // NOI18N
        anhCenter.setMaximumSize(new java.awt.Dimension(1400, 1400));
        anhCenter.setMinimumSize(new java.awt.Dimension(1400, 1400));
        anhCenter.setPreferredSize(new java.awt.Dimension(1400, 1400));
        pCenter.add(anhCenter, java.awt.BorderLayout.CENTER);

        Panel.add(pCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(Panel, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 1214, 608);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void btnSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSanPhamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSanPhamActionPerformed

    private void btnNhaCungCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhaCungCapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNhaCungCapActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThongKeActionPerformed
    private void addActionListeners(List<JButton> buttons) {
        for (JButton button : buttons) {
            button.addActionListener(this::changeButtonColor);
        }
    }
        private void changeButtonColor(ActionEvent e) {

        JButton sourceButton = (JButton) e.getSource();
        Component[] components = sourceButton.getParent().getComponents();

        // Đặt tất cả nút về màu mặc định
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setBackground(Color.WHITE);
            }
        }

        // Đổi màu nút được chọn
        sourceButton.setBackground(new Color(0, 155, 118));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            // Trỏ tới folder chứa file myTheme.properties trên classpath
            // Ví dụ bạn để file tại: src/resources/theme/myTheme.properties
            FlatLaf.registerCustomDefaultsSource("theme");

            // Đảm bảo file myTheme.properties được nạp cùng FlatLaf
            UIManager.put("FlatLaf.additionalDefaults", "myTheme.properties");

            // Chọn nền FlatLightLaf (áp dụng toàn bộ)
            UIManager.setLookAndFeel(new FlatLightLaf());

            // (Tuỳ chọn) Nếu muốn ép accent qua code:
            // UIManager.put("@accentColor", "#FFFFFF");

            // Cập nhật UI nếu có component tạo trước LAF
            FlatLaf.updateUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Tạo UI như bình thường
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
        private void setFontForMenuItems(RoundedMenuItem[] items) {
        Font font = new Font("Segoe UI", Font.BOLD, 12); // Thiết lập font ở đây
        for (RoundedMenuItem item : items) {
            item.setFont(font);
        }
    }
    public void replaceMainPanel(JPanel newPanel) {
        // Xóa tất cả các phần cũ trong mainPanel
        pCenter.removeAll();

        // Đặt layout cho mainPanel
        pCenter.setLayout(new java.awt.BorderLayout());

        // Thêm panel mới vào mainPanel
        pCenter.add(newPanel, java.awt.BorderLayout.CENTER);

        // Cập nhật lại giao diện
        pCenter.revalidate();
        pCenter.repaint();
    }    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel Panel;
    private javax.swing.JLabel anhCenter;
    private Swing.RoundPanel anhNV;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnNhaCungCap;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnPhieuNhap;
    private javax.swing.JButton btnSanPham;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JScrollPane jspMenu;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRole;
    private javax.swing.JPanel pCenter;
    private Swing.RoundPanel pDangXuat;
    private Swing.RoundPanel pMenu;
    private Swing.RoundPanel pThongTin;
    private javax.swing.JPanel pWest;
    private Swing.RoundPanel rpWest;
    private Swing.RoundPanel thongtinNV;
    // End of variables declaration//GEN-END:variables
}
class BackgroundPanel extends javax.swing.JPanel {
    private java.awt.Image bg;

    public BackgroundPanel(String resourcePath) {
        setOpaque(true);
        java.net.URL url = getClass().getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("Không tìm thấy ảnh: " + resourcePath);
        bg = new javax.swing.ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (bg == null) return;

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                            java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int W = getWidth(), H = getHeight();
        int w = bg.getWidth(null), h = bg.getHeight(null);
        double s = Math.max((double) W / w, (double) H / h); // COVER: phủ kín

        int sw = (int) (w * s), sh = (int) (h * s);
        int x = (W - sw) / 2, y = (H - sh) / 2;

        g2.drawImage(bg, x, y, sw, sh, null);
        g2.dispose();
    }
}



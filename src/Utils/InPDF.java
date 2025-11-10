package Utils;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Rectangle;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import Entity.PhieuNhap.CTPhieuNhap;
import Entity.HoaDon.CTHoaDon;
import Entity.HoaDon.HoaDon;
import Entity.PhieuNhap.PhieuNhap;

public class InPDF {

    private final DecimalFormat formatter = new DecimalFormat("###,###,###");
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Document document = new Document();
    private FileOutputStream file;
    private final JFrame jf = new JFrame();
    private final FileDialog fd = new FileDialog(jf, "Xuất PDF", FileDialog.SAVE);

    private Font fontNormal10;
    private Font fontBold15;
    private Font fontBold25;
    private Font fontBoldItalic15;

    public InPDF() {
        try {
            fontNormal10 = new Font(BaseFont.createFont("lib/TimesNewRoman/SVN-Times New Roman.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12, Font.NORMAL);
            fontBold25 = new Font(BaseFont.createFont("lib/TimesNewRoman/SVN-Times New Roman Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 25, Font.NORMAL);
            fontBold15 = new Font(BaseFont.createFont("lib/TimesNewRoman/SVN-Times New Roman Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 15, Font.NORMAL);
            fontBoldItalic15 = new Font(BaseFont.createFont("lib/TimesNewRoman/SVN-Times New Roman Bold Italic.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 15, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            Logger.getLogger(InPDF.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static Chunk createWhiteSpace(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(" ");
        return new Chunk(builder.toString());
    }

    private String getFile(String name) {
        fd.pack();
        fd.setSize(800, 600);
        Rectangle rect = jf.getContentPane().getBounds();
        fd.setLocation((int) rect.getCenterX() - 200, (int) rect.getCenterY() - 150);
        fd.setFile(name);
        fd.setVisible(true);
        if (fd.getFile() == null)
            return null;
        return fd.getDirectory() + fd.getFile();
    }

    private void openFile(String file) {
        try {
            Desktop.getDesktop().open(new File(file));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Không thể mở file PDF: " + e.getMessage());
        }
    }

    /* ============================ PHIẾU NHẬP ============================ */
    public void printPhieuNhap(PhieuNhap pn, List<CTPhieuNhap> listCTPN) {
        String url = getFile(pn.getMaPN());
        if (url == null)
            return;
        url += ".pdf";

        try {
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();

            // --- Header ---
            Paragraph header = new Paragraph("CỬA HÀNG TIỆN LỢI ABC", fontBold15);
            header.add(new Chunk(createWhiteSpace(50)));
            header.add(new Chunk("Ngày in: " + formatDate.format(new Date()), fontNormal10));
            document.add(header);
            document.add(Chunk.NEWLINE);

            Paragraph title = new Paragraph("PHIẾU NHẬP HÀNG", fontBold25);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // --- Thông tin phiếu ---
            document.add(new Paragraph("Mã phiếu nhập: " + pn.getMaPN(), fontNormal10));
            String tenNCC = (pn.getNCC() != null) ? pn.getNCC().getTenNCC() : "Chưa rõ";
            String sdtNCC = (pn.getNCC() != null && pn.getNCC().getSdt() != null)
                    ? pn.getNCC().getSdt()
                    : "Không có";
            document.add(new Paragraph("Nhà cung cấp: " + tenNCC, fontNormal10));
            document.add(new Paragraph("Số điện thoại: " + sdtNCC, fontNormal10));

            String nv = (pn.getNhanVien() != null) ? pn.getNhanVien().getTenNV() : "Chưa rõ";
            document.add(new Paragraph("Nhân viên nhập: " + nv, fontNormal10));

            if (pn.getThoiGian() != null)
                document.add(new Paragraph(
                        "Thời gian: " + formatDate.format(java.sql.Timestamp.valueOf(pn.getThoiGian())), fontNormal10));

            document.add(Chunk.NEWLINE);

            // --- Bảng sản phẩm ---
            PdfPTable table = new PdfPTable(5); // 5 cột: STT, Tên SP, Đơn giá, SL, Thành tiền
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 8f, 40f, 18f, 12f, 22f });

            // Tiêu đề bảng
            table.addCell(new PdfPCell(new Phrase("STT", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Tên sản phẩm", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", fontBold15)));

            // Dữ liệu sản phẩm
            BigDecimal tongTien = BigDecimal.ZERO;
            int stt = 1;

            for (CTPhieuNhap ctpn : listCTPN) {
                BigDecimal thanhTien = ctpn.getDonGia().multiply(BigDecimal.valueOf(ctpn.getSoLuong()));
                tongTien = tongTien.add(thanhTien);

                String tenSP = (ctpn.getSanPham() != null) ? ctpn.getSanPham().getTenSP() : "Không rõ";

                table.addCell(new PdfPCell(new Phrase(String.valueOf(stt++), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(tenSP, fontNormal10)));
                table.addCell(new PdfPCell(
                        new Phrase(formatter.format(ctpn.getDonGia().doubleValue()) + " đ", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ctpn.getSoLuong()), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(formatter.format(thanhTien.doubleValue()) + " đ", fontNormal10)));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // --- Tổng cộng ---
            Paragraph total = new Paragraph("Tổng giá trị nhập: " + formatter.format(tongTien.doubleValue()) + " đ",
                    fontBold15);
            total.setIndentationLeft(300);
            document.add(total);

            // --- Chữ ký ---
            document.add(Chunk.NEWLINE);
            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(20);
            signTable.setSpacingAfter(20);
            signTable.setWidths(new float[] { 1, 1 });

            // Ô tiêu đề “Người lập phiếu” và “Người giao hàng”
            PdfPCell cellLeft = new PdfPCell(new Phrase("Người lập phiếu", fontBoldItalic15));
            PdfPCell cellRight = new PdfPCell(new Phrase("Người giao hàng", fontBoldItalic15));

            // Canh giữa, bỏ viền
            cellLeft.setBorder(PdfPCell.NO_BORDER);
            cellRight.setBorder(PdfPCell.NO_BORDER);
            cellLeft.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellRight.setHorizontalAlignment(Element.ALIGN_CENTER);

            signTable.addCell(cellLeft);
            signTable.addCell(cellRight);

            // Dòng ghi chú
            PdfPCell cellLeftNote = new PdfPCell(new Phrase("(Ký và ghi rõ họ tên)", fontNormal10));
            PdfPCell cellRightNote = new PdfPCell(new Phrase("(Ký và ghi rõ họ tên)", fontNormal10));
            cellLeftNote.setBorder(PdfPCell.NO_BORDER);
            cellRightNote.setBorder(PdfPCell.NO_BORDER);
            cellLeftNote.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellRightNote.setHorizontalAlignment(Element.ALIGN_CENTER);

            signTable.addCell(cellLeftNote);
            signTable.addCell(cellRightNote);

            document.add(signTable);

            document.close();
            openFile(url);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi in phiếu nhập: " + e.getMessage());
        }
    }

    /* ============================ HÓA ĐƠN ============================ */
    public void printHoaDon(HoaDon hd, List<CTHoaDon> listCTHD) {
        String url = getFile(hd.getMaHD());
        if (url == null)
            return;
        url += ".pdf";

        try {
            file = new FileOutputStream(url);
            document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();

            // --- Header ---
            Paragraph header = new Paragraph("CỬA HÀNG TIỆN LỢI ABC", fontBold15);
            header.add(new Chunk(createWhiteSpace(50)));
            header.add(new Chunk("Ngày in: " + formatDate.format(new Date()), fontNormal10));
            document.add(header);
            document.add(Chunk.NEWLINE);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", fontBold25);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // --- Thông tin hóa đơn ---
            document.add(new Paragraph("Mã hóa đơn: " + hd.getMaHD(), fontNormal10));
            String kh = (hd.getKhachHang() != null) ? hd.getKhachHang().getTenKH() : "Khách lẻ";
            String sdt = (hd.getKhachHang() != null && hd.getKhachHang().getSdt() != null)
                    ? hd.getKhachHang().getSdt()
                    : "Không có";
            document.add(new Paragraph("Khách hàng: " + kh, fontNormal10));
            document.add(new Paragraph("Số điện thoại: " + sdt, fontNormal10));

            String nv = (hd.getNhanVien() != null) ? hd.getNhanVien().getTenNV() : "Chưa rõ";
            document.add(new Paragraph("Nhân viên bán hàng: " + nv, fontNormal10));

            if (hd.getThoiGian() != null)
                document.add(new Paragraph(
                        "Thời gian: " + formatDate.format(java.sql.Timestamp.valueOf(hd.getThoiGian())), fontNormal10));

            document.add(new Paragraph("Hình thức thanh toán: " + hd.getKieuThanhToan(), fontNormal10));
            document.add(Chunk.NEWLINE);

            // --- Bảng sản phẩm ---
            PdfPTable table = new PdfPTable(5); // 5 cột: STT, Tên SP, Đơn giá, SL, Thành tiền
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 8f, 40f, 18f, 12f, 22f });

            // Tiêu đề bảng
            table.addCell(new PdfPCell(new Phrase("STT", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Tên sản phẩm", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", fontBold15)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", fontBold15)));

            // Dữ liệu sản phẩm
            BigDecimal tongTien = BigDecimal.ZERO;
            int stt = 1;

            for (CTHoaDon cthd : listCTHD) {
                BigDecimal thanhTien = cthd.getDonGia().multiply(BigDecimal.valueOf(cthd.getSoLuong()));
                tongTien = tongTien.add(thanhTien);

                String tenSP = (cthd.getSanPham() != null) ? cthd.getSanPham().getTenSP() : "Không rõ";

                table.addCell(new PdfPCell(new Phrase(String.valueOf(stt++), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(tenSP, fontNormal10)));
                table.addCell(new PdfPCell(
                        new Phrase(formatter.format(cthd.getDonGia().doubleValue()) + " đ", fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(cthd.getSoLuong()), fontNormal10)));
                table.addCell(new PdfPCell(new Phrase(formatter.format(thanhTien.doubleValue()) + " đ", fontNormal10)));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // --- Tổng cộng ---
            Paragraph total = new Paragraph("Tổng thanh toán: " + formatter.format(tongTien.doubleValue()) + " đ",
                    fontBold15);
            total.setIndentationLeft(300);
            document.add(total);

            // --- Chữ ký ---
            document.add(Chunk.NEWLINE);
            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(20);
            signTable.setSpacingAfter(20);
            signTable.setWidths(new float[] { 1, 1 });

            // Ô tiêu đề “Người lập hóa đơn” và “Khách hàng”
            PdfPCell cellLeft = new PdfPCell(new Phrase("Người lập hóa đơn", fontBoldItalic15));
            PdfPCell cellRight = new PdfPCell(new Phrase("Khách hàng", fontBoldItalic15));

            // Canh giữa, bỏ viền
            cellLeft.setBorder(PdfPCell.NO_BORDER);
            cellRight.setBorder(PdfPCell.NO_BORDER);
            cellLeft.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellRight.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Thêm 2 ô tiêu đề
            signTable.addCell(cellLeft);
            signTable.addCell(cellRight);

            // Ô dòng ghi chú
            PdfPCell cellLeftNote = new PdfPCell(new Phrase("(Ký và ghi rõ họ tên)", fontNormal10));
            PdfPCell cellRightNote = new PdfPCell(new Phrase("(Ký và ghi rõ họ tên)", fontNormal10));
            cellLeftNote.setBorder(PdfPCell.NO_BORDER);
            cellRightNote.setBorder(PdfPCell.NO_BORDER);
            cellLeftNote.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellRightNote.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Thêm dòng ghi chú
            signTable.addCell(cellLeftNote);
            signTable.addCell(cellRightNote);

            // Thêm bảng chữ ký vào tài liệu
            document.add(signTable);

            document.close();
            openFile(url);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi in hóa đơn: " + e.getMessage());
        }
    }

}

    package Entity.SanPham;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.Objects;

    public class SanPham {
        private String maSP; // PK
        private String tenSP; // nvarchar(50)
        private byte[] anhSP; // varbinary(max)
        private String moTaSP; // nvarchar(50)
        private DanhMuc danhMuc; // FK -> DanhMuc
        private DonViTinh donViTinh; // FK -> DonViTinh
        private XuatXu xuatXu; // FK -> XuatXu
        private int soLuong; // int NOT NULL
        private BigDecimal giaNhap; // DECIMAL(18,2)
        private BigDecimal donGia; // DECIMAL(18,2)
        private LocalDateTime hsd; // datetime

        public SanPham() {
            this.giaNhap = BigDecimal.ZERO;
            this.donGia = BigDecimal.ZERO;
        }

        // Constructor đầy đủ
        public SanPham(String maSP, String tenSP, byte[] anhSP, String moTaSP,
                DanhMuc danhMuc, DonViTinh donViTinh, XuatXu xuatXu,
                int soLuong, BigDecimal giaNhap, BigDecimal donGia, LocalDateTime hsd) {
            this.maSP = maSP;
            this.tenSP = tenSP;
            this.anhSP = anhSP;
            this.moTaSP = moTaSP;
            this.danhMuc = danhMuc;
            this.donViTinh = donViTinh;
            this.xuatXu = xuatXu;
            this.soLuong = soLuong;
            this.giaNhap = giaNhap != null ? giaNhap : BigDecimal.ZERO;
            this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
            this.hsd = hsd;
        }

        // Constructor rút gọn chỉ cần mã (phục vụ DAO hoặc FK)
        public SanPham(String maSP) {
            this.maSP = maSP;
        }

        // --- Getters & Setters ---
        public String getMaSP() {
            return maSP;
        }

        public void setMaSP(String maSP) {
            this.maSP = maSP;
        }

        public String getTenSP() {
            return tenSP;
        }

        public void setTenSP(String tenSP) {
            this.tenSP = tenSP;
        }

        public byte[] getAnhSP() {
            return anhSP;
        }

        public void setAnhSP(byte[] anhSP) {
            this.anhSP = anhSP;
        }

        public String getMoTaSP() {
            return moTaSP;
        }

        public void setMoTaSP(String moTaSP) {
            this.moTaSP = moTaSP;
        }

        public DanhMuc getDanhMuc() {
            return danhMuc;
        }

        public void setDanhMuc(DanhMuc danhMuc) {
            this.danhMuc = danhMuc;
        }

        public DonViTinh getDonViTinh() {
            return donViTinh;
        }

        public void setDonViTinh(DonViTinh donViTinh) {
            this.donViTinh = donViTinh;
        }

        public XuatXu getXuatXu() {
            return xuatXu;
        }

        public void setXuatXu(XuatXu xuatXu) {
            this.xuatXu = xuatXu;
        }

        public int getSoLuong() {
            return soLuong;
        }

        public void setSoLuong(int soLuong) {
            this.soLuong = soLuong;
        }

        public BigDecimal getGiaNhap() {
            return giaNhap;
        }

        public void setGiaNhap(BigDecimal giaNhap) {
            this.giaNhap = giaNhap != null ? giaNhap : BigDecimal.ZERO;
        }

        public BigDecimal getDonGia() {
            return donGia;
        }

        public void setDonGia(BigDecimal donGia) {
            this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
        }

        public LocalDateTime getHsd() {
            return hsd;
        }

        public void setHsd(LocalDateTime hsd) {
            this.hsd = hsd;
        }

        // --- equals & hashCode ---
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof SanPham))
                return false;
            SanPham that = (SanPham) o;
            return Objects.equals(maSP, that.maSP);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maSP);
        }

        // --- toString ---
        @Override
        public String toString() {
            return "SanPham{" +
                    "maSP='" + maSP + '\'' +
                    ", tenSP='" + tenSP + '\'' +
                    ", danhMuc=" + (danhMuc != null ? danhMuc.getTenDM() : "null") +
                    ", donViTinh=" + (donViTinh != null ? donViTinh.getTenDVT() : "null") +
                    ", xuatXu=" + (xuatXu != null ? xuatXu.getTenXX() : "null") +
                    ", soLuong=" + soLuong +
                    ", donGia=" + donGia +
                    '}';
        }
    }

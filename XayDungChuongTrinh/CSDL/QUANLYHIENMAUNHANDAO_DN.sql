-- =============================================================
-- ĐỒ ÁN: HỆ THỐNG QUẢN LÝ HIẾN MÁU NHÂN ĐẠO ĐÀ NẴNG 
-- SINH VIÊN THỰC HIỆN: NHÓM 20 
-- VĂN QUÝ VƯƠNG - PHẠM MINH HUY - LÊ VIỆT HƯNG - LÊ VĂN MẠNH
-- =============================================================

DROP DATABASE IF EXISTS QuanLyHienMauDN;
CREATE DATABASE QuanLyHienMauDN CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuanLyHienMauDN;

-- -------------------------------------------------------------
-- BƯỚC 1: TẠO CẤU TRÚC BẢNG (CREATE TABLES)
-- -------------------------------------------------------------

-- 1.1. Nhóm danh mục cơ sở
CREATE TABLE VAITRO (
    maVaiTro CHAR(10) PRIMARY KEY,
    tenVaiTro VARCHAR(50) NOT NULL
);

CREATE TABLE PHUONGXA (
    maPhuongXa CHAR(10) PRIMARY KEY,
    tenPhuongXa VARCHAR(100) NOT NULL
);

CREATE TABLE DIADIEM (
    maDiaDiem CHAR(10) PRIMARY KEY,
    tenDiaDiem VARCHAR(150) NOT NULL,
    diaChiChiTiet VARCHAR(255) NOT NULL,
    maPhuongXa CHAR(10),
    loaiDiaDiem VARCHAR(50) 
);

CREATE TABLE KHOACONGTAC (
    maKhoa CHAR(10) PRIMARY KEY,
    tenKhoa VARCHAR(100) NOT NULL
);

-- 1.2. Nhóm Tài khoản & Nhân sự
CREATE TABLE TAIKHOAN (
    maTaiKhoan CHAR(10) PRIMARY KEY,
    maVaiTro CHAR(10),
    email VARCHAR(100) NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    trangThai BOOLEAN DEFAULT TRUE 
);

CREATE TABLE NHANVIEN (
    maNhanVien CHAR(10) PRIMARY KEY,
    maTaiKhoan CHAR(10),
    maKhoa CHAR(10),
    maDiaDiem CHAR(10),
    hoTen VARCHAR(100) NOT NULL,
    CCCD CHAR(12) NOT NULL,
    gioiTinh VARCHAR(10),
    soDienThoai VARCHAR(10) NOT NULL
);

CREATE TABLE TINHNGUYENVIEN (
    maTNV CHAR(10) PRIMARY KEY,
    maTaiKhoan CHAR(10),
    maPhuongXa CHAR(10),
    hoTen VARCHAR(100) NOT NULL,
    CCCD CHAR(12) NOT NULL,
    ngaySinh DATE NOT NULL,
    gioiTinh VARCHAR(10),
    soDienThoai VARCHAR(10) NOT NULL,
    nhomMau VARCHAR(5),
    diaChi VARCHAR(255)
);

-- 1.3. Nhóm Quản lý Chiến dịch & Đăng ký
CREATE TABLE CHIENDICHHIENMAU (
    maChienDich CHAR(10) PRIMARY KEY,
    maDiaDiem CHAR(10),
    maNhanVien CHAR(10),
    tenChienDich VARCHAR(255) NOT NULL,
    thoiGianBD DATETIME NOT NULL,
    thoiGianKT DATETIME NOT NULL,
    soLuongDuKien INT,
    trangThai VARCHAR(50) NOT NULL,
    maQR VARCHAR(255)
);

CREATE TABLE DONDANGKY (
    maDon CHAR(10) PRIMARY KEY,
    maTNV CHAR(10),
    maChienDich CHAR(10),
    maNhanVien CHAR(10) DEFAULT NULL,
    maQR VARCHAR(255),
    thoiGianDangKy DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai VARCHAR(50) NOT NULL
);

CREATE TABLE HOSOSUCKHOE (
    maHoSo CHAR(10) PRIMARY KEY,
    maDon CHAR(10),
    benhLyNen VARCHAR(255),
    moTaKhac VARCHAR(255)
);

-- 1.4. Nhóm Y tế & Kho máu
CREATE TABLE KETQUALAMSANG (
    maKQ CHAR(10) PRIMARY KEY,
    maDon CHAR(10),
    maNhanVien CHAR(10),
    huyetAp VARCHAR(20),
    nhipTim INT,
    canNang DOUBLE,
    nhietDo DOUBLE,
    ketQua BOOLEAN,
    lyDoTuChoi VARCHAR(255)
);

CREATE TABLE KHOMAU (
    maKho CHAR(10) PRIMARY KEY,
    nhomMau VARCHAR(10),
    soLuongTon INT DEFAULT 0,
    nguongAnToan INT DEFAULT 10
);

CREATE TABLE TUIMAU (
    maTuiMau CHAR(15) PRIMARY KEY,
    maDon CHAR(10),
    maNhanVien CHAR(10),
    maKho CHAR(10),
    theTich INT,
    thoiGianLayMau DATETIME,
    trangThai VARCHAR(50) NOT NULL,
    nhietDoVanChuyen DOUBLE
);

CREATE TABLE KETQUAXETNGHIEM (
    maKQ CHAR(10) PRIMARY KEY,
    maTuiMau CHAR(15),
    maNhanVien CHAR(10),
    nhomMau VARCHAR(5),
    moTa VARCHAR(255)
);

-- 1.5. Nhập xuất & Tương tác
CREATE TABLE PHIEUNHAPXUAT (
    maPhieu CHAR(10) PRIMARY KEY,
    maNhanVien CHAR(10),
    loaiPhieu VARCHAR(50) NOT NULL,
    ngayNhapXuat DATE
);

CREATE TABLE CHITIETNHAPXUAT (
    maPhieu CHAR(10),
    maTuiMau CHAR(15),
    PRIMARY KEY (maPhieu, maTuiMau)
);

CREATE TABLE CHUNGNHAN (
    maChungNhan CHAR(10) PRIMARY KEY,
    maDon CHAR(10),
    maNhanVien CHAR(10),
    filePDF VARCHAR(255),
    ngayCap DATE
);

CREATE TABLE TINTUC (
    maTinTuc CHAR(10) PRIMARY KEY,
    maNhanVien CHAR(10),
    tieuDe VARCHAR(255) NOT NULL,
    noiDung TEXT,
    hinhAnh VARCHAR(255),
    ngayDang DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai VARCHAR(50)
);

CREATE TABLE THONGBAO (
    maThongBao CHAR(10) PRIMARY KEY,
    maTaiKhoanGui CHAR(10),
    maTaiKhoanNhan CHAR(10),
    noiDung TEXT,
    thoiGianGui DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai VARCHAR(50)
);

CREATE TABLE TINNHAN (
    maTinNhan CHAR(10) PRIMARY KEY,
    maTaiKhoanGui CHAR(10),
    maTaiKhoanNhan CHAR(10),
    noiDung TEXT,
    thoiGian DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai BOOLEAN DEFAULT FALSE 
);

-- -------------------------------------------------------------
-- BƯỚC 2: THIẾT LẬP RÀNG BUỘC (ALTER TABLE)
-- -------------------------------------------------------------

-- 2.1. Định danh & Liên lạc
ALTER TABLE NHANVIEN ADD CONSTRAINT chk_cccd_nv CHECK (CCCD REGEXP '^[0-9]{12}$');
ALTER TABLE TINHNGUYENVIEN ADD CONSTRAINT chk_cccd_tnv CHECK (CCCD REGEXP '^[0-9]{12}$');
ALTER TABLE NHANVIEN ADD CONSTRAINT chk_sdt_nv CHECK (soDienThoai REGEXP '^0[0-9]{9}$');
ALTER TABLE TINHNGUYENVIEN ADD CONSTRAINT chk_sdt_tnv CHECK (soDienThoai REGEXP '^0[0-9]{9}$');
ALTER TABLE TAIKHOAN ADD CONSTRAINT chk_email_tk CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$');

-- 2.2. Y tế
ALTER TABLE KETQUALAMSANG ADD CONSTRAINT chk_can_nang CHECK (canNang >= 45);
ALTER TABLE TUIMAU ADD CONSTRAINT chk_the_tich CHECK (theTich IN (250, 350, 450));

-- 2.3. Trạng thái (Chi tiết theo yêu cầu của Vương)
ALTER TABLE THONGBAO ADD CONSTRAINT chk_tt_thong_bao CHECK (trangThai IN ('Đã đọc', 'Chưa đọc'));
ALTER TABLE TINTUC ADD CONSTRAINT chk_tt_tin_tuc CHECK (trangThai IN ('Đã thêm', 'Đã xoá', 'Hết hạn'));
ALTER TABLE DONDANGKY ADD CONSTRAINT chk_tt_don CHECK (trangThai IN ('Đã đăng ký', 'Đã hiến', 'Đã nhận chứng nhận', 'Chưa hiến'));
ALTER TABLE TUIMAU ADD CONSTRAINT chk_tt_tui CHECK (trangThai IN ('Chờ xét nghiệm', 'Nhập kho', 'Đã xuất', 'Hủy'));
ALTER TABLE CHIENDICHHIENMAU ADD CONSTRAINT chk_tt_cd CHECK (trangThai IN ('Đang lập kế hoạch', 'Đã phê duyệt', 'Đang diễn ra', 'Đã kết thúc'));
ALTER TABLE PHIEUNHAPXUAT ADD CONSTRAINT chk_loai_phieu CHECK (loaiPhieu IN ('Nhập kho', 'Xuất kho'));

-- -------------------------------------------------------------
-- BƯỚC 3: THIẾT LẬP KHÓA NGOẠI (FOREIGN KEYS)
-- -------------------------------------------------------------

ALTER TABLE DIADIEM ADD FOREIGN KEY (maPhuongXa) REFERENCES PHUONGXA(maPhuongXa);
ALTER TABLE TAIKHOAN ADD FOREIGN KEY (maVaiTro) REFERENCES VAITRO(maVaiTro);
ALTER TABLE NHANVIEN ADD FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan);
ALTER TABLE NHANVIEN ADD FOREIGN KEY (maKhoa) REFERENCES KHOACONGTAC(maKhoa);
ALTER TABLE NHANVIEN ADD FOREIGN KEY (maDiaDiem) REFERENCES DIADIEM(maDiaDiem);
ALTER TABLE TINHNGUYENVIEN ADD FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan);
ALTER TABLE TINHNGUYENVIEN ADD FOREIGN KEY (maPhuongXa) REFERENCES PHUONGXA(maPhuongXa);
ALTER TABLE CHIENDICHHIENMAU ADD FOREIGN KEY (maDiaDiem) REFERENCES DIADIEM(maDiaDiem);
ALTER TABLE CHIENDICHHIENMAU ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE DONDANGKY ADD FOREIGN KEY (maTNV) REFERENCES TINHNGUYENVIEN(maTNV);
ALTER TABLE DONDANGKY ADD FOREIGN KEY (maChienDich) REFERENCES CHIENDICHHIENMAU(maChienDich);
ALTER TABLE DONDANGKY ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE HOSOSUCKHOE ADD FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon);
ALTER TABLE TUIMAU ADD FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon);
ALTER TABLE TUIMAU ADD FOREIGN KEY (maKho) REFERENCES KHOMAU(maKho);
ALTER TABLE TUIMAU ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE KETQUALAMSANG ADD FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon);
ALTER TABLE KETQUALAMSANG ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE KETQUAXETNGHIEM ADD FOREIGN KEY (maTuiMau) REFERENCES TUIMAU(maTuiMau);
ALTER TABLE KETQUAXETNGHIEM ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE PHIEUNHAPXUAT ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE CHITIETNHAPXUAT ADD FOREIGN KEY (maPhieu) REFERENCES PHIEUNHAPXUAT(maPhieu);
ALTER TABLE CHITIETNHAPXUAT ADD FOREIGN KEY (maTuiMau) REFERENCES TUIMAU(maTuiMau);
ALTER TABLE CHUNGNHAN ADD FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon);
ALTER TABLE CHUNGNHAN ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE TINTUC ADD FOREIGN KEY (maNhanVien) REFERENCES NHANVIEN(maNhanVien);
ALTER TABLE THONGBAO ADD FOREIGN KEY (maTaiKhoanGui) REFERENCES TAIKHOAN(maTaiKhoan);
ALTER TABLE THONGBAO ADD FOREIGN KEY (maTaiKhoanNhan) REFERENCES TAIKHOAN(maTaiKhoan);
ALTER TABLE TINNHAN ADD FOREIGN KEY (maTaiKhoanGui) REFERENCES TAIKHOAN(maTaiKhoan);
ALTER TABLE TINNHAN ADD FOREIGN KEY (maTaiKhoanNhan) REFERENCES TAIKHOAN(maTaiKhoan);
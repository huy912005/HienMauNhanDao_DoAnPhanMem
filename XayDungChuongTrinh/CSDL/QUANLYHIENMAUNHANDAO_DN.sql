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
-- Cập nhật ràng buộc cân nặng xuống 40kg để cho phép D45 thất bại một cách hợp lệ
ALTER TABLE KETQUALAMSANG ADD CONSTRAINT chk_can_nang CHECK (canNang >= 40); 
ALTER TABLE TUIMAU ADD CONSTRAINT chk_the_tich CHECK (theTich IN (250, 350, 450));

-- 2.3. Trạng thái 
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

-- =============================================================
-- 1. DANH MỤC CƠ SỞ, ĐỊA ĐIỂM & KHOA CÔNG TÁC (QUY MÔ TP ĐÀ NẴNG)
-- =============================================================

INSERT INTO VAITRO VALUES 
('AD','Quản trị hệ thống'), 
('BS','Bác sĩ chuyên khoa'), 
('NV_SL','Nhân viên sàng lọc'),
('NV_TN','Nhân viên thu nhận'), 
('NV_XN','Nhân viên xét nghiệm'), 
('QLK','Quản lý kho máu'), 
('TNV','Tình nguyện viên');

INSERT INTO PHUONGXA VALUES 
('HC01','Phường Thạch Thang, Hải Châu'), 
('HC02','Phường Hải Châu I, Hải Châu'), 
('HC03','Phường Hải Châu II, Hải Châu'), 
('HC04','Phường Thuận Phước, Hải Châu'), 
('HC05','Phường Thanh Bình, Hải Châu'), 
('LC01','Phường Hòa Khánh Bắc, Liên Chiểu'), 
('LC02','Phường Hòa Minh, Liên Chiểu'), 
('LC03','Phường Hòa Khánh Nam, Liên Chiểu'), 
('NHS01','Phường Khuê Mỹ, Ngũ Hành Sơn'), 
('NHS02','Phường Mỹ An, Ngũ Hành Sơn'), 
('TK01','Phường Vĩnh Trung, Thanh Khê'), 
('TK02','Phường Thạc Gián, Thanh Khê');

INSERT INTO DIADIEM VALUES 
('DD01','Bệnh viện Đà Nẵng','124 Hải Phòng','HC01','Bệnh viện'),
('DD02','Bệnh viện C Đà Nẵng','122 Hải Phòng','HC01','Bệnh viện'),
('DD03','Bệnh viện Ung bướu','Hoàng Trung Thông','LC02','Bệnh viện'),
('DD04','Bệnh viện Phụ sản - Nhi','402 Lê Văn Hiến','NHS01','Bệnh viện'),
('DD05','Hội Chữ thập đỏ TP Đà Nẵng','522 Ông Ích Khiêm','HC03','Điểm cố định'),
('DD06','ĐH Sư phạm Kỹ thuật (UTE)','48 Cao Thắng','HC01','Điểm lưu động'),
('DD07','Đại học Đông Á','33 Xô Viết Nghệ Tĩnh','HC02','Điểm lưu động');

INSERT INTO KHOACONGTAC VALUES 
('KHH_DN','Khoa Huyết học - BV Đà Nẵng'), 
('KLS_DN','Khoa Khám bệnh - BV Đà Nẵng'), 
('KXN_DN','Khoa Xét nghiệm - BV Đà Nẵng'),
('KHH_C','Khoa Huyết học - BV C'), 
('KCC_PSN','Khoa Cấp cứu - BV Phụ sản Nhi'), 
('KHH_UB','Khoa Huyết học - BV Ung bướu');

-- =============================================================
-- 2. HỆ THỐNG TÀI KHOẢN (DÙNG CHUNG NHÂN VIÊN & TNV CÓ APP)
-- =============================================================

INSERT INTO TAIKHOAN VALUES 
-- Tài khoản Nhân sự y tế & Admin (15 người)
('TK_NV01','BS','lequoctuan.bs@bvdn.vn','123',1), 
('TK_NV02','BS','phamhongngoc.bs@bvdn.vn','123',1), 
('TK_NV03','NV_SL','nguyenthilan.sl@bvdn.vn','123',1),
('TK_NV04','NV_SL','trinhdieuthuy.sl@bvdn.vn','123',1), 
('TK_NV05','NV_SL','dolanphuong.sl@bvc.vn','123',1), 
('TK_NV06','NV_TN','dangvanmanh.tn@bvdn.vn','123',1),
('TK_NV07','NV_TN','levanhoang.tn@bvdn.vn','123',1), 
('TK_NV08','NV_TN','vubaoquynh.tn@bvc.vn','123',1), 
('TK_NV09','NV_XN','hoangthihuy.xn@bvdn.vn','123',1),
('TK_NV10','NV_XN','nguyentuyetmai.xn@bvdn.vn','123',1), 
('TK_NV11','NV_XN','trandinhnam.xn@bvub.vn','123',1), 
('TK_NV12','QLK','tranminhhung.kho@bvdn.vn','123',1),
('TK_NV13','QLK','lamtandat.kho@bvc.vn','123',1), 
('TK_NV14','AD','admin.system@redcross.dn.vn','123',1), 
('TK_NV15','BS','vunhatminh.bs@bvc.vn','123',1),

-- Tài khoản Tình nguyện viên đã đăng ký App (30 người)
('TK_U01','TNV','vanquyvuong.it@gmail.com','123',1), 
('TK_U02','TNV','phamminhhuy.dev@gmail.com','123',1), 
('TK_U03','TNV','leviethung.tech@gmail.com','123',1),
('TK_U04','TNV','levanmanh.2002@gmail.com','123',1), 
('TK_U05','TNV','tranthuha.95@yahoo.com','123',1), 
('TK_U06','TNV','nguyenhoanglong.dn@outlook.com','123',1),
('TK_U07','TNV','dothanhvinh.arch@gmail.com','123',1), 
('TK_U08','TNV','truonggiabao.2003@gmail.com','123',1), 
('TK_U09','TNV','nguyenthuylinh.marketing@gmail.com','123',1),
('TK_U10','TNV','vudinhphong.eng@gmail.com','123',1), 
('TK_U11','TNV','trancongminh.hr@yahoo.com','123',1), 
('TK_U12','TNV','ngothanhson.sale@gmail.com','123',1),
('TK_U13','TNV','dangngocthao.199x@gmail.com','123',1), 
('TK_U14','TNV','hoangvanbach.teacher@outlook.com','123',1), 
('TK_U15','TNV','phanvantri.mechanic@gmail.com','123',1),
('TK_U16','TNV','lyphuongchau.design@gmail.com','123',1), 
('TK_U17','TNV','buiquangdung.law@gmail.com','123',1), 
('TK_U18','TNV','maithanhyen.nurse@yahoo.com','123',1),
('TK_U19','TNV','duongkhactiep.freelance@gmail.com','123',1), 
('TK_U20','TNV','votritai.banking@gmail.com','123',1), 
('TK_U21','TNV','doanbaotram.edu@gmail.com','123',1),
('TK_U22','TNV','hovietthai.it@gmail.com','123',1), 
('TK_U23','TNV','chautuankiet.photo@outlook.com','123',1), 
('TK_U24','TNV','tonnuquynh.chef@gmail.com','123',1),
('TK_U25','TNV','lamchikhang.seo@gmail.com','123',1), 
('TK_U26','TNV','dinhngocdiep.writer@yahoo.com','123',1), 
('TK_U27','TNV','thainhatnam.dev@gmail.com','123',1),
('TK_U28','TNV','kieuthanhhuong.acc@gmail.com','123',1), 
('TK_U29','TNV','trinhcamtu.logistics@gmail.com','123',1), 
('TK_U30','TNV','luugiahuy.tour@outlook.com','123',1);

-- =============================================================
-- 3. NHÂN VIÊN Y TẾ ĐƯỢC PHÂN CÔNG (15 NGƯỜI)
-- =============================================================

INSERT INTO NHANVIEN VALUES 
('NV01','TK_NV01','KLS_DN','DD01','Lê Quốc Tuấn','048075000001','Nam','0905123456'),
('NV02','TK_NV02','KLS_DN','DD01','Phạm Hồng Ngọc','048182000002','Nữ','0914234567'),
('NV03','TK_NV03','KLS_DN','DD01','Nguyễn Thị Lan','048185000003','Nữ','0935345678'),
('NV04','TK_NV04','KLS_DN','DD01','Trịnh Diệu Thúy','048190000004','Nữ','0905456789'),
('NV05','TK_NV05','KHH_C','DD02','Đỗ Lan Phương','048188000005','Nữ','0702567890'),
('NV06','TK_NV06','KHH_DN','DD01','Đặng Văn Mạnh','048092000006','Nam','0385678901'),
('NV07','TK_NV07','KHH_DN','DD01','Lê Văn Hoàng','048095000007','Nam','0914789012'),
('NV08','TK_NV08','KHH_C','DD02','Vũ Bảo Quỳnh','048195000008','Nữ','0935890123'),
('NV09','TK_NV09','KXN_DN','DD01','Hoàng Thị Huy','048190000009','Nữ','0905901234'),
('NV10','TK_NV10','KXN_DN','DD01','Nguyễn Tuyết Mai','048199000010','Nữ','0702012345'),
('NV11','TK_NV11','KHH_UB','DD03','Trần Đình Nam','048096000011','Nam','0914123450'),
('NV12','TK_NV12','KHH_DN','DD01','Trần Minh Hưng','048080000012','Nam','0935234561'),
('NV13','TK_NV13','KHH_C','DD02','Lâm Tấn Đạt','048085000013','Nam','0905345672'),
('NV14','TK_NV14','KLS_DN','DD01','Admin Hệ Thống','048099000014','Nam','0702456783'),
('NV15','TK_NV15','KLS_DN','DD02','Vũ Nhật Minh','048088000015','Nam','0914567894');

-- =============================================================
-- 4. TÌNH NGUYỆN VIÊN (50 NGƯỜI)
-- =============================================================

-- Nhóm 1 & 3: 30 người CÓ TÀI KHOẢN APP (Liên kết bảng TAIKHOAN)
INSERT INTO TINHNGUYENVIEN VALUES 
('TNV01','TK_U01','LC01','Văn Quý Vương','048002000101','2002-08-15','Nam','0385111001','B+','48 Cao Thắng, Liên Chiểu'),
('TNV02','TK_U02','LC02','Phạm Minh Huy','048002000102','2002-10-12','Nam','0385111002','O+','12 Nguyễn Lương Bằng'),
('TNV03','TK_U03','HC01','Lê Việt Hưng','048001000103','2001-05-20','Nam','0385111003','A+','56 Quang Trung, Hải Châu'),
('TNV04','TK_U04','LC02','Lê Văn Mạnh','048002000104','2002-12-05','Nam','0385111004','O+','Tôn Đức Thắng, Liên Chiểu'),
('TNV05','TK_U05','HC02','Trần Thu Hà','048195000105','1995-03-12','Nữ','0905111005','AB+','Bạch Đằng, Hải Châu'),
('TNV06','TK_U06','NHS01','Nguyễn Hoàng Long','048088000106','1988-11-25','Nam','0935111006','O-','Lê Văn Hiến, Ngũ Hành Sơn'),
('TNV07','TK_U07','LC01','Đỗ Thành Vinh','048096000107','1996-02-15','Nam','0905111007','O+','Ngô Thì Nhậm, Liên Chiểu'),
('TNV08','TK_U08','NHS01','Trương Gia Bảo','048003000108','2003-01-20','Nam','0702111008','A-','Hồ Xuân Hương, Ngũ Hành Sơn'),
('TNV09','TK_U09','HC02','Nguyễn Thùy Linh','048197000109','1997-04-18','Nữ','0905111009','B+','Trần Phú, Hải Châu'),
('TNV10','TK_U10','LC01','Vũ Đình Phong','048085000110','1985-09-05','Nam','0914111010','AB-','Nguyễn Sinh Sắc, Liên Chiểu'),
('TNV11','TK_U11','HC01','Trần Công Minh','048090000111','1990-12-30','Nam','0905111011','A+','Đống Đa, Hải Châu'),
('TNV12','TK_U12','LC01','Ngô Thanh Sơn','048094000112','1994-08-14','Nam','0935111012','B+','Phạm Như Xương, Liên Chiểu'),
('TNV13','TK_U13','HC02','Đặng Ngọc Thảo','048100000113','2000-03-25','Nữ','0914111013','O+','Lê Lợi, Hải Châu'),
('TNV14','TK_U14','HC01','Hoàng Văn Bách','048082000114','1982-12-12','Nam','0914111014','O+','Lý Tự Trọng, Hải Châu'),
('TNV15','TK_U15','LC01','Phan Văn Trị','048090000115','1990-03-03','Nam','0914111015','AB+','Nguyễn Chánh, Liên Chiểu'),
('TNV16','TK_U16','LC02','Lý Phương Châu','048199000116','1999-07-22','Nữ','0905111016','A+','Hoàng Thị Loan, Liên Chiểu'),
('TNV17','TK_U17','HC01','Bùi Quang Dũng','048095000117','1995-11-11','Nam','0935111017','B+','Nguyễn Du, Hải Châu'),
('TNV18','TK_U18','NHS01','Mai Thanh Yến','048101000118','2001-02-14','Nữ','0702111018','O+','Võ Nguyên Giáp, Ngũ Hành Sơn'),
('TNV19','TK_U19','HC02','Dương Khắc Tiệp','048098000119','1998-05-09','Nam','0914111019','O+','Phan Châu Trinh, Hải Châu'),
('TNV20','TK_U20','LC01','Võ Trí Tài','048093000120','1993-08-30','Nam','0905111020','A-','Âu Cơ, Liên Chiểu'),
('TNV21','TK_U21','LC02','Đoàn Bảo Trâm','048197000121','1997-12-01','Nữ','0385111021','B+','Nguyễn Đình Trọng, Liên Chiểu'),
('TNV22','TK_U22','HC01','Hồ Viết Thái','048091000122','1991-04-15','Nam','0935111022','O+','Hùng Vương, Hải Châu'),
('TNV23','TK_U23','NHS01','Châu Tuấn Kiệt','048002000123','2002-09-25','Nam','0914111023','AB+','Non Nước, Ngũ Hành Sơn'),
('TNV24','TK_U24','HC02','Tôn Nữ Quỳnh','048196000124','1996-06-18','Nữ','0905111024','O+','Yên Bái, Hải Châu'),
('TNV25','TK_U25','LC01','Lâm Chí Khang','048089000125','1989-10-10','Nam','0702111025','A+','Bàu Mạc, Liên Chiểu'),
('TNV26','TK_U26','LC02','Đinh Ngọc Diệp','048100000126','2000-01-20','Nữ','0385111026','B+','Trung Nghĩa, Liên Chiểu'),
('TNV27','TK_U27','HC01','Thái Nhật Nam','048094000127','1994-03-08','Nam','0935111027','O+','Lê Duẩn, Hải Châu'),
('TNV28','TK_U28','NHS01','Kiều Thanh Hương','048192000128','1992-07-07','Nữ','0914111028','O-','Châu Thị Tế, Ngũ Hành Sơn'),
('TNV29','TK_U29','HC02','Trịnh Cẩm Tú','048198000129','1998-11-30','Nữ','0905111029','A+','Trần Quốc Toản, Hải Châu'),
('TNV30','TK_U30','LC01','Lưu Gia Huy','048003000130','2003-02-28','Nam','0702111030','AB+','Đồng Kè, Liên Chiểu');

-- Nhóm 2: 20 người ĐẾN HIẾN TRỰC TIẾP KHÔNG TÀI KHOẢN (maTaiKhoan = NULL)
INSERT INTO TINHNGUYENVIEN VALUES 
('TNV31',NULL,'LC02','Đồng Đức Hải','048095000131','1995-05-15','Nam','0385111031','O+','Phước Lý, Liên Chiểu'),
('TNV32',NULL,'HC01','Nhan Gia Hân','048101000132','2001-08-08','Nữ','0935111032','B+','Hải Phòng, Hải Châu'),
('TNV33',NULL,'NHS01','Mạc Văn Khoa','048090000133','1990-12-12','Nam','0914111033','O+','Bá Tùng, Ngũ Hành Sơn'),
('TNV34',NULL,'HC02','Cấn Đình Bảo','048097000134','1997-04-04','Nam','0905111034','A+','Nguyễn Thái Học, Hải Châu'),
('TNV35',NULL,'LC01','Lại Cẩm Nhung','048199000135','1999-09-09','Nữ','0702111035','AB+','Mẹ Suốt, Liên Chiểu'),
('TNV36',NULL,'LC02','Uông Tiến Đạt','048002000136','2002-11-11','Nam','0385111036','O+','Bùi Chát, Liên Chiểu'),
('TNV37',NULL,'HC01','Vi Cầm Quỳnh','048196000137','1996-02-20','Nữ','0935111037','B+','Cao Thắng, Hải Châu'),
('TNV38',NULL,'NHS01','Từ Bảo Long','048093000138','1993-06-06','Nam','0914111038','O+','Nam Kỳ Khởi Nghĩa, Ngũ Hành Sơn'),
('TNV39',NULL,'HC02','Bạch Diễm Khanh','048198000139','1998-10-10','Nữ','0905111039','A+','Hùng Vương, Hải Châu'),
('TNV40',NULL,'LC01','Khổng Tấn Đạt','048001000140','2001-01-01','Nam','0702111040','O+','Nguyễn Viết Xuân, Liên Chiểu'),
('TNV41',NULL,'LC02','Tạ Thị Ngọc','048194000141','1994-05-25','Nữ','0385111041','B+','Hoàng Văn Thái, Liên Chiểu'),
('TNV42',NULL,'HC01','Giang Hạo Thiên','048099000142','1999-08-18','Nam','0935111042','AB+','Nguyễn Thị Minh Khai, Hải Châu'),
('TNV43',NULL,'NHS01','Thân Vĩnh Trường','048092000143','1992-12-22','Nam','0914111043','O+','Trần Đại Nghĩa, Ngũ Hành Sơn'),
('TNV44',NULL,'HC02','Phương Tú Anh','048102000144','2002-03-30','Nữ','0905111044','A+','Hoàng Diệu, Hải Châu'),
('TNV45',NULL,'LC01','Quản Trọng Nhân','048096000145','1996-07-07','Nam','0702111045','O+','Tô Hiệu, Liên Chiểu'),
('TNV46',NULL,'LC02','Phí Hồng Đức','048091000146','1991-11-20','Nam','0385111046','B+','Tôn Đức Thắng, Liên Chiểu'),
('TNV47',NULL,'HC01','Đái Nhật Tâm','048000000147','2000-04-14','Nam','0935111047','O+','Ông Ích Khiêm, Hải Châu'),
('TNV48',NULL,'NHS01','Kiều Thủy Tiên','048195000148','1995-09-28','Nữ','0914111048','A-','Nguyễn Khắc Viện, Ngũ Hành Sơn'),
('TNV49',NULL,'HC02','Thạch Vĩnh Khang','048098000149','1998-02-02','Nam','0905111049','AB+','Thái Phiên, Hải Châu'),
('TNV50',NULL,'LC01','Hứa Kim Tuyến','048103000150','2003-10-15','Nữ','0702111050','O+','Lạc Long Quân, Liên Chiểu');

-- =============================================================
-- 5. CHIẾN DỊCH HIẾN MÁU
-- =============================================================
INSERT INTO CHIENDICHHIENMAU VALUES 
('CD_XH26','DD06','NV01','Lễ hội Xuân Hồng UTE 2026','2026-02-10 07:00','2026-02-12 17:00',500,'Đã kết thúc','QR_XH26'),
('CD_CN26','DD07','NV02','Chủ Nhật Đỏ Đại học Đông Á','2026-03-15 07:00','2026-03-15 11:30',300,'Đã phê duyệt','QR_CN26'),
('CD_TX26','DD05','NV15','Hiến máu thường xuyên Chữ Thập Đỏ','2026-05-01 07:00','2026-05-31 17:00',200,'Đang diễn ra','QR_TX26');

-- =============================================================
-- 6. ĐƠN ĐĂNG KÝ (3 LUỒNG HOẠT ĐỘNG RÕ RÀNG)
-- =============================================================

-- Trường hợp 1: Đăng ký trước qua mạng (maNhanVien = NULL). Gồm 20 TNV đầu tiên.
INSERT INTO DONDANGKY VALUES 
('D01','TNV01','CD_XH26',NULL,'QR_01','2026-02-05','Đã hiến'), 
('D02','TNV02','CD_XH26',NULL,'QR_02','2026-02-05','Đã hiến'),
('D03','TNV03','CD_XH26',NULL,'QR_03','2026-02-05','Đã hiến'), 
('D04','TNV04','CD_XH26',NULL,'QR_04','2026-02-05','Đã hiến'),
('D05','TNV05','CD_XH26',NULL,'QR_05','2026-02-05','Đã hiến'), 
('D06','TNV06','CD_XH26',NULL,'QR_06','2026-02-05','Đã hiến'),
('D07','TNV07','CD_XH26',NULL,'QR_07','2026-02-05','Đã hiến'), 
('D08','TNV08','CD_XH26',NULL,'QR_08','2026-02-05','Đã hiến'),
('D09','TNV09','CD_XH26',NULL,'QR_09','2026-02-05','Đã hiến'), 
('D10','TNV10','CD_XH26',NULL,'QR_10','2026-02-05','Đã hiến'),
('D11','TNV11','CD_XH26',NULL,'QR_11','2026-02-05','Đã hiến'), 
('D12','TNV12','CD_XH26',NULL,'QR_12','2026-02-05','Đã hiến'),
('D13','TNV13','CD_XH26',NULL,'QR_13','2026-02-05','Đã hiến'), 
('D14','TNV14','CD_XH26',NULL,'QR_14','2026-02-05','Đã hiến'),
('D15','TNV15','CD_XH26',NULL,'QR_15','2026-02-05','Chưa hiến'), 
('D16','TNV16','CD_XH26',NULL,'QR_16','2026-02-05','Đã hiến'),
('D17','TNV17','CD_XH26',NULL,'QR_17','2026-02-05','Đã hiến'), 
('D18','TNV18','CD_XH26',NULL,'QR_18','2026-02-05','Đã hiến'),
('D19','TNV19','CD_XH26',NULL,'QR_19','2026-02-05','Đã hiến'), 
('D20','TNV20','CD_XH26',NULL,'QR_20','2026-02-05','Đã hiến');

-- Trường hợp 2: Không có tài khoản, đến hiến trực tiếp (NV03, NV04 nhập giúp).
INSERT INTO DONDANGKY VALUES 
('D21','TNV31','CD_XH26','NV03','QR_21','2026-02-10','Đã hiến'), 
('D22','TNV32','CD_XH26','NV04','QR_22','2026-02-10','Đã hiến'),
('D23','TNV33','CD_XH26','NV03','QR_23','2026-02-10','Đã hiến'), 
('D24','TNV34','CD_XH26','NV04','QR_24','2026-02-10','Đã hiến'),
('D25','TNV35','CD_XH26','NV03','QR_25','2026-02-10','Đã hiến'), 
('D26','TNV36','CD_XH26','NV04','QR_26','2026-02-10','Đã hiến'),
('D27','TNV37','CD_XH26','NV03','QR_27','2026-02-10','Đã hiến'), 
('D28','TNV38','CD_XH26','NV04','QR_28','2026-02-10','Chưa hiến'),
('D29','TNV39','CD_XH26','NV03','QR_29','2026-02-10','Đã hiến'), 
('D30','TNV40','CD_XH26','NV04','QR_30','2026-02-10','Đã hiến'),
('D31','TNV41','CD_XH26','NV03','QR_31','2026-02-10','Đã hiến'), 
('D32','TNV42','CD_XH26','NV04','QR_32','2026-02-10','Đã hiến'),
('D33','TNV43','CD_XH26','NV03','QR_33','2026-02-10','Đã hiến'), 
('D34','TNV44','CD_XH26','NV04','QR_34','2026-02-10','Đã hiến'),
('D35','TNV45','CD_XH26','NV03','QR_35','2026-02-10','Đã hiến'), 
('D36','TNV46','CD_XH26','NV04','QR_36','2026-02-10','Đã hiến'),
('D37','TNV47','CD_XH26','NV03','QR_37','2026-02-10','Đã hiến'), 
('D38','TNV48','CD_XH26','NV04','QR_38','2026-02-10','Đã hiến'),
('D39','TNV49','CD_XH26','NV03','QR_39','2026-02-10','Đã hiến'), 
('D40','TNV50','CD_XH26','NV04','QR_40','2026-02-10','Đã hiến');

-- Trường hợp 3: Đã có tài khoản nhưng đến trực tiếp nhờ NV03, NV04 hỗ trợ nhập.
INSERT INTO DONDANGKY VALUES 
('D41','TNV21','CD_XH26','NV03','QR_41','2026-02-10','Đã hiến'), 
('D42','TNV22','CD_XH26','NV04','QR_42','2026-02-10','Đã hiến'),
('D43','TNV23','CD_XH26','NV03','QR_43','2026-02-10','Đã hiến'), 
('D44','TNV24','CD_XH26','NV04','QR_44','2026-02-10','Đã hiến'),
('D45','TNV25','CD_XH26','NV03','QR_45','2026-02-10','Chưa hiến'), 
('D46','TNV26','CD_XH26','NV04','QR_46','2026-02-10','Đã hiến'),
('D47','TNV27','CD_XH26','NV03','QR_47','2026-02-10','Đã hiến'), 
('D48','TNV28','CD_XH26','NV04','QR_48','2026-02-10','Đã hiến'),
('D49','TNV29','CD_XH26','NV03','QR_49','2026-02-10','Đã hiến'), 
('D50','TNV30','CD_XH26','NV04','QR_50','2026-02-10','Đã hiến');

-- =============================================================
-- 7. ĐỒNG BỘ 100%: HỒ SƠ SỨC KHỎE & KHÁM LÂM SÀNG
-- (D15 Sốt, D28 Xăm mình, D45 Thiếu cân bị đánh rớt)
-- =============================================================
INSERT INTO HOSOSUCKHOE VALUES 
('HS01','D01','Không','Huyết áp bình thường'), 
('HS02','D02','Không','Ngủ 8 tiếng'), 
('HS03','D03','Không','Đã ăn sáng'), 
('HS04','D04','Không','Khỏe mạnh'), 
('HS05','D05','Không','Tốt'),
('HS06','D06','Không','Tốt'), 
('HS07','D07','Không','Đã uống đủ nước'), 
('HS08','D08','Không','Tốt'), 
('HS09','D09','Không','Tốt'), 
('HS10','D10','Không','Tốt'),
('HS11','D11','Không','Tốt'), 
('HS12','D12','Không','Tốt'), 
('HS13','D13','Không','Tốt'), 
('HS14','D14','Không','Tốt'), 
('HS15','D15','Sốt','Viêm họng 2 ngày nay'),
('HS16','D16','Không','Tốt'), 
('HS17','D17','Không','Tốt'), 
('HS18','D18','Không','Tốt'), 
('HS19','D19','Không','Tốt'), 
('HS20','D20','Không','Tốt'),
('HS21','D21','Không','Tốt'), 
('HS22','D22','Không','Tốt'), 
('HS23','D23','Không','Tốt'), 
('HS24','D24','Không','Tốt'), 
('HS25','D25','Không','Tốt'),
('HS26','D26','Không','Tốt'), 
('HS27','D27','Không','Tốt'), 
('HS28','D28','Không','Mới xăm hình tháng trước'), 
('HS29','D29','Không','Tốt'), 
('HS30','D30','Không','Tốt'),
('HS31','D31','Không','Tốt'), 
('HS32','D32','Không','Tốt'), 
('HS33','D33','Không','Tốt'), 
('HS34','D34','Không','Tốt'), 
('HS35','D35','Không','Tốt'),
('HS36','D36','Không','Tốt'), 
('HS37','D37','Không','Tốt'), 
('HS38','D38','Không','Tốt'), 
('HS39','D39','Không','Tốt'), 
('HS40','D40','Không','Tốt'),
('HS41','D41','Không','Tốt'), 
('HS42','D42','Không','Tốt'), 
('HS43','D43','Không','Tốt'), 
('HS44','D44','Không','Tốt'), 
('HS45','D45','Không','Nhẹ cân, gầy'),
('HS46','D46','Không','Tốt'), 
('HS47','D47','Không','Tốt'), 
('HS48','D48','Không','Tốt'), 
('HS49','D49','Không','Tốt'), 
('HS50','D50','Không','Tốt');

INSERT INTO KETQUALAMSANG VALUES 
('KQ01','D01','NV03','120/80',75,65,36.5,1,NULL), 
('KQ02','D02','NV04','115/75',80,55,36.6,1,NULL), 
('KQ03','D03','NV03','125/85',72,70,36.5,1,NULL),
('KQ04','D04','NV04','110/70',82,60,36.7,1,NULL), 
('KQ05','D05','NV03','122/80',76,58,36.5,1,NULL), 
('KQ06','D06','NV04','118/78',79,52,36.6,1,NULL),
('KQ07','D07','NV03','130/85',74,75,36.5,1,NULL), 
('KQ08','D08','NV04','125/80',81,62,36.6,1,NULL), 
('KQ09','D09','NV03','115/75',77,54,36.7,1,NULL),
('KQ10','D10','NV04','120/80',75,68,36.5,1,NULL), 
('KQ11','D11','NV03','122/82',78,70,36.6,1,NULL), 
('KQ12','D12','NV04','110/70',85,50,36.5,1,NULL),
('KQ13','D13','NV03','125/85',72,72,36.7,1,NULL), 
('KQ14','D14','NV04','115/75',80,60,36.6,1,NULL), 
('KQ15','D15','NV03','140/90',95,65,38.0,0,'Sốt trên 37.5 độ'),
('KQ16','D16','NV04','120/80',75,68,36.5,1,NULL), 
('KQ17','D17','NV03','118/78',78,55,36.6,1,NULL), 
('KQ18','D18','NV04','125/85',72,70,36.5,1,NULL),
('KQ19','D19','NV03','110/70',82,60,36.7,1,NULL), 
('KQ20','D20','NV04','122/80',76,58,36.5,1,NULL), 
('KQ21','D21','NV03','118/78',79,52,36.6,1,NULL),
('KQ22','D22','NV04','130/85',74,75,36.5,1,NULL), 
('KQ23','D23','NV03','125/80',81,62,36.6,1,NULL), 
('KQ24','D24','NV04','115/75',77,54,36.7,1,NULL),
('KQ25','D25','NV03','120/80',75,68,36.5,1,NULL), 
('KQ26','D26','NV04','122/82',78,70,36.6,1,NULL), 
('KQ27','D27','NV03','110/70',85,50,36.5,1,NULL),
('KQ28','D28','NV04','125/85',72,72,36.7,0,'Xăm mình dưới 6 tháng'), 
('KQ29','D29','NV03','115/75',80,60,36.6,1,NULL), 
('KQ30','D30','NV04','120/80',75,68,36.5,1,NULL),
('KQ31','D31','NV03','115/75',78,55,36.6,1,NULL), 
('KQ32','D32','NV04','125/85',72,70,36.5,1,NULL), 
('KQ33','D33','NV03','110/70',82,60,36.7,1,NULL),
('KQ34','D34','NV04','122/80',76,58,36.5,1,NULL), 
('KQ35','D35','NV03','118/78',79,52,36.6,1,NULL), 
('KQ36','D36','NV04','130/85',74,75,36.5,1,NULL),
('KQ37','D37','NV03','125/80',81,62,36.6,1,NULL), 
('KQ38','D38','NV04','115/75',77,54,36.7,1,NULL), 
('KQ39','D39','NV03','120/80',75,68,36.5,1,NULL),
('KQ40','D40','NV04','122/82',78,70,36.6,1,NULL), 
('KQ41','D41','NV03','110/70',85,50,36.5,1,NULL), 
('KQ42','D42','NV04','120/80',75,68,36.5,1,NULL),
('KQ43','D43','NV03','115/75',80,60,36.6,1,NULL), 
('KQ44','D44','NV04','120/80',75,68,36.5,1,NULL), 
('KQ45','D45','NV03','100/60',90,43,36.7,0,'Cân nặng dưới 40kg'),
('KQ46','D46','NV04','125/85',72,70,36.5,1,NULL), 
('KQ47','D47','NV03','110/70',82,60,36.7,1,NULL), 
('KQ48','D48','NV04','122/80',76,58,36.5,1,NULL),
('KQ49','D49','NV03','118/78',79,52,36.6,1,NULL), 
('KQ50','D50','NV04','130/85',74,75,36.5,1,NULL);

-- =============================================================
-- 8. THU NHẬN, XÉT NGHIỆM VÀ KHO MÁU 
-- Đa dạng trạng thái (Nhập kho, Chờ xét nghiệm, Đã xuất, Hủy)
-- =============================================================
-- =============================================================
-- 8. THU NHẬN, XÉT NGHIỆM VÀ KHO MÁU 
-- Đa dạng trạng thái (Nhập kho, Chờ xét nghiệm, Đã xuất, Hủy)
-- =============================================================

-- Xóa dữ liệu rác (nếu có) do câu lệnh lỗi vừa rồi gây ra


-- Phân tách mã kho theo từng nhóm máu chuyên biệt
INSERT INTO KHOMAU VALUES 
('DD01_O+', 'O+', 150, 50), 
('DD01_A+', 'A+', 30, 40), 
('DD01_B+', 'B+', 60, 30), 
('DD01_AB+', 'AB+', 10, 15),
('DD01_O-', 'O-', 5, 10), 
('DD01_A-', 'A-', 5, 10), 
('DD01_B-', 'B-', 5, 10), 
('DD01_AB-', 'AB-', 2, 5);

-- 47 TÚI MÁU (Túi máu của TNV nhóm nào sẽ được lưu vào kho nhóm đó)
INSERT INTO TUIMAU VALUES 
('TM01','D01','NV06','DD01_B+',250,'2026-02-10 07:15','Nhập kho',4.5), 
('TM02','D02','NV07','DD01_O+',350,'2026-02-10 07:18','Nhập kho',4.2),
('TM03','D03','NV08','DD01_A+',250,'2026-02-10 07:20','Nhập kho',4.5), 
('TM04','D04','NV06','DD01_O+',350,'2026-02-10 07:23','Nhập kho',4.2),
('TM05','D05','NV07','DD01_AB+',250,'2026-02-10 07:25','Nhập kho',4.5), 
('TM06','D06','NV08','DD01_O-',350,'2026-02-10 07:28','Nhập kho',4.2),
('TM07','D07','NV06','DD01_O+',250,'2026-02-10 07:30','Nhập kho',4.5), 
('TM08','D08','NV07','DD01_A-',350,'2026-02-10 07:33','Nhập kho',4.2),
('TM09','D09','NV08','DD01_B+',250,'2026-02-10 07:35','Nhập kho',4.5), 
('TM10','D10','NV06','DD01_AB-',350,'2026-02-10 07:38','Hủy',4.2),
('TM11','D11','NV07','DD01_A+',250,'2026-02-10 07:40','Nhập kho',4.5), 
('TM12','D12','NV08','DD01_B+',350,'2026-02-10 07:43','Nhập kho',4.2),
('TM13','D13','NV06','DD01_O+',250,'2026-02-10 07:45','Nhập kho',4.5), 
('TM14','D14','NV07','DD01_O+',350,'2026-02-10 07:48','Nhập kho',4.2),
-- SKIPPED TM15
('TM16','D16','NV08','DD01_A+',350,'2026-02-10 07:53','Nhập kho',4.2), 
('TM17','D17','NV06','DD01_B+',250,'2026-02-10 07:55','Nhập kho',4.5),
('TM18','D18','NV07','DD01_O+',350,'2026-02-10 07:58','Nhập kho',4.2), 
('TM19','D19','NV08','DD01_O+',250,'2026-02-10 08:00','Nhập kho',4.5),
('TM20','D20','NV06','DD01_A-',350,'2026-02-10 08:03','Đã xuất',4.2),
('TM21','D21','NV07','DD01_B+',250,'2026-02-10 08:05','Nhập kho',4.5), 
('TM22','D22','NV08','DD01_O+',350,'2026-02-10 08:08','Nhập kho',4.2),
('TM23','D23','NV06','DD01_AB+',250,'2026-02-10 08:10','Nhập kho',4.5), 
('TM24','D24','NV07','DD01_O+',350,'2026-02-10 08:13','Nhập kho',4.2),
('TM25','D25','NV08','DD01_A+',250,'2026-02-10 08:15','Nhập kho',4.5), 
('TM26','D26','NV06','DD01_B+',350,'2026-02-10 08:18','Nhập kho',4.2),
('TM27','D27','NV07','DD01_O+',250,'2026-02-10 08:20','Nhập kho',4.5), 
-- SKIPPED TM28
('TM29','D29','NV08','DD01_A+',250,'2026-02-10 08:25','Nhập kho',4.5), 
('TM30','D30','NV06','DD01_AB+',350,'2026-02-10 08:28','Nhập kho',4.2),
('TM31','D31','NV07','DD01_O+',250,'2026-02-10 08:30','Nhập kho',4.5), 
('TM32','D32','NV08','DD01_B+',350,'2026-02-10 08:33','Nhập kho',4.2),
('TM33','D33','NV06','DD01_O+',250,'2026-02-10 08:35','Nhập kho',4.5), 
('TM34','D34','NV07','DD01_A+',350,'2026-02-10 08:38','Nhập kho',4.2),
('TM35','D35','NV08','DD01_AB+',250,'2026-02-10 08:40','Nhập kho',4.5), 
('TM36','D36','NV06','DD01_O+',350,'2026-02-10 08:43','Nhập kho',4.2),
('TM37','D37','NV07','DD01_B+',250,'2026-02-10 08:45','Nhập kho',4.5), 
('TM38','D38','NV08','DD01_O+',350,'2026-02-10 08:48','Nhập kho',4.2),
('TM39','D39','NV06','DD01_A+',250,'2026-02-10 08:50','Nhập kho',4.5), 
('TM40','D40','NV07','DD01_O+',350,'2026-02-10 08:53','Nhập kho',4.2),
('TM41','D41','NV08','DD01_B+',250,'2026-02-10 08:55','Hủy',4.5), 
('TM42','D42','NV06','DD01_AB+',350,'2026-02-10 08:58','Nhập kho',4.2), 
('TM43','D43','NV07','DD01_O+',250,'2026-02-10 09:00','Nhập kho',4.5),
('TM44','D44','NV08','DD01_A+',350,'2026-02-10 09:03','Nhập kho',4.2),
-- SKIPPED TM45
('TM46','D46','NV06','DD01_B+',350,'2026-02-10 09:08','Nhập kho',4.2), 
('TM47','D47','NV07','DD01_O+',250,'2026-02-10 09:10','Nhập kho',4.5),
('TM48','D48','NV08','DD01_A-',350,'2026-02-10 09:13','Nhập kho',4.2), 
('TM49','D49','NV06','DD01_AB+',250,'2026-02-10 09:15','Chờ xét nghiệm',4.5),
('TM50','D50','NV07','DD01_O+',350,'2026-02-10 09:18','Chờ xét nghiệm',4.2);

INSERT INTO KETQUAXETNGHIEM VALUES 
('XN01','TM01','NV09','B+','Âm tính. Đạt.'), 
('XN02','TM02','NV10','O+','Âm tính. Đạt.'),
('XN03','TM03','NV11','A+','Âm tính. Đạt.'), 
('XN04','TM04','NV09','O+','Âm tính. Đạt.'),
('XN05','TM05','NV10','AB+','Âm tính. Đạt.'), 
('XN06','TM06','NV11','O-','Âm tính. Đạt.'),
('XN07','TM07','NV09','O+','Âm tính. Đạt.'), 
('XN08','TM08','NV10','A-','Âm tính. Đạt.'),
('XN09','TM09','NV11','B+','Âm tính. Đạt.'), 
('XN10','TM10','NV09','AB-','Dương tính Viêm gan B. Hủy túi máu.'),
('XN11','TM11','NV10','A+','Âm tính. Đạt.'), 
('XN12','TM12','NV11','B+','Âm tính. Đạt.'),
('XN13','TM13','NV09','O+','Âm tính. Đạt.'), 
('XN14','TM14','NV10','O+','Âm tính. Đạt.'),
('XN16','TM16','NV11','A+','Âm tính. Đạt.'), 
('XN17','TM17','NV09','B+','Âm tính. Đạt.'),
('XN18','TM18','NV10','O+','Âm tính. Đạt.'), 
('XN19','TM19','NV11','O+','Âm tính. Đạt.'),
('XN20','TM20','NV09','A-','Âm tính. Phân phối gấp cho ca mổ.'), 
('XN21','TM21','NV10','B+','Âm tính. Đạt.'),
('XN22','TM22','NV11','O+','Âm tính. Đạt.'), 
('XN23','TM23','NV09','AB+','Âm tính. Đạt.'),
('XN24','TM24','NV10','O+','Âm tính. Đạt.'), 
('XN25','TM25','NV11','A+','Âm tính. Đạt.'),
('XN26','TM26','NV09','B+','Âm tính. Đạt.'), 
('XN27','TM27','NV10','O+','Âm tính. Đạt.'),
('XN29','TM29','NV11','A+','Âm tính. Đạt.'), 
('XN30','TM30','NV09','AB+','Âm tính. Đạt.'),
('XN31','TM31','NV10','O+','Âm tính. Đạt.'), 
('XN32','TM32','NV11','B+','Âm tính. Đạt.'),
('XN33','TM33','NV09','O+','Âm tính. Đạt.'), 
('XN34','TM34','NV10','A+','Âm tính. Đạt.'),
('XN35','TM35','NV11','AB+','Âm tính. Đạt.'), 
('XN36','TM36','NV09','O+','Âm tính. Đạt.'),
('XN37','TM37','NV10','B+','Âm tính. Đạt.'), 
('XN38','TM38','NV11','O+','Âm tính. Đạt.'),
('XN39','TM39','NV09','A+','Âm tính. Đạt.'), 
('XN40','TM40','NV10','O+','Âm tính. Đạt.'),
('XN41','TM41','NV11','B+','Dương tính HIV. Hủy túi máu báo cáo khẩn.'), 
('XN42','TM42','NV09','O+','Âm tính. Đạt.'),
('XN43','TM43','NV10','O+','Âm tính. Đạt.'), 
('XN44','TM44','NV11','A+','Âm tính. Đạt.'),
('XN46','TM46','NV09','B+','Âm tính. Đạt.'), 
('XN47','TM47','NV10','O+','Âm tính. Đạt.'),
('XN48','TM48','NV11','A-','Âm tính. Đạt.'), 
('XN49','TM49','NV09',NULL,'Đang quay ly tâm.'),  -- Đã sửa 'Chưa rõ' thành NULL
('XN50','TM50','NV10',NULL,'Chờ kết quả PCR.');    -- Đã sửa 'Chưa rõ' thành NULL

-- =============================================================
-- 9. CHỨNG NHẬN, NHẬP XUẤT, TƯƠNG TÁC
-- =============================================================
INSERT INTO CHUNGNHAN VALUES 
('CN01','D01','NV14','/pdf/CN01.pdf','2026-02-12'), 
('CN02','D02','NV14','/pdf/CN02.pdf','2026-02-12'),
('CN03','D03','NV14','/pdf/CN03.pdf','2026-02-12'), 
('CN04','D04','NV14','/pdf/CN04.pdf','2026-02-12'),
('CN05','D05','NV14','/pdf/CN05.pdf','2026-02-12'), 
('CN06','D06','NV14','/pdf/CN06.pdf','2026-02-12'),
('CN07','D07','NV14','/pdf/CN07.pdf','2026-02-12'), 
('CN08','D08','NV14','/pdf/CN08.pdf','2026-02-12'),
('CN09','D09','NV14','/pdf/CN09.pdf','2026-02-12'), 
('CN10','D10','NV14','/pdf/CN10.pdf','2026-02-12'),
('CN11','D11','NV14','/pdf/CN11.pdf','2026-02-12'), 
('CN12','D12','NV14','/pdf/CN12.pdf','2026-02-12'),
('CN13','D13','NV14','/pdf/CN13.pdf','2026-02-12'), 
('CN14','D14','NV14','/pdf/CN14.pdf','2026-02-12'),
('CN16','D16','NV14','/pdf/CN16.pdf','2026-02-12'), 
('CN17','D17','NV14','/pdf/CN17.pdf','2026-02-12'),
('CN18','D18','NV14','/pdf/CN18.pdf','2026-02-12'), 
('CN19','D19','NV14','/pdf/CN19.pdf','2026-02-12'),
('CN20','D20','NV14','/pdf/CN20.pdf','2026-02-12'), 
('CN21','D21','NV14','/pdf/CN21.pdf','2026-02-12'),
('CN22','D22','NV14','/pdf/CN22.pdf','2026-02-12'), 
('CN23','D23','NV14','/pdf/CN23.pdf','2026-02-12'),
('CN24','D24','NV14','/pdf/CN24.pdf','2026-02-12'), 
('CN25','D25','NV14','/pdf/CN25.pdf','2026-02-12'),
('CN26','D26','NV14','/pdf/CN26.pdf','2026-02-12'), 
('CN27','D27','NV14','/pdf/CN27.pdf','2026-02-12'),
('CN29','D29','NV14','/pdf/CN29.pdf','2026-02-12'), 
('CN30','D30','NV14','/pdf/CN30.pdf','2026-02-12'),
('CN31','D31','NV14','/pdf/CN31.pdf','2026-02-12'), 
('CN32','D32','NV14','/pdf/CN32.pdf','2026-02-12'),
('CN33','D33','NV14','/pdf/CN33.pdf','2026-02-12'), 
('CN34','D34','NV14','/pdf/CN34.pdf','2026-02-12'),
('CN35','D35','NV14','/pdf/CN35.pdf','2026-02-12'), 
('CN36','D36','NV14','/pdf/CN36.pdf','2026-02-12'),
('CN37','D37','NV14','/pdf/CN37.pdf','2026-02-12'), 
('CN38','D38','NV14','/pdf/CN38.pdf','2026-02-12'),
('CN39','D39','NV14','/pdf/CN39.pdf','2026-02-12'), 
('CN40','D40','NV14','/pdf/CN40.pdf','2026-02-12'),
('CN41','D41','NV14','/pdf/CN41.pdf','2026-02-12'), 
('CN42','D42','NV14','/pdf/CN42.pdf','2026-02-12'),
('CN43','D43','NV14','/pdf/CN43.pdf','2026-02-12'), 
('CN44','D44','NV14','/pdf/CN44.pdf','2026-02-12'),
('CN46','D46','NV14','/pdf/CN46.pdf','2026-02-12'), 
('CN47','D47','NV14','/pdf/CN47.pdf','2026-02-12'),
('CN48','D48','NV14','/pdf/CN48.pdf','2026-02-12'), 
('CN49','D49','NV14','/pdf/CN49.pdf','2026-02-12'),
('CN50','D50','NV14','/pdf/CN50.pdf','2026-02-12');

INSERT INTO PHIEUNHAPXUAT VALUES 
('PN01','NV12','Nhập kho','2026-02-10'), 
('PN02','NV13','Nhập kho','2026-02-11'), 
('PX01','NV12','Xuất kho','2026-02-12');

INSERT INTO CHITIETNHAPXUAT VALUES 
('PN01','TM01'), 
('PN01','TM02'), 
('PN01','TM03'), 
('PN01','TM04'), 
('PN01','TM05'),
('PN02','TM06'), 
('PN02','TM07'), 
('PN02','TM08'), 
('PX01','TM20');


INSERT INTO TINTUC VALUES 
('TT01','NV14','Tổng kết Lễ hội Xuân Hồng UTE 2026','Đà Nẵng đã thu nhận thành công 500 đơn vị máu...','img1.jpg','2026-02-13','Đã thêm'),
('TT02','NV14','Lời kêu gọi hiến máu nhóm O và A','Hiện tại kho máu đang cạn kiệt nhóm O và A...','img2.jpg','2026-03-01','Đã thêm');

INSERT INTO THONGBAO VALUES 
-- Thông báo nội bộ giữa các nhân viên y tế (Điều phối công việc)
('TB01','TK_NV14','TK_NV03','[Hệ thống] Nhắc nhở ca trực Sàng lọc tại UTE bắt đầu lúc 07:00 ngày 10/02.','2026-02-09','Đã đọc'),
('TB02','TK_NV12','TK_NV14','[Cảnh báo tự động] Kho máu O- đang dưới ngưỡng an toàn (Chỉ còn 5 túi). Cần lập chiến dịch huy động khẩn cấp.','2026-02-20','Chưa đọc');

INSERT INTO TINNHAN VALUES 
-- Kịch bản 1: Hỏi thăm sức khỏe tự động sau 24h hiến máu (Hệ thống gửi TNV)
('MSG01','TK_NV14','TK_U01','[Chăm sóc sức khỏe] Cảm ơn bạn đã tham gia hiến máu hôm qua. Vui lòng uống nhiều nước, hạn chế mang vác nặng và nghỉ ngơi đầy đủ. Nếu có dấu hiệu chóng mặt, hãy liên hệ ngay cơ sở y tế gần nhất.','2026-02-11', 1),
('MSG02','TK_NV14','TK_U02','[Chăm sóc sức khỏe] Cảm ơn bạn đã tham gia hiến máu hôm qua. Vui lòng uống nhiều nước, hạn chế mang vác nặng...','2026-02-11', 1),

-- Kịch bản 2: Báo kết quả xét nghiệm và cấp giấy chứng nhận
('MSG03','TK_NV14','TK_U03','[Kết quả hiến máu] Túi máu của bạn đã đạt chuẩn an toàn. Giấy chứng nhận điện tử đã được cập nhật vào ứng dụng. Trân trọng cảm ơn!','2026-02-14', 1),
('MSG04','TK_NV14','TK_U10','[Quan trọng] Mẫu máu của bạn phát hiện có kháng thể bất thường. Giấy chứng nhận đã được cấp để tri ân nghĩa cử của bạn. Vui lòng đến Bệnh viện Đà Nẵng để được tư vấn sức khỏe miễn phí.','2026-02-14', 1),

-- Kịch bản 3: Huy động máu khẩn cấp theo nhóm máu (Chỉ gửi cho người có nhóm O+)
('MSG05','TK_NV14','TK_U07','[Khẩn cấp] Kho máu BVDN đang thiếu hụt nghiêm trọng nhóm máu O+. Bệnh nhân cấp cứu đang rất cần bạn. Hãy đến điểm hiến máu gần nhất!','2026-03-01', 1);




-- =============================================================
-- CÁC CÂU LỆNH TRUY VẤN (SELECT) KIỂM TRA HỆ THỐNG
-- Phục vụ test logic và lấy số liệu làm báo cáo cho Nhóm 20
-- =============================================================


-- -------------------------------------------------------------
-- 1. KIỂM TRA TỆP TÌNH NGUYỆN VIÊN (AI DÙNG APP, AI WALK-IN)
-- -------------------------------------------------------------
SELECT 
    tnv.maTNV, 
    tnv.hoTen AS 'Tên Tình Nguyện Viên', 
    tnv.nhomMau AS 'Nhóm Máu', 
    tk.email AS 'Email Đăng Nhập',
    CASE 
        WHEN tnv.maTaiKhoan IS NULL THEN 'Hiến trực tiếp (Không có App)'
        ELSE 'Đã đăng ký App' 
    END AS 'Nhóm Người Dùng'
FROM TINHNGUYENVIEN tnv
LEFT JOIN TAIKHOAN tk ON tnv.maTaiKhoan = tk.maTaiKhoan;


-- -------------------------------------------------------------
-- 2. BÁO CÁO THỐNG KÊ 3 LUỒNG ĐĂNG KÝ TRONG CHIẾN DỊCH
-- (Logic cực kỳ quan trọng để bảo vệ trước Hội đồng)
-- -------------------------------------------------------------
SELECT 
    d.maDon AS 'Mã Đơn', 
    tnv.hoTen AS 'Người Hiến', 
    c.tenChienDich AS 'Chiến Dịch',
    CASE 
        WHEN d.maNhanVien IS NULL THEN '1. Đăng ký Online (TNV tự tạo)'
        WHEN d.maNhanVien IS NOT NULL AND tnv.maTaiKhoan IS NULL THEN '2. Trực tiếp - Không tài khoản (NV nhập mới)'
        WHEN d.maNhanVien IS NOT NULL AND tnv.maTaiKhoan IS NOT NULL THEN '3. Trực tiếp - Có tài khoản (NV quét mã)'
    END AS 'Luồng Nghiệp Vụ',
    IFNULL(nv.hoTen, 'Hệ thống tự động') AS 'Nhân Viên Tiếp Nhận'
FROM DONDANGKY d
JOIN TINHNGUYENVIEN tnv ON d.maTNV = tnv.maTNV
JOIN CHIENDICHHIENMAU c ON d.maChienDich = c.maChienDich
LEFT JOIN NHANVIEN nv ON d.maNhanVien = nv.maNhanVien
ORDER BY d.maDon;


-- -------------------------------------------------------------
-- 3. TRUY VẾT HÀNH TRÌNH TÚI MÁU (Từ khám đến kết quả)
-- Chứng minh hệ thống bắt được ca rớt lâm sàng & hủy túi máu
-- -------------------------------------------------------------
SELECT 
    d.maDon AS 'Mã Đơn', 
    tnv.hoTen AS 'Tên TNV', 
    IF(kq.ketQua = 1, 'Đạt', 'Rớt') AS 'Khám Lâm Sàng', 
    IFNULL(kq.lyDoTuChoi, 'Không') AS 'Lý do rớt',
    IFNULL(t.maTuiMau, 'Không có túi máu') AS 'Mã Túi Máu', 
    IFNULL(t.trangThai, 'N/A') AS 'Trạng Thái Túi',
    IFNULL(xn.moTa, 'Chưa xét nghiệm hoặc Không thu máu') AS 'Kết Quả Xét Nghiệm'
FROM DONDANGKY d
JOIN TINHNGUYENVIEN tnv ON d.maTNV = tnv.maTNV
LEFT JOIN KETQUALAMSANG kq ON d.maDon = kq.maDon
LEFT JOIN TUIMAU t ON d.maDon = t.maDon
LEFT JOIN KETQUAXETNGHIEM xn ON t.maTuiMau = xn.maTuiMau;


-- -------------------------------------------------------------
-- 4. BẢNG ĐIỀU KHIỂN (DASHBOARD): CẢNH BÁO KHO MÁU
-- Lấy ra những nhóm máu đang thiếu hụt để huy động
-- -------------------------------------------------------------
SELECT 
    maKho AS 'Mã Kho',
    nhomMau AS 'Nhóm Máu', 
    soLuongTon AS 'Tồn Kho Hiện Tại', 
    nguongAnToan AS 'Ngưỡng Cảnh Báo',
    IF(soLuongTon < nguongAnToan, '⚠️ BÁO ĐỘNG ĐỎ - CẦN HUY ĐỘNG', '✅ Mức an toàn') AS 'Tình Trạng Kho'
FROM KHOMAU;


-- -------------------------------------------------------------
-- 5. KIỂM TRA HỆ THỐNG GỬI TIN NHẮN TỰ ĐỘNG (AUTOMATION)
-- Xem hệ thống đã chăm sóc người hiến máu thành công chưa
-- -------------------------------------------------------------
SELECT 
    tn.maTinNhan AS 'Mã Tin',
    DATE_FORMAT(tn.thoiGian, '%d/%m/%Y %H:%i') AS 'Thời Gian Gửi',
    tk.email AS 'Gửi Đến',
    tn.noiDung AS 'Nội Dung Tin Nhắn',
    IF(tn.trangThai = 1, 'Đã gửi thành công', 'Lỗi hệ thống') AS 'Trạng Thái Gửi'
FROM TINNHAN tn
JOIN TAIKHOAN tk ON tn.maTaiKhoanNhan = tk.maTaiKhoan;
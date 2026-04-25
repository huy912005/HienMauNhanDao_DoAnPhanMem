-- =============================================================
-- ĐỒ ÁN: HỆ THỐNG QUẢN LÝ HIẾN MÁU NHÂN ĐẠO ĐÀ NẴNG 
-- SINH VIÊN THỰC HIỆN: NHÓM 20 
-- VĂN QUÝ VƯƠNG- PHẠM MINH HUY- LÊ VIỆT HƯNG- LÊ VĂN MẠNH
-- =============================================================

-- 1. Xóa database cũ nếu đã tồn tại để chạy lại từ đầu
DROP DATABASE IF EXISTS QuanLyHienMauNhanDaoDN;

-- 2. Tạo database mới với bảng mã hỗ trợ tiếng Việt
CREATE DATABASE QuanLyHienMauNhanDaoDN CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuanLyHienMauNhanDaoDN;

-- 3. Bảng Vai trò
CREATE TABLE VAITRO (
    maVaiTro CHAR(10) PRIMARY KEY,
    tenVaiTro NVARCHAR(50) NOT NULL
);

-- 4. Bảng Tài khoản (Hỗ trợ xác thực và phân quyền) 
CREATE TABLE TAIKHOAN (
    maTaiKhoan CHAR(10) PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    maVaiTro CHAR(10),
    trangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (maVaiTro) REFERENCES VAITRO(maVaiTro)
);

-- 5. Bảng Tình nguyện viên [cite: 359]
CREATE TABLE TINHNGUYENVIEN (
    maTNV CHAR(10) PRIMARY KEY,
    maTaiKhoan CHAR(10),
    hoTen NVARCHAR(100) NOT NULL,
    CCCD CHAR(12) UNIQUE NOT NULL,
    ngaySinh DATE,
    gioiTinh NVARCHAR(10),
    soDienThoai VARCHAR(15),
    nhomMau VARCHAR(5), -- Cập nhật sau khi có kết quả xét nghiệm chính xác [cite: 151, 152]
    diaChi NVARCHAR(255),
    FOREIGN KEY (maTaiKhoan) REFERENCES TAIKHOAN(maTaiKhoan)
);

-- 6. Bảng Chiến dịch hiến máu (Phục vụ chức năng Tìm kiếm) [cite: 398, 413]
CREATE TABLE CHIENDICHHIENMAU (
    maChienDich CHAR(10) PRIMARY KEY,
    tenChienDich NVARCHAR(255) NOT NULL,
    thoiGianBD DATETIME,
    thoiGianKT DATETIME,
    diaChi NVARCHAR(255),
    soLuongDuKien INT,
    maQR VARCHAR(255), -- Mã QR để TNV quét khi đến điểm hiến [cite: 145, 146]
    trangThai NVARCHAR(50) -- Chờ duyệt, Đang diễn ra, Kết thúc
);

-- 7. Bảng Đơn đăng ký (Phân biệt Online và Tại chỗ bằng maNhanVien) [cite: 376, 426]
CREATE TABLE DONDANGKY (
    maDon CHAR(10) PRIMARY KEY,
    maTNV CHAR(10),
    maChienDich CHAR(10),
    maNhanVien CHAR(10) DEFAULT NULL, -- NULL nếu TNV đăng ký Online; có ID nếu NVYT nhập tại chỗ
    thoiGianDangKy DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai NVARCHAR(50), -- Chờ khám, Đã khám, Đã hiến, Từ chối
    FOREIGN KEY (maTNV) REFERENCES TINHNGUYENVIEN(maTNV),
    FOREIGN KEY (maChienDich) REFERENCES CHIENDICHHIENMAU(maChienDich)
);

-- 8. Bảng Kết quả lâm sàng (Phục vụ chức năng Khám sàng lọc) [cite: 247, 471]
CREATE TABLE KETQUALAMSANG (
    maKQ CHAR(10) PRIMARY KEY,
    maDon CHAR(10),
    huyetAp VARCHAR(20),
    nhipTim INT,
    canNang DOUBLE,
    nhietDo DOUBLE,
    ketQua BOOLEAN, -- TRUE: Đủ điều kiện, FALSE: Từ chối [cite: 474]
    lyDoTuChoi NVARCHAR(255),
    FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon)
);

-- 9. Bảng Túi máu (Phục vụ chức năng Thu nhận đơn vị máu) [cite: 388, 478]
CREATE TABLE TUIMAU (
    maTuiMau CHAR(15) PRIMARY KEY,
    maDon CHAR(10),
    theTich INT, -- 250, 350, 450ml [cite: 180, 181, 182]
    thoiGianLayMau DATETIME,
    trangThai NVARCHAR(50), -- Chờ xét nghiệm, Đạt, Hủy, Đã xuất
    nhietDoVanChuyen DOUBLE, -- Cập nhật cho hiến máu lưu động [cite: 481]
    FOREIGN KEY (maDon) REFERENCES DONDANGKY(maDon)
);

-- 10. Bảng Kho máu (Phục vụ chức năng Thống kê tồn kho) [cite: 164, 500]
CREATE TABLE KHOMAU (
    nhomMau VARCHAR(10) PRIMARY KEY,
    soLuongTon INT DEFAULT 0,
    nguongAnToan INT DEFAULT 20 -- Dùng để trigger cảnh báo thiếu máu [cite: 196, 503]
);
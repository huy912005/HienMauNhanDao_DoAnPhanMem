/**
 * auth.js - Authentication & Role-Based Access Control
 * Hệ thống Quản lý Hiến máu Nhân đạo TP. Đà Nẵng
 */

const AUTH_KEY = 'hm_dn_auth';

// ============================================================
// Mock user database (simulated - no backend required)
// ============================================================
const MOCK_USERS = [
  { id: 'U001', email: 'admin@hieumau.dn.vn', password: 'Admin@123', role: 'admin', name: 'Nguyễn Quản Trị', department: 'Quản trị hệ thống', avatar: '' },
  { id: 'U002', email: 'yte@hieumau.dn.vn', password: 'Yte@123', role: 'nhan_vien_y_te', name: 'BS. Trần Thị Mai', department: 'Phòng Xét Nghiệm', avatar: '' },
  { id: 'U003', email: 'bacsi@hieumau.dn.vn', password: 'Bacsi@123', role: 'bac_si', name: 'TS.BS. Lê Văn Hùng', department: 'Khoa Huyết Học', avatar: '' },
  { id: 'U004', email: 'kho@hieumau.dn.vn', password: 'Kho@123', role: 'quan_ly_kho', name: 'Phạm Thị Lan', department: 'Kho Máu Trung Tâm', avatar: '' },
  { id: 'U005', email: 'tnv@gmail.com', password: 'Tnv@123', role: 'tinh_nguyen_vien', name: 'Nguyễn Văn Bình', department: 'Tình nguyện viên', avatar: '' },
];

// ============================================================
// Role Config - Menu items & permissions per role
// ============================================================
const ROLE_CONFIG = {
  admin: {
    label: 'Quản trị hệ thống',
    color: '#7c3aed',
    badgeColor: 'bg-purple-100 text-purple-700',
    icon: 'admin_panel_settings',
    menu: [
      { id: 'trang-chu', label: 'Bảng điều khiển', icon: 'dashboard', page: 'TrangChuAdmin.html' },
      { id: 'quan-ly-chien-dich', label: 'Quản lý chiến dịch', icon: 'campaign', page: 'QuanLyChienDich.html' },
      { id: 'thong-ke-ton-kho', label: 'Thống kê tồn kho', icon: 'bar_chart', page: 'ThongKeTonKho.html' },
      { id: 'quan-ly-tai-khoan', label: 'Quản lý tài khoản', icon: 'manage_accounts', page: 'QuanLyTaiKhoan.html' },
      { id: 'cap-giay-chung-nhan', label: 'Cấp chứng nhận', icon: 'workspace_premium', page: 'CapGiayChungNhan.html' },
    ]
  },
  nhan_vien_y_te: {
    label: 'Nhân viên Y tế',
    color: '#0891b2',
    badgeColor: 'bg-cyan-100 text-cyan-700',
    icon: 'medical_services',
    menu: [
      { id: 'kham-sang-loc', label: 'Khám sàng lọc', icon: 'fact_check', page: 'KhamSangLoc.html' },
      { id: 'cap-nhat-xet-nghiem', label: 'Cập nhật XN', icon: 'biotech', page: 'CapNhatXetNghiem.html' },
    ]
  },
  bac_si: {
    label: 'Bác sĩ',
    color: '#059669',
    badgeColor: 'bg-emerald-100 text-emerald-700',
    icon: 'stethoscope',
    menu: [
      { id: 'kham-sang-loc-bs', label: 'Khám sàng lọc', icon: 'clinical_notes', page: 'KhamSangLoc.html' },
      { id: 'xem-ho-so-benh-an', label: 'Hồ sơ bệnh án', icon: 'folder_shared', page: 'XemHoSoBenhAn.html' },
    ]
  },
  quan_ly_kho: {
    label: 'Quản lý Kho máu',
    color: '#d97706',
    badgeColor: 'bg-amber-100 text-amber-700',
    icon: 'inventory_2',
    menu: [
      { id: 'nhap-xuat-kho', label: 'Nhập / Xuất kho', icon: 'swap_horiz', page: 'QuanLyNhapXuatKho.html' },
      { id: 'thong-ke-ton-kho', label: 'Thống kê tồn kho', icon: 'bar_chart', page: 'ThongKeTonKho.html' },
    ]
  },
  tinh_nguyen_vien: {
    label: 'Tình nguyện viên',
    color: '#af101a',
    badgeColor: 'bg-red-100 text-red-700',
    icon: 'volunteer_activism',
    menu: [
      { id: 'trang-chu', label: 'Trang chủ', icon: 'home', page: 'TrangChu.html' },
      { id: 'tim-kiem-chien-dich', label: 'Tìm kiếm chiến dịch', icon: 'search', page: 'TimKiemChienDich.html' },
      { id: 'dang-ky-khai-bao', label: 'Đăng ký & Khai báo', icon: 'how_to_reg', page: 'DangKyVaKhaiBaoYTe.html' },
      { id: 'ho-so-lich-su', label: 'Hồ sơ & Lịch sử', icon: 'person', page: 'HoSoVaLichSu.html' },
    ]
  }
};

// ============================================================
// Auth Functions
// ============================================================
function login(email, password) {
  const user = MOCK_USERS.find(u => u.email === email && u.password === password);
  if (!user) return { success: false, message: 'Email hoặc mật khẩu không đúng.' };
  const session = { ...user };
  delete session.password;
  sessionStorage.setItem(AUTH_KEY, JSON.stringify(session));
  return { success: true, user: session };
}

function logout() {
  sessionStorage.removeItem(AUTH_KEY);
  window.location.href = '../DangNhap.html';
}

function getSession() {
  const raw = sessionStorage.getItem(AUTH_KEY);
  return raw ? JSON.parse(raw) : null;
}

function requireAuth() {
  const session = getSession();
  if (!session) {
    window.location.href = '../DangNhap.html';
    return null;
  }
  return session;
}

function getRoleConfig(role) {
  return ROLE_CONFIG[role] || null;
}

function getFirstPage(role) {
  const config = getRoleConfig(role);
  if (!config || !config.menu.length) return 'TrangChu.html';
  return config.menu[0].page;
}

// ============================================================
// Register new volunteer (self-registration only)
// ============================================================
function registerVolunteer(data) {
  const exists = MOCK_USERS.find(u => u.email === data.email);
  if (exists) return { success: false, message: 'Email đã được sử dụng.' };
  const newUser = {
    id: 'U' + Date.now(),
    email: data.email,
    password: data.password,
    role: 'tinh_nguyen_vien',
    name: data.name,
    department: 'Tình nguyện viên',
    avatar: ''
  };
  MOCK_USERS.push(newUser);
  return { success: true };
}

// ============================================================
// Admin: create hospital staff account
// ============================================================
function createStaffAccount(data) {
  const exists = MOCK_USERS.find(u => u.email === data.email);
  if (exists) return { success: false, message: 'Email đã tồn tại trong hệ thống.' };
  const autoPassword = 'Staff@' + Math.random().toString(36).slice(-6).toUpperCase();
  const newUser = {
    id: 'U' + Date.now(),
    email: data.email,
    password: autoPassword,
    role: data.role,
    name: data.name,
    department: data.department || '',
    avatar: '',
    cccd: data.cccd || ''
  };
  MOCK_USERS.push(newUser);
  return { success: true, password: autoPassword, user: newUser };
}

function getAllUsers() {
  return MOCK_USERS.map(u => ({ ...u, password: undefined }));
}

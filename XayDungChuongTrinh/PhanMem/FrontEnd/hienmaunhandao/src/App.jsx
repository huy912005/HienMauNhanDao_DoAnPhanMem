import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import UserLayout from './layouts/UserLayout';
import QuanLyKhoLayout from './layouts/QuanLyKhoLayout';
import NVYTLayout from './layouts/NVYTLayout';
import BacSiLayout from './layouts/BacSiLayout';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import RegisterVolunteer from './pages/RegisterVolunteer';
import OtpVerification from './pages/OtpVerification';
import AboutPage from './pages/AboutPage';
import ChienDichPage from './pages/ChienDichPage';
import ThongTinCaNhan from './pages/ThongTinCaNhan';
import KhaiBaoYTe from './pages/KhaiBaoYTe';
import XacNhanDangKy from './pages/XacNhanDangKy';
import ThongKeTonKho from './pages/qlk/ThongKeTonKho';
import QuanLyNhapKho from './pages/qlk/QuanLyNhapKho';
import QuanLyNhapKhoTheoChienDich from './pages/qlk/QuanLyNhapKhoTheoChienDich';
import DanhSachDonDangKy from './pages/DanhSachDonDangKy';
import DebugLogin from './pages/qlk/DebugLogin';

// NVYT pages
import DonDangKy from './pages/nvyt/DonDangKy';
import TinhNguyenVien from './pages/nvyt/TinhNguyenVien';
import KhaiBaoYTeNVYT from './pages/nvyt/KhaiBaoYTeNVYT';
import KhamLamSang from './pages/bacsi/KhamLamSang';
import DanhSachChoKham from './pages/bacsi/DanhSachChoKham';
import KetQuaXetNghiem from './pages/bacsi/KetQuaXetNghiem';
import CapNhatXetNghiem from './pages/nvyt/CapNhatXetNghiem';
import ThuNhanMau from './pages/nvyt/ThuNhanMau'; // Trigger Vite reload

const queryClient = new QueryClient();

// Guard: chỉ cho phép role NVYT truy cập
function NvytGuard({ children }) {
  const role = localStorage.getItem('role');
  if (role !== 'NVYT') return <Navigate to="/login" replace />;
  return children;
}

function BacSiGuard({ children }) {
  const role = localStorage.getItem('role');
  if (role !== 'BS') return <Navigate to="/login" replace />;
  return children;
}

// Guard: chỉ cho phép role QLK (Quản lý kho) truy cập
function QlkGuard({ children }) {
  const role = localStorage.getItem('role');
  if (role !== 'QLK') return <Navigate to="/login" replace />;
  return children;
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          {/* ── Trang người dùng / tình nguyện viên ── */}
          <Route path="/" element={<UserLayout />}>
            <Route index element={<HomePage />} />
            <Route path="login" element={<Login />} />
            <Route path="register" element={<RegisterVolunteer />} />
            <Route path="otp" element={<OtpVerification />} />
            <Route path="about" element={<AboutPage />} />
            <Route path="chiendich" element={<ChienDichPage />} />
            <Route path="don-dang-ky" element={<DanhSachDonDangKy />} />
            <Route path="don-dang-ky-detail/:maDon" element={<XacNhanDangKy />} />
            <Route path="khai-bao-thong-tin-ca-nhan" element={<ThongTinCaNhan />} />
            <Route path="khai-bao-y-te" element={<KhaiBaoYTe />} />
            <Route path="xac-nhan-dang-ky" element={<XacNhanDangKy />} />
            <Route path="xac-nhan-dang-ky/:maDon" element={<XacNhanDangKy />} />
            <Route path="debug-login" element={<DebugLogin />} />
          </Route>
          {/* Quản Lý Kho Routes */}
          <Route path="/quan-ly-kho" element={<QlkGuard><QuanLyKhoLayout /></QlkGuard>}>
            <Route path="thong-ke" element={<ThongKeTonKho />} />
            <Route path="nhap-kho" element={<QuanLyNhapKho />} />
            <Route path="nhap-kho-chien-dich" element={<QuanLyNhapKhoTheoChienDich />} />
          </Route>

          {/* ── Trang bác sĩ (maVaiTro = BS trong TAIKHOAN) ── */}
          <Route path="/bac-si" element={<BacSiGuard><BacSiLayout /></BacSiGuard>}>
            <Route index element={<Navigate to="danh-sach-cho-kham" replace />} />
            <Route path="danh-sach-cho-kham" element={<DanhSachChoKham />} />
            <Route path="kham-lam-sang" element={<KhamLamSang />} />
            <Route path="ket-qua-xet-nghiem" element={<KetQuaXetNghiem />} />
          </Route>

          {/* ── Trang nhân viên y tế ── */}
          <Route path="/nvyt" element={<NvytGuard><NVYTLayout /></NvytGuard>}>
            <Route index element={<Navigate to="don-dang-ky" replace />} />
            <Route path="don-dang-ky" element={<DonDangKy />} />
            <Route path="tinh-nguyen-vien" element={<TinhNguyenVien />} />
            <Route path="khai-bao-y-te" element={<KhaiBaoYTeNVYT />} />
            <Route path="cap-nhat-xet-nghiem" element={<CapNhatXetNghiem />} />
            <Route path="thu-nhan-mau" element={<ThuNhanMau />} />
          </Route>
        </Routes>
      </Router>
    </QueryClientProvider>
  );
}

export default App;

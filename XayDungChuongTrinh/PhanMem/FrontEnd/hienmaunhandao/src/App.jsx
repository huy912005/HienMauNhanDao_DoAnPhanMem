import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import UserLayout from './layouts/UserLayout';
import NVYTLayout from './layouts/NVYTLayout';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import RegisterVolunteer from './pages/RegisterVolunteer';
import OtpVerification from './pages/OtpVerification';
import AboutPage from './pages/AboutPage';
import ChienDichPage from './pages/ChienDichPage';
import ThongTinCaNhan from './pages/ThongTinCaNhan';
import KhaiBaoYTe from './pages/KhaiBaoYTe';
import XacNhanDangKy from './pages/XacNhanDangKy';
// NVYT pages
import DonDangKy from './pages/nvyt/DonDangKy';
import TinhNguyenVien from './pages/nvyt/TinhNguyenVien';
import KhaiBaoYTeNVYT from './pages/nvyt/KhaiBaoYTeNVYT';
import KhamLamSang from './pages/nvyt/KhamLamSang';
import ThuNhanMau from './pages/nvyt/ThuNhanMau';
import CapNhatXetNghiem from './pages/nvyt/CapNhatXetNghiem';

const queryClient = new QueryClient();

// Guard: chỉ cho phép role NVYT truy cập
function NvytGuard({ children }) {
  const role = localStorage.getItem('role');
  if (role !== 'NVYT') return <Navigate to="/login" replace />;
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
            <Route path="khai-bao-thong-tin-ca-nhan" element={<ThongTinCaNhan />} />
            <Route path="khai-bao-y-te" element={<KhaiBaoYTe />} />
            <Route path="xac-nhan-dang-ky" element={<XacNhanDangKy />} />
          </Route>

          {/* ── Trang nhân viên y tế ── */}
          <Route path="/nvyt" element={<NvytGuard><NVYTLayout /></NvytGuard>}>
            <Route index element={<Navigate to="don-dang-ky" replace />} />
            <Route path="don-dang-ky" element={<DonDangKy />} />
            <Route path="tinh-nguyen-vien" element={<TinhNguyenVien />} />
            <Route path="khai-bao-y-te" element={<KhaiBaoYTeNVYT />} />
            <Route path="kham-lam-sang" element={<KhamLamSang />} />
            <Route path="thu-nhan-mau" element={<ThuNhanMau />} />
            <Route path="cap-nhat-xet-nghiem" element={<CapNhatXetNghiem />} />
          </Route>
        </Routes>
      </Router>
    </QueryClientProvider>
  );
}

export default App;

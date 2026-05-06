import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

export default function Header() {
  const navigate = useNavigate();
  const location = useLocation();
  const token = localStorage.getItem('token');
  const userEmail = localStorage.getItem('email');
  const [hasCampaign, setHasCampaign] = useState(false);

  useEffect(() => {
    const campaign = localStorage.getItem('selectedCampaign');
    setHasCampaign(!!campaign);
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    navigate('/login');
  };

  // Helper để xác định class cho link đang active
  const navLinkClass = (path) => {
    const isActive = location.pathname === path;
    return isActive 
      ? "px-4 py-2 text-primary font-bold border-b-2 border-primary transition-colors uppercase text-sm"
      : "px-4 py-2 text-slate-700 hover:text-primary font-bold border-b-2 border-transparent hover:border-primary transition-colors uppercase text-sm";
  };

  return (
    <>
      <div className="w-full bg-slate-800 text-white text-[11px] font-medium py-1.5 px-8 flex justify-between items-center">
        <div className="flex gap-6">
          <span className="flex items-center gap-2"><span className="material-symbols-outlined text-[14px]">location_on</span>Địa chỉ: 123 Lê Lợi, Hải Châu, Đà Nẵng</span>
          <span className="flex items-center gap-2"><span className="material-symbols-outlined text-[14px]">call</span>Đường dây nóng: 1900 1234</span>
        </div>
        <div className="flex gap-4 items-center">
          <div className="flex gap-2">
            <a href="#" className="hover:text-primary transition-colors">Facebook</a>
            <a href="#" className="hover:text-primary transition-colors">Zalo</a>
          </div>
          <div className="h-3 w-[1px] bg-slate-600"></div>
          <a href="#" className="hover:text-primary transition-colors">Hỗ trợ</a>
        </div>
      </div>

      <header className="w-full bg-white border-b border-slate-200 sticky top-0 z-50 shadow-sm">
        <div className="w-[1200px] mx-auto h-24 flex items-center justify-between px-4">
          <div className="flex items-center gap-4 cursor-pointer" onClick={() => navigate('/')}>
            <div className="w-12 h-12 bg-primary rounded-xl flex items-center justify-center text-white shrink-0">
              <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>bloodtype</span>
            </div>
            <div className="flex flex-col">
              <h1 className="text-xl font-black tracking-tight text-red-700 leading-tight">HỆ THỐNG HIẾN MÁU</h1>
              <p className="text-[11px] uppercase tracking-widest text-slate-500 font-bold">TP. Đà Nẵng</p>
            </div>
          </div>

          <nav className="flex items-center gap-1">
            <Link className={navLinkClass('/')} to="/">Trang chủ</Link>
            <Link className={navLinkClass('/about')} to="/about">Giới thiệu</Link>
            {hasCampaign ? (
              <Link className={navLinkClass('/khai-bao-thong-tin-ca-nhan')} to="/khai-bao-thong-tin-ca-nhan">
                Đăng ký hiến máu
              </Link>
            ) : (
              <span className="px-4 py-2 text-slate-400 font-bold border-b-2 border-transparent uppercase text-sm cursor-not-allowed opacity-50">
                Đăng ký hiến máu
              </span>
            )}
            <Link className={navLinkClass('/chiendich')} to="/chiendich">Chiến dịch</Link>
          </nav>

          <div className="flex items-center gap-4">
            {token ? (
              <>
                <button className="w-10 h-10 flex items-center justify-center text-slate-600 hover:bg-slate-50 rounded-full relative">
                  <span className="material-symbols-outlined">notifications</span>
                  <span className="absolute top-2 right-2 w-2 h-2 bg-red-600 rounded-full"></span>
                </button>
                <div className="flex items-center gap-3 pl-4 border-l border-slate-200 cursor-pointer relative group py-2">
                  <div className="text-right hidden md:block">
                    <p className="text-sm font-bold text-slate-900 leading-none mb-1">{userEmail}</p>
                    <p className="text-[10px] text-slate-500 uppercase font-bold tracking-tight">Tình nguyện viên</p>
                  </div>
                  <img alt="User portrait" className="w-10 h-10 rounded-full object-cover border-2 border-primary-container/20 shrink-0"
                    src="https://lh3.googleusercontent.com/aida-public/AB6AXuCZrkDisk3_j22F4lcXb3jllF-99BYq3_SrntgHddIgx0onK2l25zj9yhgH0CmKsiDKHkcKkj4fUkJYPLzjVjDwaqQGvaw9tMIBFeFdSruc8nrljijQYlXObDS9Cl8vGxmXo6QmITg4G4hMQYoZ8uopbYnk178pDHgbWOMuPbFk0gIfxKgLIZPEvBzsxP0L8DjAy2sjgVh-OfYdDzSpRdcdxYlZ4c9LNMFhhzsp2kBlmP-sFmYmsUQVv-D164nUzgWv_CxOokcTNjOZ" />
                  
                  <div className="absolute right-0 top-full mt-2 w-48 bg-white border border-slate-200 rounded-xl shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                    <div className="py-2">
                      <Link to="#" className="flex items-center gap-3 px-4 py-2 text-sm text-slate-700 hover:bg-slate-50 hover:text-primary transition-colors">
                        <span className="material-symbols-outlined text-[18px]">person</span>Hồ sơ & Lịch sử
                      </Link>
                      <div className="h-[1px] bg-slate-100 my-1"></div>
                      <button onClick={handleLogout} className="w-full flex items-center gap-3 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors">
                        <span className="material-symbols-outlined text-[18px]">logout</span>Đăng xuất
                      </button>
                    </div>
                  </div>
                </div>
              </>
            ) : (
              <div className="flex gap-2">
                <Link to="/login" className="px-4 py-2 text-primary font-bold border border-primary rounded-lg hover:bg-red-50">Đăng nhập</Link>
                <Link to="/register" className="px-4 py-2 bg-primary text-white font-bold rounded-lg hover:bg-red-800">Đăng ký</Link>
              </div>
            )}
          </div>
        </div>
      </header>
    </>
  );
}

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function XacNhanDangKy() {
  const navigate = useNavigate();
  const [selectedCampaign, setSelectedCampaign] = useState(null);
  const [thongTinCaNhan, setThongTinCaNhan] = useState(null);
  const [maDon, setMaDon] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const campaign = localStorage.getItem('selectedCampaign');
    const thongTin = localStorage.getItem('thongTinCaNhan');
    const savedMaDon = localStorage.getItem('maDon');

    if (!campaign || !thongTin || !savedMaDon) {
      navigate('/chiendich');
      return;
    }

    setSelectedCampaign(JSON.parse(campaign));
    setThongTinCaNhan(JSON.parse(thongTin));
    setMaDon(savedMaDon);
    setLoading(false);

    // Xoá localStorage sau khi hiển thị thông tin thành công
    setTimeout(() => {
      localStorage.removeItem('selectedCampaign');
      localStorage.removeItem('thongTinCaNhan');
      localStorage.removeItem('healthAnswers');
      localStorage.removeItem('moTaKhac');
      localStorage.removeItem('tinhNguyenVienId');
      localStorage.removeItem('maDon');
    }, 5000);
  }, [navigate]);

  if (loading) {
    return (
      <div className="flex-1 flex items-center justify-center bg-slate-50 min-h-screen">
        <div className="text-slate-500">Đang tải...</div>
      </div>
    );
  }

  if (!selectedCampaign || !thongTinCaNhan) {
    return (
      <div className="flex-1 flex items-center justify-center bg-slate-50 min-h-screen">
        <div className="text-slate-500">Đang tải...</div>
      </div>
    );
  }

  return (
    <main className="flex-1 w-[1200px] mx-auto p-8 mt-8 mb-16 flex justify-center">
      <div className="bg-white w-[700px] border border-slate-200 rounded-2xl shadow-xl overflow-hidden flex flex-col">
        {/* Success Header */}
        <div className="bg-gradient-to-br from-green-600 to-green-800 h-36 flex flex-col items-center justify-center text-white px-6 text-center relative overflow-hidden">
          <div className="absolute top-0 right-0 w-32 h-32 bg-white/10 rounded-full -mr-16 -mt-16"></div>
          <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center mb-2 z-10 backdrop-blur-sm">
            <span className="material-symbols-outlined text-2xl text-white">check_circle</span>
          </div>
          <h3 className="text-xl font-black uppercase tracking-tight relative z-10">ĐĂNG KÝ THÀNH CÔNG</h3>
          <p className="text-[11px] opacity-80 mt-1 uppercase tracking-widest font-bold relative z-10">Chi tiết phiếu đăng ký hiến máu</p>
        </div>

        <div className="p-8 flex flex-col items-center">

          {/* Registration Code */}
          <div className="text-center mb-6">
            <p className="text-sm font-medium text-slate-500 mb-1">Mã đăng ký của bạn</p>
            <p className="text-3xl font-black text-primary tracking-[0.2em] bg-red-50 px-6 py-2 rounded-xl border border-red-100">{maDon}</p>
          </div>

          {/* Warning Box */}
          <div className="w-full p-4 bg-yellow-50 border border-yellow-100 rounded-md mb-8 flex gap-3">
            <span className="material-symbols-outlined text-yellow-600 shrink-0">tips_and_updates</span>
            <p className="text-[12px] text-yellow-800 leading-normal font-medium">Vui lòng chụp màn hình hoặc ghi lại mã đăng ký này và xuất trình tại quầy tiếp đón để được làm thủ tục nhanh nhất.</p>
          </div>

          {/* Registration Form Summary */}
          <div className="w-full bg-slate-50 border border-slate-100 rounded-xl p-6 mb-8 relative">
            <h3 className="text-lg font-bold text-slate-800 mb-6 pb-4 border-b border-slate-200 flex items-center gap-2">
              <span className="material-symbols-outlined text-primary">description</span>
              Thông Tin Đăng Ký
            </h3>

            <div className="grid grid-cols-2 gap-y-4 gap-x-8">
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Họ và tên</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.hoVaTen}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Số CCCD</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.soCCCD}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Ngày sinh</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.ngaySinh}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Giới tính</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.gioiTinh}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Số điện thoại</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.soDienThoai}</p>
              </div>
              <div className="col-span-2 space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Địa chỉ cư trú</span>
                <p className="text-sm font-bold text-slate-800">{thongTinCaNhan?.diaChi}</p>
              </div>

              <div className="col-span-2 my-2"><div className="h-px w-full bg-slate-200"></div></div>

              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Chiến dịch</span>
                <p className="text-sm font-bold text-slate-800">{selectedCampaign?.tenChienDich}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Dung tích hiến</span>
                <p className="text-sm font-bold text-primary bg-red-100 inline-block px-2 py-0.5 rounded-md">{thongTinCaNhan?.dungTichMau} ml</p>
              </div>
              <div className="col-span-2 space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Thời gian</span>
                <p className="text-sm font-semibold text-slate-700">{new Date(selectedCampaign?.thoiGianBD).toLocaleString('vi-VN')}</p>
              </div>
              <div className="col-span-2 space-y-1">
                <span className="text-xs font-bold text-slate-400 uppercase">Địa điểm</span>
                <p className="text-sm font-semibold text-slate-700">{selectedCampaign?.diaDiem?.tenDiaDiem}</p>
              </div>
            </div>
          </div>

          {/* Buttons */}
          <div className="flex gap-4 w-full justify-center">
            <button
              onClick={() => window.print()}
              className="w-48 h-12 border border-slate-200 bg-white rounded-md text-sm font-bold text-slate-600 hover:bg-slate-50 transition-colors flex items-center justify-center gap-2 shadow-sm"
            >
              <span className="material-symbols-outlined text-lg">print</span>
              IN PHIẾU
            </button>
            <button
              onClick={() => navigate('/')}
              className="w-48 h-12 bg-slate-800 text-white rounded-md text-sm font-bold hover:bg-slate-900 transition-colors flex items-center justify-center gap-2 shadow-md"
            >
              VỀ TRANG CHỦ
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function ThongTinCaNhan() {
  const navigate = useNavigate();
  const [selectedCampaign, setSelectedCampaign] = useState(null);
  const [formData, setFormData] = useState({
    hoVaTen: '',
    soCCCD: '',
    ngaySinh: '',
    gioiTinh: 'Nam',
    soDienThoai: '',
    email: '',
    diaChi: '',
    dungTichMau: '350'
  });
  const [error, setError] = useState('');

  useEffect(() => {
    const campaign = localStorage.getItem('selectedCampaign');
    if (!campaign) {
      navigate('/chiendich');
      return;
    }
    setSelectedCampaign(JSON.parse(campaign));

    // Load user info if available
    const email = localStorage.getItem('email');
    if (email) {
      setFormData(prev => ({
        ...prev,
        email: email
      }));
    }
  }, [navigate]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const validateForm = () => {
    if (!formData.hoVaTen.trim()) {
      setError('Vui lòng nhập họ và tên');
      return false;
    }
    if (!formData.soCCCD.trim()) {
      setError('Vui lòng nhập số CCCD');
      return false;
    }
    if (!formData.ngaySinh) {
      setError('Vui lòng chọn ngày sinh');
      return false;
    }
    if (!formData.soDienThoai.trim()) {
      setError('Vui lòng nhập số điện thoại');
      return false;
    }
    if (!formData.diaChi.trim()) {
      setError('Vui lòng nhập địa chỉ cư trú');
      return false;
    }
    setError('');
    return true;
  };

  const handleNext = () => {
    if (!validateForm()) return;
    localStorage.setItem('thongTinCaNhan', JSON.stringify(formData));
    navigate('/khai-bao-y-te');
  };

  if (!selectedCampaign) {
    return (
      <div className="flex-1 flex items-center justify-center bg-slate-50 min-h-screen">
        <div className="text-slate-500">Đang tải...</div>
      </div>
    );
  }

  return (
    <div className="flex-1 flex flex-col min-h-screen bg-slate-50">
      {/* Main Content */}
      <main className="flex-1 w-[1200px] mx-auto p-8 mt-8 bg-white border border-slate-200 rounded-2xl shadow-sm mb-16 self-center">
        {/* Progress Steps */}
        <div className="mb-8 max-w-2xl mx-auto">
          <div className="flex items-center justify-between relative">
            <div className="absolute left-0 top-1/2 -translate-y-1/2 w-full h-1 bg-slate-100 -z-10"></div>
            <div className="flex flex-col items-center gap-2">
              <div className="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center font-bold text-sm shadow-md ring-4 ring-white">1</div>
              <span className="text-xs font-bold text-primary">Thông tin cá nhân</span>
            </div>
            <div className="flex flex-col items-center gap-2">
              <div className="w-8 h-8 rounded-full bg-slate-200 text-slate-500 flex items-center justify-center font-bold text-sm ring-4 ring-white">2</div>
              <span className="text-xs font-bold text-slate-400">Khai báo y tế</span>
            </div>
          </div>
        </div>

        <div className="mb-8">
          <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">ĐĂNG KÝ HIẾN MÁU</h1>
          <p className="text-slate-500 text-sm mt-1">Vui lòng cung cấp thông tin cá nhân và chọn dung tích hiến máu.</p>
        </div>

        <div className="grid grid-cols-12 gap-8">
          {/* Campaign Info */}
          <div className="col-span-4 space-y-6">
            {selectedCampaign && (
              <div className="w-full bg-white border border-slate-200 rounded-md p-6 relative overflow-hidden h-fit shadow-sm">
                <div className="absolute top-0 right-0 p-4 opacity-10 pointer-events-none">
                  <span className="material-symbols-outlined text-8xl">campaign</span>
                </div>
                <div className="relative z-10">
                  <div className="mb-4">
                    <span className="inline-block px-2 py-1 bg-red-100 text-red-700 text-[10px] font-bold rounded uppercase">Chiến dịch đang diễn ra</span>
                  </div>
                  <h3 className="text-xl font-bold text-slate-800 mb-6">{selectedCampaign.tenChienDich}</h3>
                  <div className="space-y-5">
                    <div className="flex items-start gap-4">
                      <span className="material-symbols-outlined text-primary shrink-0">calendar_month</span>
                      <div>
                        <p className="text-[11px] text-slate-500 font-bold uppercase">Thời gian</p>
                        <p className="text-sm font-semibold text-slate-700">{new Date(selectedCampaign.thoiGianBD).toLocaleString('vi-VN')}</p>
                      </div>
                    </div>
                    <div className="flex items-start gap-4">
                      <span className="material-symbols-outlined text-primary shrink-0">location_on</span>
                      <div>
                        <p className="text-[11px] text-slate-500 font-bold uppercase">Địa điểm</p>
                        <p className="text-sm font-semibold text-slate-700">{selectedCampaign.diaDiem?.tenDiaDiem}</p>
                        <p className="text-xs text-slate-400 mt-1 italic leading-relaxed">{selectedCampaign.diaDiem?.diaChiChiTiet}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}

            <div className="w-full bg-red-50 border border-red-100 rounded-md p-6 h-fit">
              <h4 className="font-bold text-primary mb-4 flex items-center gap-2">
                <span className="material-symbols-outlined text-lg">info</span>
                Lưu ý trước khi hiến
              </h4>
              <ul className="text-[13px] text-slate-700 space-y-3 leading-relaxed">
                <li className="flex gap-2"><span>•</span> <span>Ngủ đủ ít nhất 6 tiếng trước ngày hiến máu.</span></li>
                <li className="flex gap-2"><span>•</span> <span>Không uống rượu bia trong vòng 24 giờ.</span></li>
                <li className="flex gap-2"><span>•</span> <span>Ăn nhẹ, tránh các thực phẩm nhiều dầu mỡ.</span></li>
                <li className="flex gap-2"><span>•</span> <span>Mang theo CMND/CCCD hoặc thẻ hiến máu.</span></li>
              </ul>
            </div>
          </div>

          {/* Registration Form */}
          <div className="col-span-8">
            <div className="w-full bg-white border border-slate-200 rounded-md p-10 h-full shadow-sm">
              <div className="flex items-center gap-4 mb-8 pb-6 border-b border-slate-100">
                <div className="w-12 h-12 rounded-full bg-blue-50 flex items-center justify-center">
                  <span className="material-symbols-outlined text-blue-600 text-3xl">person</span>
                </div>
                <div>
                  <h3 className="text-xl font-bold text-slate-800">Thông Tin Cá Nhân</h3>
                  <p className="text-xs text-slate-500 mt-1">Các thông tin bắt buộc phải điền chính xác theo CMND/CCCD.</p>
                </div>
              </div>

              {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm font-medium">
                  {error}
                </div>
              )}

              <form className="space-y-6">
                <div className="grid grid-cols-2 gap-6">
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Họ và tên <span className="text-red-500">*</span></label>
                    <input
                      name="hoVaTen"
                      value={formData.hoVaTen}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none placeholder-slate-400"
                      type="text"
                      placeholder="Nhập họ và tên đầy đủ"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Số CCCD <span className="text-red-500">*</span></label>
                    <input
                      name="soCCCD"
                      value={formData.soCCCD}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none placeholder-slate-400"
                      type="text"
                      placeholder="Nhập số CCCD 12 số"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Ngày sinh <span className="text-red-500">*</span></label>
                    <input
                      name="ngaySinh"
                      value={formData.ngaySinh}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none"
                      type="date"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Giới tính <span className="text-red-500">*</span></label>
                    <select
                      name="gioiTinh"
                      value={formData.gioiTinh}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none"
                    >
                      <option value="Nam">Nam</option>
                      <option value="Nữ">Nữ</option>
                    </select>
                  </div>
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Số điện thoại <span className="text-red-500">*</span></label>
                    <input
                      name="soDienThoai"
                      value={formData.soDienThoai}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none placeholder-slate-400"
                      type="tel"
                      placeholder="Nhập số điện thoại"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="block text-sm font-bold text-slate-700">Email</label>
                    <input
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none placeholder-slate-400"
                      type="email"
                      placeholder="example@email.com"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <label className="block text-sm font-bold text-slate-700">Địa chỉ cư trú <span className="text-red-500">*</span></label>
                  <input
                    name="diaChi"
                    value={formData.diaChi}
                    onChange={handleInputChange}
                    className="w-full h-11 border border-slate-300 rounded-md text-sm focus:ring-primary focus:border-primary outline-none placeholder-slate-400"
                    type="text"
                    placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố"
                  />
                </div>

                {/* Donation Volume */}
                <div className="pt-6 border-t border-slate-100">
                  <h4 className="text-sm font-bold text-slate-800 py-2.5 border-l-4 border-primary pl-4 bg-slate-50 mb-4">
                    Dung tích máu dự kiến hiến <span className="text-red-500">*</span>
                  </h4>
                  <div className="grid grid-cols-3 gap-4">
                    <label className="relative flex flex-col p-4 border border-slate-200 cursor-pointer rounded-xl hover:bg-slate-50 transition-colors has-[:checked]:border-primary has-[:checked]:bg-red-50 has-[:checked]:ring-1 has-[:checked]:ring-primary">
                      <input
                        type="radio"
                        name="dungTichMau"
                        value="250"
                        checked={formData.dungTichMau === '250'}
                        onChange={handleInputChange}
                        className="sr-only"
                      />
                      <span className="text-lg font-bold text-slate-800 mb-1">250 ml</span>
                      <span className="text-xs text-slate-500 font-medium">Phù hợp cho cân nặng từ 42kg - 45kg</span>
                    </label>
                    <label className="relative flex flex-col p-4 border border-slate-200 cursor-pointer rounded-xl hover:bg-slate-50 transition-colors has-[:checked]:border-primary has-[:checked]:bg-red-50 has-[:checked]:ring-1 has-[:checked]:ring-primary">
                      <input
                        type="radio"
                        name="dungTichMau"
                        value="350"
                        checked={formData.dungTichMau === '350'}
                        onChange={handleInputChange}
                        className="sr-only"
                      />
                      <span className="text-lg font-bold text-slate-800 mb-1">350 ml</span>
                      <span className="text-xs text-slate-500 font-medium">Phù hợp cho cân nặng trên 45kg</span>
                    </label>
                    <label className="relative flex flex-col p-4 border border-slate-200 cursor-pointer rounded-xl hover:bg-slate-50 transition-colors has-[:checked]:border-primary has-[:checked]:bg-red-50 has-[:checked]:ring-1 has-[:checked]:ring-primary">
                      <input
                        type="radio"
                        name="dungTichMau"
                        value="450"
                        checked={formData.dungTichMau === '450'}
                        onChange={handleInputChange}
                        className="sr-only"
                      />
                      <span className="text-lg font-bold text-slate-800 mb-1">450 ml</span>
                      <span className="text-xs text-slate-500 font-medium">Phù hợp cho cân nặng trên 50kg</span>
                    </label>
                  </div>
                </div>

                <div className="flex justify-end pt-4">
                  <button
                    onClick={handleNext}
                    className="w-48 h-12 bg-slate-800 text-white rounded-md font-bold text-sm flex items-center justify-center gap-2 hover:bg-slate-900 transition-all shadow-md active:scale-[0.98]"
                    type="button"
                  >
                    TIẾP THEO
                    <span className="material-symbols-outlined text-xl">arrow_forward</span>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

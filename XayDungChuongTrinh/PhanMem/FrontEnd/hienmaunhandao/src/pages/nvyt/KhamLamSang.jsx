import React, { useState } from 'react';
import { useOutletContext } from 'react-router-dom';

export default function KhamLamSang() {
  const { nhanVien } = useOutletContext();
  const [isCheckedIn, setIsCheckedIn] = useState(false);
  const [donorInfo, setDonorInfo] = useState(null);
  const [qrInput, setQrInput] = useState('');
  const [pulsing, setPulsing] = useState(false);
  const [form, setForm] = useState({ huyetAp: '', nhipTim: '', canNang: '', ketQua: '' });
  const [volumeSelect, setVolumeSelect] = useState('250');
  const [maTuiMau] = useState(`BB-DN-${new Date().getFullYear()}-${String(Math.floor(Math.random() * 99999)).padStart(6, '0')}`);
  const [saving, setSaving] = useState(false);
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = 'success') => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3500);
  };

  const handleScanQR = () => {
    if (!qrInput.trim()) { showToast('Vui lòng nhập mã QR / mã đơn', 'error'); return; }
    setPulsing(true);
    showToast('Đang kích hoạt cảm biến quét...', 'info');
    setTimeout(() => {
      setPulsing(false);
      setIsCheckedIn(true);
      setDonorInfo({
        hoVaTen: 'Lê Văn Bình',
        ngaySinh: '12/05/1995',
        gioiTinh: 'Nam',
        nhomMau: 'O+',
        soLanHienMau: 3,
        maDon: qrInput.trim(),
      });
      showToast(`Check-in thành công: Lê Văn Bình`, 'success');
    }, 1200);
  };

  const getVolumeAllowed = (weight) => {
    const w = parseFloat(weight);
    if (!w || w < 42) return { allowed: [250], max: 0 };
    if (w < 45) return { allowed: [250], max: 250 };
    return { allowed: [250, 350, 450], max: 450 };
  };

  const handleWeightChange = (val) => {
    setForm(p => ({ ...p, canNang: val }));
    const w = parseFloat(val);
    if (!isNaN(w)) {
      if (w < 42) {
        setForm(p => ({ ...p, canNang: val, ketQua: 'khong_dat' }));
        showToast('Cảnh báo: Cân nặng dưới 42kg không đủ điều kiện hiến máu.', 'error');
      } else {
        setForm(p => ({ ...p, canNang: val, ketQua: 'dat' }));
        if (w < 45) {
          setVolumeSelect('250');
          showToast('Lưu ý: Tình nguyện viên dưới 45kg chỉ được hiến tối đa 250ml.', 'warning');
        }
      }
    }
  };

  const handleBPChange = (val) => {
    setForm(p => ({ ...p, huyetAp: val }));
    const systolic = parseInt(val.split('/')[0]);
    if (!isNaN(systolic) && (systolic > 160 || systolic < 90)) {
      showToast('Cảnh báo: Chỉ số huyết áp bất thường. Cần kiểm tra kỹ.', 'error');
    }
  };

  const handleSave = async () => {
    if (!isCheckedIn) { showToast('Vui lòng quét mã QR để check-in trước.', 'error'); return; }
    if (!form.ketQua) { showToast('Vui lòng chọn kết quả sàng lọc.', 'error'); return; }
    setSaving(true);
    showToast('Đang mã hóa dữ liệu túi máu...', 'info');
    setTimeout(() => {
      showToast('Đang in tem nhãn barcode cho túi máu...', 'info');
      setTimeout(() => {
        setSaving(false);
        showToast('Hoàn tất! Đã lưu dữ liệu và ghi nhận túi máu vào hệ thống.', 'success');
      }, 1500);
    }, 1000);
  };

  const volInfo = getVolumeAllowed(form.canNang);

  const BLOOD_TYPES = [
    { label: 'A+', fill: '75%' }, { label: 'A-', fill: '25%' },
    { label: 'B+', fill: '50%' }, { label: 'B-', fill: '66%' },
    { label: 'O+', fill: '100%' }, { label: 'O-', fill: '20%' },
    { label: 'AB+', fill: '33%' }, { label: 'AB-', fill: '5%' },
  ];

  return (
    <div className="space-y-6">
      {toast && (
        <div className={`fixed top-6 right-6 z-[100] px-5 py-3 rounded-xl shadow-lg text-white text-sm font-bold flex items-center gap-2 transition-all
          ${toast.type === 'error' ? 'bg-red-600' : toast.type === 'warning' ? 'bg-amber-500' : toast.type === 'info' ? 'bg-blue-600' : 'bg-green-600'}`}>
          <span className="material-symbols-outlined text-lg">
            {toast.type === 'error' ? 'error' : toast.type === 'warning' ? 'warning' : toast.type === 'info' ? 'info' : 'check_circle'}
          </span>
          {toast.msg}
        </div>
      )}

      {/* Page header */}
      <div className="flex items-end justify-between">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">Khám sàng lọc & Thu nhận máu</h1>
          <p className="text-slate-500 mt-1 text-sm">Cập nhật chỉ số y tế và mã túi máu cho tình nguyện viên</p>
        </div>
        <div className="text-right text-sm text-slate-400 font-mono">
          {donorInfo?.maDon ? `ID PHIÊN: ${donorInfo.maDon}` : 'ID PHIÊN: ---'}
        </div>
      </div>

      {/* Grid layout giống KhamSangLoc.html */}
      <div className="grid grid-cols-12 gap-6">
        {/* QR Scan */}
        <div className="col-span-4 bg-white border border-slate-200 rounded-2xl p-6 h-[320px] flex flex-col items-center justify-center text-center relative overflow-hidden group shadow-sm">
          <div className="absolute inset-0 bg-slate-50/50 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none" />
          <div className="w-20 h-20 rounded-full bg-red-50 flex items-center justify-center mb-4 text-primary relative">
            {pulsing && <div className="absolute inset-0 rounded-full bg-red-400 opacity-20 animate-ping" />}
            <span className="material-symbols-outlined text-4xl relative z-10">qr_code_scanner</span>
          </div>
          <h4 className="font-bold text-slate-800 mb-2">Quét mã QR / Mã đơn</h4>
          <p className="text-sm text-slate-500 px-4 mb-4">Nhập mã đơn đăng ký hoặc quét QR để check-in và bắt đầu quy trình khám</p>
          <div className="w-full space-y-2">
            <input value={qrInput} onChange={e => setQrInput(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleScanQR()}
              placeholder="Nhập mã đơn..."
              className={`w-full h-10 border rounded-xl px-4 text-sm text-center font-mono outline-none focus:ring-2 transition-all
                ${isCheckedIn ? 'border-green-300 bg-green-50 text-green-700' : 'border-slate-200 focus:border-primary focus:ring-primary/10'}`}
            />
            <button onClick={handleScanQR}
              className={`w-full h-12 border-2 border-dashed rounded-xl flex items-center justify-center font-bold text-sm transition-all
                ${isCheckedIn
                  ? 'border-green-300 bg-green-50 text-green-600'
                  : 'border-red-200 bg-red-50 hover:bg-red-100 text-primary'
                }`}>
              <span className="material-symbols-outlined mr-2">barcode_scanner</span>
              {isCheckedIn ? 'CHECK-IN THÀNH CÔNG' : 'BẮT ĐẦU QUÉT MÃ QR'}
            </button>
          </div>
        </div>

        {/* Donor Info */}
        <div className="col-span-8 bg-white border border-slate-200 rounded-2xl p-6 h-[320px] flex flex-col shadow-sm">
          <div className="w-full border-b border-slate-100 pb-4 mb-6 flex justify-between items-center shrink-0">
            <span className="text-xs font-black uppercase text-slate-400 tracking-widest">Thông tin đối tượng</span>
            <span className={`px-3 py-1 text-xs font-bold rounded-full ${isCheckedIn ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
              {isCheckedIn ? 'Đã check-in' : 'Sẵn sàng'}
            </span>
          </div>
          <div className="grid grid-cols-2 gap-8 flex-1">
            <div className="space-y-6">
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Họ và tên</label>
                <p className="text-slate-700 font-medium">{donorInfo?.hoVaTen || '---'}</p>
              </div>
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Ngày sinh / Giới tính</label>
                <p className="text-slate-700 font-medium">{donorInfo ? `${donorInfo.ngaySinh} / ${donorInfo.gioiTinh}` : '---'}</p>
              </div>
            </div>
            <div className="space-y-6">
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Nhóm máu dự kiến</label>
                <p className={`font-medium ${donorInfo ? 'text-slate-700' : 'text-slate-400 italic'}`}>
                  {donorInfo?.nhomMau || 'Chờ quét mã...'}
                </p>
              </div>
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Số lần hiến máu</label>
                <p className="text-slate-700 font-medium">{donorInfo?.soLanHienMau ?? 0}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Form sàng lọc */}
        <div className="col-span-7 bg-white border border-slate-200 rounded-2xl p-8 min-h-[440px] shadow-sm">
          <div className="flex items-center gap-2 mb-8">
            <span className="material-symbols-outlined text-primary">clinical_notes</span>
            <h4 className="font-bold text-slate-800">Thông số sàng lọc</h4>
          </div>
          <div className="space-y-8">
            <div className="grid grid-cols-3 gap-4">
              {[
                { label: 'Huyết áp (mmHg)', key: 'huyetAp', placeholder: '120/80', type: 'text', onChange: handleBPChange },
                { label: 'Nhịp tim (bpm)', key: 'nhipTim', placeholder: '75', type: 'number', onChange: v => setForm(p => ({ ...p, nhipTim: v })) },
                { label: 'Cân nặng (kg)', key: 'canNang', placeholder: '65', type: 'number', onChange: handleWeightChange },
              ].map(f => (
                <div key={f.key} className="flex flex-col gap-1">
                  <label className="text-sm font-semibold text-slate-600">{f.label}</label>
                  <input type={f.type} value={form[f.key]} placeholder={f.placeholder}
                    onChange={e => f.onChange(e.target.value)}
                    className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-primary focus:ring-2 focus:ring-primary/10 transition-shadow px-4 outline-none"
                  />
                </div>
              ))}
            </div>

            <div className="pt-4 flex flex-col gap-4">
              <label className="text-sm font-semibold text-slate-600">Kết quả sàng lọc</label>
              <div className="flex gap-4 h-16">
                {[
                  { val: 'dat', label: 'Đạt yêu cầu', icon: 'check_circle', color: 'text-green-600' },
                  { val: 'khong_dat', label: 'Không đạt', icon: 'cancel', color: 'text-red-600' },
                ].map(opt => (
                  <label key={opt.val}
                    className="w-full flex items-center px-4 border border-slate-200 rounded-xl cursor-pointer hover:bg-slate-50 transition-colors group">
                    <input type="radio" name="ketQua" value={opt.val}
                      checked={form.ketQua === opt.val}
                      onChange={() => setForm(p => ({ ...p, ketQua: opt.val }))}
                      className="w-5 h-5 text-primary focus:ring-primary/20 border-slate-300"
                    />
                    <span className="ml-3 text-slate-700 font-medium">{opt.label}</span>
                    <span className={`material-symbols-outlined ml-auto opacity-0 group-hover:opacity-100 ${opt.color}`}
                      style={{ fontVariationSettings: "'FILL' 1" }}>{opt.icon}</span>
                  </label>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Thu nhận máu */}
        <div className="col-span-5 bg-white border border-slate-200 rounded-2xl p-8 flex flex-col shadow-sm">
          <div className="flex items-center gap-2 mb-8">
            <span className="material-symbols-outlined text-primary">vaccines</span>
            <h4 className="font-bold text-slate-800">Thu nhận máu</h4>
          </div>
          <div className="space-y-8 flex-1">
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-slate-600">Thể tích hiến máu</label>
              <select value={volumeSelect} onChange={e => setVolumeSelect(e.target.value)}
                className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-primary focus:ring-2 focus:ring-primary/10 px-4 outline-none">
                <option value="250" disabled={!volInfo.allowed.includes(250)}>250 ml</option>
                <option value="350" disabled={!volInfo.allowed.includes(350)}>350 ml</option>
                <option value="450" disabled={!volInfo.allowed.includes(450)}>450 ml</option>
              </select>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-slate-600">Mã vạch túi máu</label>
              <div className="relative w-full h-11">
                <input readOnly value={maTuiMau}
                  className="w-full h-full bg-slate-50 border border-slate-200 rounded-xl font-mono text-slate-600 cursor-not-allowed px-4 pr-12 outline-none text-sm"
                />
                <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl">barcode</span>
              </div>
              <p className="text-[10px] text-slate-400 mt-1 uppercase font-medium">Mã túi máu được hệ thống tự động khởi tạo</p>
            </div>
          </div>
          <div className="mt-8 flex flex-col gap-3 shrink-0">
            <button onClick={handleSave} disabled={saving}
              className="w-full h-14 bg-primary hover:bg-red-800 text-white rounded-xl font-bold transition-all shadow-lg shadow-primary/20 active:scale-[0.98] flex items-center justify-center gap-2 disabled:opacity-60">
              <span className="material-symbols-outlined">save</span>
              LƯU DỮ LIỆU
            </button>
            <button onClick={() => { if (confirm('Bạn có chắc muốn xóa các dữ liệu đang nhập?')) { setIsCheckedIn(false); setDonorInfo(null); setForm({ huyetAp: '', nhipTim: '', canNang: '', ketQua: '' }); setQrInput(''); } }}
              className="w-full h-11 border border-slate-200 text-slate-500 hover:bg-slate-50 rounded-xl font-semibold transition-all text-sm flex items-center justify-center gap-2">
              <span className="material-symbols-outlined text-lg">restart_alt</span>
              LÀM MỚI BIỂU MẪU
            </button>
          </div>
        </div>

        {/* Inventory visual */}
        <div className="col-span-12 bg-white border border-slate-200 rounded-2xl p-6 h-[180px] flex flex-col shadow-sm">
          <div className="w-full flex justify-between items-center mb-6">
            <h4 className="text-xs font-black uppercase text-slate-500 tracking-wider">Tình trạng lưu trữ tại điểm hiến máu</h4>
            <span className="text-xs text-primary font-bold">28 túi thu nhận hôm nay</span>
          </div>
          <div className="w-full grid grid-cols-8 gap-4 flex-1">
            {BLOOD_TYPES.map(bt => (
              <div key={bt.label} className="flex flex-col gap-2">
                <div className="w-full h-16 bg-slate-100 rounded-lg relative overflow-hidden">
                  <div className="absolute bottom-0 left-0 w-full bg-primary opacity-80" style={{ height: bt.fill }} />
                </div>
                <p className={`text-center text-xs font-bold ${bt.fill === '5%' ? 'text-slate-400' : 'text-slate-600'}`}>{bt.label}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

const BLOOD_TYPES = [
  { label: 'A+', fill: '75%' }, { label: 'A-', fill: '25%' },
  { label: 'B+', fill: '50%' }, { label: 'B-', fill: '66%' },
  { label: 'O+', fill: '100%' }, { label: 'O-', fill: '20%' },
  { label: 'AB+', fill: '33%' }, { label: 'AB-', fill: '5%' },
];

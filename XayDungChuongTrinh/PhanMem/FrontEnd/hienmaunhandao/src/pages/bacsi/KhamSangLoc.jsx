import { useState, useRef, useEffect } from 'react';
import { useOutletContext } from 'react-router-dom';
import { thuNhanMauService } from '../../services/khamLamSangService';
import { khamLamSangService } from '../../services/khamLamSangService';

const BLOOD_TYPES_LIST = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];

export default function KhamSangLoc() {
  const { nhanVien } = useOutletContext();
  const [isCheckedIn, setIsCheckedIn] = useState(false);
  const [scanning, setScanning] = useState(false);
  const [donor, setDonor] = useState(null);
  const [qrInput, setQrInput] = useState('');
  const [bloodPressure, setBloodPressure] = useState('');
  const [heartRate, setHeartRate] = useState('');
  const [weight, setWeight] = useState('');
  const [temperature, setTemperature] = useState('37.0');
  const [rejectReason, setRejectReason] = useState('');
  const [result, setResult] = useState('');
  const [volume, setVolume] = useState('350');
  const [bagCode] = useState(`BB-DN-${new Date().getFullYear()}-${String(Math.floor(Math.random() * 99999)).padStart(6, '0')}`);
  const [toast, setToast] = useState(null);
  const [bloodStats, setBloodStats] = useState({ tongSoTui: 0, theoNhomMau: {} });
  const [showList, setShowList] = useState(false);
  const [screeningList, setScreeningList] = useState([]);
  const [stats, setStats] = useState({ tongSo: 0, datYeuCau: 0, khongDat: 0 });
  const [loading, setLoading] = useState(false);
  const weightRef = useRef(null);

  // Fetch blood inventory stats from DB
  useEffect(() => {
    const fetchStats = async () => {
      try {
        const res = await thuNhanMauService.getStats();
        const data = res?.data || res || { tongSoTui: 0, theoNhomMau: {} };
        setBloodStats(data);
      } catch (err) {
        console.error('Lỗi khi lấy thống kê túi máu:', err);
      }
    };
    fetchStats();
  }, []);

  const fetchScreeningList = async () => {
    try {
      setLoading(true);
      const [dataRes, statsRes] = await Promise.all([
        khamLamSangService.getAll(),
        khamLamSangService.getStats(),
      ]);
      setScreeningList(Array.isArray(dataRes) ? dataRes : (dataRes.data || []));
      setStats(statsRes.data || statsRes || { tongSo: 0, datYeuCau: 0, khongDat: 0 });
    } catch (err) {
      console.error('Lỗi khi lấy dữ liệu:', err);
      showToast('Lỗi khi tải dữ liệu', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (showList) {
      fetchScreeningList();
    }
  }, [showList]);

  const showToast = (msg, type = 'info') => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3500);
  };

  const handleScanQR = async () => {
    if (!qrInput.trim()) {
      showToast('Vui lòng nhập mã QR / mã đơn', 'error');
      return;
    }
    if (scanning) return;
    setScanning(true);
    showToast('Đang tra cứu đơn đăng ký...', 'info');
    try {
      const res = await khamLamSangService.getWaiting();
      const list = Array.isArray(res) ? res : (res.data || []);
      const found = list.find(d => d.maDon === qrInput.trim());
      if (found) {
        setIsCheckedIn(true);
        setDonor({
          hoVaTen: found.tenTinhNguyenVien,
          ngaySinh: found.ngaySinh || '---',
          gioiTinh: found.gioiTinh || '---',
          nhomMau: found.nhomMau || '---',
          soLanHien: 0,
          maDon: found.maDon,
        });
        showToast(`Check-in thành công: ${found.tenTinhNguyenVien}`, 'success');
      } else {
        showToast('Không tìm thấy đơn đăng ký hoặc người này chưa sẵn sàng khám', 'warning');
      }
    } catch (err) {
      console.error(err);
      showToast('Lỗi khi kết nối server', 'error');
    } finally {
      setScanning(false);
    }
  };

  const evaluateScreening = (w = weight, bp = bloodPressure) => {
    const wNum = parseFloat(w);
    if (!isNaN(wNum) && wNum > 0) {
      if (wNum < 42) {
        setResult('fail');
        showToast('Cảnh báo: Cân nặng dưới 42kg không đủ điều kiện hiến máu.', 'error');
      } else {
        setResult('pass');
        if (wNum < 45) {
          setVolume('250');
          showToast('Lưu ý: Dưới 45kg chỉ được hiến tối đa 250ml.', 'warning');
        }
      }
    }
    if (bp) {
      const systolic = parseInt(bp.split('/')[0]);
      if (systolic > 160 || systolic < 90) {
        showToast('Cảnh báo: Chỉ số huyết áp bất thường. Cần kiểm tra kỹ.', 'error');
      }
    }
  };

  const handleSave = async () => {
    if (!isCheckedIn) {
      showToast('Vui lòng quét mã QR để check-in trước khi lưu dữ liệu.', 'error');
      return;
    }
    if (!result) {
      showToast('Vui lòng chọn kết quả sàng lọc.', 'error');
      return;
    }
    if (!nhanVien?.maNV) {
      showToast('Không thể lấy thông tin nhân viên. Vui lòng đăng nhập lại.', 'error');
      return;
    }
    try {
      const payload = {
        maDon: donor?.maDon,
        maNhanVien: nhanVien.maNV,
        huyetAp: bloodPressure,
        nhipTim: parseInt(heartRate),
        canNang: parseFloat(weight),
        nhietDo: parseFloat(temperature),
        ketQua: result === 'pass',
        lyDoTuChoi: rejectReason,
        theTichHien: parseInt(volume),
      };
      await khamLamSangService.save(payload);
      if (result === 'pass') {
        showToast('Hoàn tất! Đã lưu dữ liệu và ghi nhận túi máu vào hệ thống.', 'success');
      } else {
        showToast('Hồ sơ không đạt yêu cầu. Đã lưu trạng thái không đạt.', 'warning');
      }
      handleReset();
      if (showList) fetchScreeningList();
    } catch (err) {
      console.error(err);
      showToast('Lỗi khi lưu dữ liệu lên server', 'error');
    }
  };

  const handleReset = () => {
    setIsCheckedIn(false);
    setDonor(null);
    setBloodPressure('');
    setHeartRate('');
    setWeight('');
    setTemperature('37.0');
    setRejectReason('');
    setResult('');
    setVolume('350');
    setQrInput('');
    setScanning(false);
  };

  const toastColors = {
    success: 'bg-emerald-600',
    error: 'bg-red-600',
    warning: 'bg-amber-500',
    info: 'bg-blue-600',
  };

  // Compute blood bars from DB stats
  const maxInStock = Math.max(...Object.values(bloodStats.theoNhomMau || {}), 1);
  const bloodBars = BLOOD_TYPES_LIST.map(type => ({
    type,
    count: bloodStats.theoNhomMau?.[type] || 0,
    height: `${Math.min((bloodStats.theoNhomMau?.[type] || 0) / maxInStock * 100, 100)}%`,
  }));

  return (
    <div className="flex flex-col gap-6 w-full relative">
      {/* Toast */}
      {toast && (
        <div className={`fixed top-6 right-6 z-50 px-5 py-3 rounded-xl shadow-xl text-white text-sm font-semibold ${toastColors[toast.type] || 'bg-slate-800'} transition-all`}>
          {toast.msg}
        </div>
      )}

      {/* Header */}
      <div className="flex justify-between items-end">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">Khám sàng lọc &amp; Thu nhận máu</h1>
          <p className="text-slate-500 mt-1 text-sm">Cập nhật chỉ số y tế và mã túi máu cho tình nguyện viên</p>
        </div>
        <span className="text-right text-sm text-slate-400 font-mono">
          ID PHIÊN: BN-{new Date().toISOString().slice(0,10).replace(/-/g,'')}-042
        </span>
      </div>

      {/* Tabs */}
      <div className="flex gap-2 border-b border-slate-200">
        <button
          onClick={() => setShowList(false)}
          className={`px-4 py-3 font-semibold text-sm transition-colors border-b-2 ${!showList
              ? 'text-red-700 border-b-red-700'
              : 'text-slate-600 border-b-transparent hover:text-slate-800'
            }`}
        >
          <span className="material-symbols-outlined mr-1 inline" style={{ fontSize: '18px' }}>
            clinical_notes
          </span>
          Nhập dữ liệu
        </button>
        <button
          onClick={() => setShowList(true)}
          className={`px-4 py-3 font-semibold text-sm transition-colors border-b-2 ${showList
              ? 'text-red-700 border-b-red-700'
              : 'text-slate-600 border-b-transparent hover:text-slate-800'
            }`}
        >
          <span className="material-symbols-outlined mr-1 inline" style={{ fontSize: '18px' }}>
            list_alt
          </span>
          Danh sách ({stats.tongSo})
        </button>
      </div>

      {!showList ? (
        <>
          {/* Dashboard Grid */}
          <div className="grid grid-cols-12 gap-6">

        {/* QR Scan */}
        <div className="col-span-4 bg-white border border-slate-200 rounded-2xl p-6 h-[320px] flex flex-col items-center justify-center text-center relative overflow-hidden shadow-sm">
          <div className={`w-20 h-20 rounded-full bg-red-50 flex items-center justify-center mb-4 text-red-700 relative`}>
            {scanning && (
              <div className="absolute inset-0 rounded-full bg-red-400 opacity-20 animate-ping"></div>
            )}
            <span className="material-symbols-outlined text-4xl relative z-10">qr_code_scanner</span>
          </div>
          <h4 className="font-bold text-slate-800 mb-2">Quét mã QR / Mã đơn</h4>
          <p className="text-sm text-slate-500 px-4 mb-4">Nhập mã đơn đăng ký để check-in và bắt đầu quy trình khám sàng lọc</p>
          <div className="w-full space-y-2">
            <input
              value={qrInput}
              onChange={e => setQrInput(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleScanQR()}
              placeholder="Nhập mã đơn..."
              disabled={isCheckedIn}
              className={`w-full h-10 border rounded-xl px-4 text-sm text-center font-mono outline-none focus:ring-2 transition-all
                ${isCheckedIn ? 'border-green-300 bg-green-50 text-green-700' : 'border-slate-200 focus:border-red-400 focus:ring-red-200'}`}
            />
            <button
              onClick={isCheckedIn ? undefined : handleScanQR}
              className={`w-full h-12 border-2 border-dashed rounded-xl flex items-center justify-center font-bold text-sm transition-all
                ${isCheckedIn
                  ? 'border-green-200 bg-green-50 text-green-600 cursor-default'
                  : 'border-red-200 bg-red-50 hover:bg-red-100 text-red-600 cursor-pointer'
                }`}
            >
              <span className="material-symbols-outlined mr-2 text-xl">
                {isCheckedIn ? 'check_circle' : 'barcode_scanner'}
              </span>
              {isCheckedIn ? 'CHECK-IN THÀNH CÔNG' : 'BẮT ĐẦU QUÉT MÃ QR'}
            </button>
          </div>
        </div>

        {/* Donor Info */}
        <div className="col-span-8 bg-white border border-slate-200 rounded-2xl p-6 h-[320px] flex flex-col shadow-sm">
          <div className="w-full border-b border-slate-100 pb-4 mb-6 flex justify-between items-center flex-shrink-0">
            <span className="text-xs font-black uppercase text-slate-400 tracking-widest">Thông tin đối tượng</span>
            <span className={`px-3 py-1 text-xs font-bold rounded-full ${isCheckedIn ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
              {isCheckedIn ? 'Đã check-in' : 'Sẵn sàng'}
            </span>
          </div>
          <div className="grid grid-cols-2 gap-8 flex-1">
            <div className="space-y-6">
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Họ và tên</label>
                <p className="text-slate-700 font-medium">{donor?.hoVaTen || '---'}</p>
              </div>
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Ngày sinh / Giới tính</label>
                <p className="text-slate-700 font-medium">
                  {donor ? `${donor.ngaySinh} / ${donor.gioiTinh}` : '---'}
                </p>
              </div>
            </div>
            <div className="space-y-6">
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Nhóm máu dự kiến</label>
                <p className="text-slate-700 font-medium italic">{donor?.nhomMau || 'Chờ quét mã...'}</p>
              </div>
              <div className="h-12 flex flex-col justify-center">
                <label className="text-[10px] uppercase font-bold text-slate-400 block">Số lần hiến máu</label>
                <p className="text-slate-700 font-medium">{donor?.soLanHien ?? 0}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Screening Form */}
        <div className="col-span-7 bg-white border border-slate-200 rounded-2xl p-8 min-h-[440px] shadow-sm">
          <div className="flex items-center gap-2 mb-8">
            <span className="material-symbols-outlined text-red-700">clinical_notes</span>
            <h4 className="font-bold text-slate-800">Thông số sàng lọc</h4>
          </div>
          <div className="space-y-8">
            <div className="grid grid-cols-4 gap-4">
              <div className="flex flex-col gap-1">
                <label className="text-sm font-semibold text-slate-600">Huyết áp (mmHg)</label>
                <input
                  className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 transition-shadow px-4 outline-none"
                  placeholder="120/80"
                  type="text"
                  value={bloodPressure}
                  onChange={e => { setBloodPressure(e.target.value); evaluateScreening(weight, e.target.value); }}
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-sm font-semibold text-slate-600">Nhịp tim (bpm)</label>
                <input
                  className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 px-4 outline-none"
                  placeholder="75"
                  type="number"
                  value={heartRate}
                  onChange={e => setHeartRate(e.target.value)}
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-sm font-semibold text-slate-600">Cân nặng (kg)</label>
                <input
                  ref={weightRef}
                  className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 px-4 outline-none"
                  placeholder="65"
                  type="number"
                  value={weight}
                  onChange={e => { setWeight(e.target.value); evaluateScreening(e.target.value, bloodPressure); }}
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-sm font-semibold text-slate-600">Nhiệt độ (°C)</label>
                <input
                  className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 px-4 outline-none"
                  placeholder="37.0"
                  type="number"
                  step="0.1"
                  value={temperature}
                  onChange={e => setTemperature(e.target.value)}
                />
              </div>
            </div>

            <div className="pt-4 flex flex-col gap-4">
              <label className="text-sm font-semibold text-slate-600">Kết quả sàng lọc</label>
              <div className="flex gap-4 h-16">
                <label className="w-full flex items-center px-4 border border-slate-200 rounded-xl cursor-pointer hover:bg-slate-50 transition-colors group">
                  <input
                    type="radio"
                    name="result"
                    value="pass"
                    checked={result === 'pass'}
                    onChange={() => { setResult('pass'); setRejectReason(''); }}
                    className="w-5 h-5 text-red-700 focus:ring-red-200 border-slate-300"
                  />
                  <span className="ml-3 text-slate-700 font-medium">Đạt yêu cầu</span>
                  <span className="material-symbols-outlined text-green-600 ml-auto opacity-0 group-hover:opacity-100" style={{ fontVariationSettings: "'FILL' 1" }}>check_circle</span>
                </label>
                <label className="w-full flex items-center px-4 border border-slate-200 rounded-xl cursor-pointer hover:bg-slate-50 transition-colors group">
                  <input
                    type="radio"
                    name="result"
                    value="fail"
                    checked={result === 'fail'}
                    onChange={() => setResult('fail')}
                    className="w-5 h-5 text-red-700 focus:ring-red-200 border-slate-300"
                  />
                  <span className="ml-3 text-slate-700 font-medium">Không đạt</span>
                  <span className="material-symbols-outlined text-red-600 ml-auto opacity-0 group-hover:opacity-100" style={{ fontVariationSettings: "'FILL' 1" }}>cancel</span>
                </label>
              </div>
            </div>

            {result === 'fail' && (
              <div className="pt-4 flex flex-col gap-2">
                <label className="text-sm font-semibold text-slate-600">Lý do từ chối</label>
                <textarea
                  className="w-full h-20 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 px-4 py-2 outline-none resize-none"
                  placeholder="Nhập lý do không đạt yêu cầu..."
                  value={rejectReason}
                  onChange={e => setRejectReason(e.target.value)}
                />
              </div>
            )}
          </div>
        </div>

        {/* Blood Collection */}
        <div className="col-span-5 bg-white border border-slate-200 rounded-2xl p-8 flex flex-col shadow-sm">
          <div className="flex items-center gap-2 mb-8">
            <span className="material-symbols-outlined text-red-700">vaccines</span>
            <h4 className="font-bold text-slate-800">Thu nhận máu</h4>
          </div>
          <div className="space-y-8 flex-1">
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-slate-600">Thể tích hiến máu</label>
              <select
                value={volume}
                onChange={e => setVolume(e.target.value)}
                className="w-full h-11 bg-white border border-slate-200 rounded-xl focus:border-red-400 focus:ring-2 focus:ring-red-200 px-4 outline-none"
              >
                <option value="250">250 ml</option>
                <option value="350">350 ml</option>
                <option value="450">450 ml</option>
              </select>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-slate-600">Mã vạch túi máu</label>
              <div className="relative w-full h-11">
                <input
                  className="w-full h-full bg-slate-50 border border-slate-200 rounded-xl font-mono text-slate-600 cursor-not-allowed px-4 pr-12 outline-none"
                  readOnly
                  type="text"
                  value={bagCode}
                />
                <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl">barcode</span>
              </div>
              <p className="text-[10px] text-slate-400 mt-2 uppercase font-medium">Mã túi máu được hệ thống tự động khởi tạo</p>
            </div>
          </div>
          <div className="mt-8 flex flex-col gap-3 flex-shrink-0">
            <button
              onClick={handleSave}
              className="w-full h-14 bg-red-700 hover:bg-red-800 text-white rounded-xl font-bold transition-all shadow-lg shadow-red-500/20 active:scale-[0.98] flex items-center justify-center gap-2"
            >
              <span className="material-symbols-outlined">save</span>
              LƯU DỮ LIỆU
            </button>
            <button
              onClick={handleReset}
              className="w-full h-11 border border-slate-200 text-slate-500 hover:bg-slate-50 rounded-xl font-semibold transition-all text-sm flex items-center justify-center gap-2"
            >
              <span className="material-symbols-outlined text-lg">restart_alt</span>
              LÀM MỚI BIỂU MẪU
            </button>
          </div>
        </div>

        {/* Blood Inventory */}
        <div className="col-span-12 bg-white border border-slate-200 rounded-2xl p-6 h-[180px] flex flex-col shadow-sm">
          <div className="w-full flex justify-between items-center mb-6">
            <h4 className="text-xs font-black uppercase text-slate-500 tracking-wider">Tình trạng lưu trữ tại điểm hiến máu</h4>
            <span className="text-xs text-red-700 font-bold">{bloodStats.tongSoTui} túi trong kho</span>
          </div>
          <div className="w-full grid grid-cols-8 gap-4 flex-1">
            {bloodBars.map(({ type, height, count }) => (
              <div key={type} className="flex flex-col gap-2">
                <div className="w-full h-16 bg-slate-100 rounded-lg relative overflow-hidden border border-slate-200">
                  <div
                    className="absolute bottom-0 left-0 w-full bg-[#af101a] opacity-80 transition-all duration-700"
                    style={{ height }}
                  ></div>
                  {count > 0 && (
                    <span className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-[10px] font-black text-white mix-blend-overlay">
                      {count}
                    </span>
                  )}
                </div>
                <p className={`text-center text-xs font-bold ${count === 0 ? 'text-slate-400' : 'text-slate-600'}`}>{type}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
        </>
      ) : (
        <div className="space-y-6">
          {/* Stats Cards */}
          <div className="grid grid-cols-3 gap-6">
            <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs uppercase font-semibold text-slate-500">Tổng số</p>
                  <p className="text-3xl font-bold text-slate-800 mt-2">{stats.tongSo}</p>
                </div>
                <span className="material-symbols-outlined text-4xl text-slate-300">description</span>
              </div>
            </div>
            <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs uppercase font-semibold text-slate-500">Đạt yêu cầu</p>
                  <p className="text-3xl font-bold text-green-600 mt-2">{stats.datYeuCau}</p>
                </div>
                <span className="material-symbols-outlined text-4xl text-green-300">check_circle</span>
              </div>
            </div>
            <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs uppercase font-semibold text-slate-500">Không đạt</p>
                  <p className="text-3xl font-bold text-red-600 mt-2">{stats.khongDat}</p>
                </div>
                <span className="material-symbols-outlined text-4xl text-red-300">cancel</span>
              </div>
            </div>
          </div>

          {/* Table */}
          {loading ? (
            <div className="flex justify-center items-center h-40">
              <p className="text-slate-500">Đang tải dữ liệu...</p>
            </div>
          ) : (
            <div className="w-full bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
              <div className="overflow-x-auto">
                <table className="w-full border-collapse text-sm">
                  <thead className="bg-slate-50 border-b border-slate-200">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Mã khám</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Tên TNV</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Chiến dịch</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Huyết áp</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Nhịp tim</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Cân nặng</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Nhiệt độ</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Kết quả</th>
                      <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Ghi chú</th>
                      <th className="px-6 py-3 text-center text-xs font-bold text-slate-600 uppercase">Thao tác</th>
                    </tr>
                  </thead>
                  <tbody>
                    {screeningList.length > 0 ? (
                      screeningList.map((item, idx) => (
                        <tr key={item.maKQ} className={`border-b border-slate-100 ${idx % 2 === 0 ? 'bg-white' : 'bg-slate-50'} hover:bg-slate-100`}>
                          <td className="px-6 py-4 font-semibold text-slate-700">{item.maKQ}</td>
                          <td className="px-6 py-4 text-slate-700">{item.tenTinhNguyenVien}</td>
                          <td className="px-6 py-4 text-slate-600">{item.tenChienDich}</td>
                          <td className="px-6 py-4 text-slate-600">{item.huyetAp}</td>
                          <td className="px-6 py-4 text-slate-600">{item.nhipTim} bpm</td>
                          <td className="px-6 py-4 text-slate-600">{item.canNang} kg</td>
                          <td className="px-6 py-4 text-slate-600">{item.nhietDo}°C</td>
                          <td className="px-6 py-4">
                            <span className={`inline-block px-3 py-1 rounded-full text-xs font-bold ${item.ketQua ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                              {item.ketQua ? 'Đạt' : 'Không đạt'}
                            </span>
                          </td>
                          <td className="px-6 py-4 text-slate-600 text-sm">{item.lyDoTuChoi || '---'}</td>
                          <td className="px-6 py-4 text-center">
                            <button
                              onClick={async () => {
                                if (confirm('Xác nhận xóa kết quả khám này?')) {
                                  try {
                                    await khamLamSangService.delete(item.maKQ);
                                    showToast('Đã xóa kết quả khám');
                                    fetchScreeningList();
                                  } catch {
                                    showToast('Lỗi khi xóa', 'error');
                                  }
                                }
                              }}
                              className="p-1 hover:bg-red-50 text-slate-400 hover:text-red-600 rounded-lg transition-colors"
                            >
                              <span className="material-symbols-outlined text-lg">delete</span>
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="10" className="px-6 py-8 text-center text-slate-500">
                          Không có dữ liệu khám sàng lọc
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

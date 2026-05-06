import React, { useState, useEffect } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import { khaiBaoYTeNvytService } from '../../services/nvytService';

const QUESTIONS = [
  { id: 'dauHong',     text: '1. Bạn có đang cảm thấy mệt mỏi, sốt hoặc đau họng không?' },
  { id: 'khangSinh',  text: '2. Bạn có đang dùng thuốc kháng sinh hay điều trị bệnh nào không?' },
  { id: 'truyenNhiem',text: '3. Trong 6 tháng qua, bạn có mắc bệnh truyền nhiễm hay phẫu thuật không?' },
  { id: 'coThai',     text: '4. Đối với nữ: Bạn có đang trong kỳ kinh nguyệt, mang thai hoặc cho con bú?' },
];

export default function KhaiBaoYTeNVYT() {
  const { nhanVien } = useOutletContext();
  const navigate = useNavigate();

  const [maDon, setMaDon] = useState('');
  const [inputMaDon, setInputMaDon] = useState('');
  const [answers, setAnswers] = useState({ dauHong: false, khangSinh: false, truyenNhiem: false, coThai: false });
  const [moTaKhac, setMoTaKhac] = useState('');
  const [terms, setTerms] = useState(false);
  const [loading, setLoading] = useState(false);
  const [checking, setChecking] = useState(false);
  const [existingRecord, setExistingRecord] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Lấy maDon từ localStorage nếu có (sau khi tạo đơn)
  useEffect(() => {
    const saved = localStorage.getItem('nvyt_maDon');
    if (saved) { setMaDon(saved); setInputMaDon(saved); checkExisting(saved); }
  }, []);

  const checkExisting = async (id) => {
    if (!id) return;
    setChecking(true);
    try {
      const rec = await khaiBaoYTeNvytService.getByMaDon(id);
      if (rec) {
        setExistingRecord(rec);
        setAnswers({
          dauHong: rec.dauHong || false,
          khangSinh: rec.khangSinh || false,
          truyenNhiem: rec.truyenNhiem || false,
          coThai: rec.coThai || false,
        });
        setMoTaKhac(rec.moTaKhac || '');
      } else {
        setExistingRecord(null);
      }
    } catch { setExistingRecord(null); }
    finally { setChecking(false); }
  };

  const handleLoadDon = () => {
    if (!inputMaDon.trim()) return;
    setMaDon(inputMaDon.trim());
    localStorage.setItem('nvyt_maDon', inputMaDon.trim());
    setError(''); setSuccess('');
    checkExisting(inputMaDon.trim());
  };

  const handleSubmit = async () => {
    if (!terms) { setError('Vui lòng xác nhận cam đoan thông tin'); return; }
    if (!maDon) { setError('Vui lòng nhập mã đơn đăng ký'); return; }
    setLoading(true); setError(''); setSuccess('');
    try {
      const payload = { maDon, ...answers, moTaKhac: moTaKhac || null };
      await khaiBaoYTeNvytService.create(payload);
      setSuccess('Đã lưu khai báo y tế thành công!');
      localStorage.removeItem('nvyt_maDon');
      localStorage.removeItem('nvyt_maTNV');
      setTimeout(() => navigate('/nvyt/don-dang-ky'), 2000);
    } catch (e) { setError(e.message || 'Lỗi khi lưu khai báo y tế'); }
    finally { setLoading(false); }
  };

  return (
    <div className="space-y-6 max-w-3xl mx-auto w-full">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">Khai báo y tế</h1>
        <p className="text-slate-500 mt-1 text-sm">Nhập thông tin sức khỏe cho tình nguyện viên theo mã đơn đăng ký</p>
      </div>

      {/* Mã đơn */}
      <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
        <h3 className="font-bold text-slate-800 mb-4 flex items-center gap-2">
          <span className="material-symbols-outlined text-primary">qr_code_scanner</span>
          Nhập mã đơn đăng ký
        </h3>
        <div className="flex gap-3">
          <input
            value={inputMaDon} onChange={e => setInputMaDon(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleLoadDon()}
            placeholder="VD: DON-2024-001"
            className="flex-1 h-11 border border-slate-200 rounded-xl px-4 text-sm font-mono outline-none focus:border-primary focus:ring-2 focus:ring-primary/10"
          />
          <button onClick={handleLoadDon} disabled={checking}
            className="h-11 px-6 bg-primary text-white rounded-xl font-bold text-sm hover:bg-red-800 transition-colors disabled:opacity-60">
            {checking ? 'Đang tải...' : 'Xác nhận'}
          </button>
        </div>
        {maDon && (
          <div className={`mt-3 p-3 rounded-xl border text-sm font-medium flex items-center gap-2
            ${existingRecord ? 'bg-amber-50 border-amber-200 text-amber-800' : 'bg-green-50 border-green-200 text-green-800'}`}>
            <span className="material-symbols-outlined text-base">{existingRecord ? 'info' : 'check_circle'}</span>
            {existingRecord ? 'Đã có khai báo y tế cho đơn này. Bạn có thể xem lại.' : `Đơn ${maDon} hợp lệ. Sẵn sàng khai báo.`}
          </div>
        )}
      </div>

      {/* Form khai báo */}
      {maDon && (
        <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
          <div className="px-6 py-5 border-b border-slate-100 flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center shrink-0">
              <span className="material-symbols-outlined text-primary text-xl" style={{ fontVariationSettings: "'FILL' 1" }}>fact_check</span>
            </div>
            <div>
              <h3 className="font-bold text-slate-800">Phiếu Khai Báo Y Tế & Sức Khỏe</h3>
              <p className="text-xs text-slate-500 mt-0.5">Mã đơn: <span className="font-mono font-bold text-primary">{maDon}</span>
                {nhanVien && <> &nbsp;|&nbsp; NV phụ trách: <span className="font-bold">{nhanVien.hoVaTen}</span></>}
              </p>
            </div>
          </div>

          <div className="p-6 space-y-6">
            {/* Câu hỏi */}
            <div>
              <h4 className="text-sm font-bold text-slate-800 py-2.5 border-l-4 border-primary pl-4 bg-slate-50 mb-3 rounded-r-lg">
                Tình trạng sức khỏe hiện tại
              </h4>
              <div className="border border-slate-100 rounded-xl overflow-hidden divide-y divide-slate-50">
                {QUESTIONS.map((q, idx) => (
                  <div key={q.id} className="flex items-center justify-between p-4 hover:bg-slate-50 transition-colors">
                    <p className="text-sm text-slate-700 pr-4 leading-relaxed">{q.text}</p>
                    <div className="flex gap-5 shrink-0">
                      {[{ val: true, label: 'Có' }, { val: false, label: 'Không' }].map(opt => (
                        <label key={String(opt.val)} className="flex items-center gap-1.5 cursor-pointer">
                          <input
                            type="radio" name={q.id}
                            checked={answers[q.id] === opt.val}
                            onChange={() => setAnswers(p => ({ ...p, [q.id]: opt.val }))}
                            className="text-primary focus:ring-primary"
                          />
                          <span className="text-xs font-semibold text-slate-600">{opt.label}</span>
                        </label>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Mô tả khác */}
            <div className="p-5 bg-slate-50 border border-slate-200 rounded-2xl space-y-4">
              <label className="flex items-start gap-3 cursor-pointer">
                <input type="checkbox" checked={terms} onChange={e => setTerms(e.target.checked)}
                  className="mt-1 w-4 h-4 text-primary rounded focus:ring-primary border-slate-300" />
                <span className="text-sm text-slate-600 leading-relaxed">
                  Tôi cam đoan những thông tin trên là hoàn toàn đúng sự thật. Tình nguyện viên tự nguyện hiến máu và đã hiểu rõ các quyền lợi cũng như rủi ro có thể xảy ra.
                </span>
              </label>
              <div className="pt-3 border-t border-slate-200">
                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-2">Mô tả khác</p>
                <textarea
                  value={moTaKhac} onChange={e => setMoTaKhac(e.target.value)}
                  placeholder="Ghi chú thêm về tình trạng sức khỏe..."
                  rows={3}
                  className="w-full border border-slate-200 bg-white rounded-xl p-3 text-sm outline-none focus:ring-2 focus:ring-primary/10 focus:border-primary resize-none"
                />
              </div>
            </div>

            {/* Lỗi / Thành công */}
            {error && (
              <div className="p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm font-medium flex items-center gap-2">
                <span className="material-symbols-outlined text-base">error</span>{error}
              </div>
            )}
            {success && (
              <div className="p-4 bg-green-50 border border-green-200 rounded-xl text-green-700 text-sm font-bold flex items-center gap-2">
                <span className="material-symbols-outlined text-base">check_circle</span>{success}
              </div>
            )}

            {/* Actions */}
            <div className="flex gap-3">
              <button onClick={() => navigate('/nvyt/don-dang-ky')}
                className="flex-1 h-12 border border-slate-200 text-slate-600 rounded-xl font-semibold text-sm hover:bg-slate-50 transition-colors flex items-center justify-center gap-2">
                <span className="material-symbols-outlined text-xl">arrow_back</span>
                Quay lại
              </button>
              <button onClick={handleSubmit} disabled={loading || !!success}
                className="flex-1 h-12 bg-primary text-white rounded-xl font-bold text-sm hover:bg-red-800 transition-all shadow-lg shadow-primary/20 active:scale-[0.98] disabled:opacity-60 flex items-center justify-center gap-2">
                {loading ? (
                  <><span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" /> Đang lưu...</>
                ) : (
                  <><span className="material-symbols-outlined text-xl">save</span> Lưu khai báo y tế</>
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

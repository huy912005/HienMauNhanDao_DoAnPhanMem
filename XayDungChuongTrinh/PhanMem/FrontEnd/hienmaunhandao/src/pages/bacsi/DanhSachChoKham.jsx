import React, { useState, useEffect, useCallback } from 'react';
import { useOutletContext } from 'react-router-dom';
import { donDangKyNvytService } from '../../services/nvytService';

const PAGE_SIZE = 10;

// ─── Trang chính ──────────────────────────────────────────────────────────────
export default function DanhSachChoKham() {
  const { nhanVien } = useOutletContext();
  const [dons, setDons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = 'success') => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3500);
  };

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const res = await donDangKyNvytService.getAll(page, PAGE_SIZE);
      const content = Array.isArray(res) ? res : (res.content || []);
      setDons(content);
      setTotalPages(res.totalPages || 1);
    } catch { showToast('Lỗi khi tải danh sách chờ khám', 'error'); }
    finally { setLoading(false); }
  }, [page]);

  useEffect(() => { loadData(); }, [loadData]);

  return (
    <div className="space-y-6">
      {/* Toast */}
      {toast && (
        <div className={`fixed top-6 right-6 z-[100] px-5 py-3 rounded-xl shadow-lg text-white text-sm font-bold flex items-center gap-2 transition-all
          ${toast.type === 'error' ? 'bg-red-600' : toast.type === 'warning' ? 'bg-amber-500' : 'bg-green-600'}`}>
          <span className="material-symbols-outlined text-lg">{toast.type === 'error' ? 'error' : toast.type === 'warning' ? 'warning' : 'check_circle'}</span>
          {toast.msg}
        </div>
      )}

      {/* Page header */}
      <div>
        <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">Danh sách chờ khám</h1>
        <p className="text-slate-500 mt-1 text-sm">Các tình nguyện viên đã đăng ký hiến máu</p>
      </div>

      {/* Table */}
      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-200">
                {['Mã đơn', 'Tình nguyện viên', 'Chiến dịch', 'Thể tích', 'Trạng thái', 'Thời gian đăng ký'].map(h => (
                  <th key={h} className="text-left px-5 py-3 text-xs font-black uppercase text-slate-400 tracking-wider whitespace-nowrap">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={6} className="text-center py-16 text-slate-400">
                  <div className="flex flex-col items-center gap-3">
                    <div className="w-8 h-8 border-3 border-primary border-t-transparent rounded-full animate-spin" />
                    <span className="text-sm">Đang tải dữ liệu...</span>
                  </div>
                </td></tr>
              ) : dons.length === 0 ? (
                <tr><td colSpan={6} className="text-center py-16 text-slate-400">
                  <span className="material-symbols-outlined text-5xl block mb-2">inbox</span>
                  Không có tình nguyện viên nào trong danh sách chờ khám
                </td></tr>
              ) : dons.map(don => (
                <tr key={don.maDon} className="border-b border-slate-100 hover:bg-slate-50/80 transition-colors">
                  <td className="px-5 py-4">
                    <span className="font-mono text-xs font-bold text-primary bg-red-50 px-2 py-1 rounded-lg">{don.maDon}</span>
                  </td>
                  <td className="px-5 py-4">
                    <p className="font-semibold text-slate-800">{don.tinhNguyenVien?.hoVaTen || don.maTNV || '---'}</p>
                    <p className="text-xs text-slate-400 mt-0.5">{don.tinhNguyenVien?.soCCCD || ''}</p>
                  </td>
                  <td className="px-5 py-4 font-mono text-xs text-slate-600">{don.maChienDich || '---'}</td>
                  <td className="px-5 py-4">
                    <span className="px-2 py-1 bg-blue-50 text-blue-700 text-xs font-bold rounded-lg">{don.theTich || '---'} ml</span>
                  </td>
                  <td className="px-5 py-4">
                    <span className={`px-2.5 py-1 text-xs font-bold rounded-full
                      ${don.trangThai === 'DA_KHAM' ? 'bg-green-100 text-green-700' :
                        don.trangThai === 'CHO_KHAM' ? 'bg-amber-100 text-amber-700' :
                        don.trangThai === 'DA_DANG_KY' ? 'bg-blue-100 text-blue-700' :
                        'bg-slate-100 text-slate-600'}`}>
                      {don.trangThai || 'Chờ xử lý'}
                    </span>
                  </td>
                  <td className="px-5 py-4 text-xs text-slate-500">
                    {don.thoiGianDangKy ? new Date(don.thoiGianDangKy).toLocaleString('vi-VN') : '---'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between px-5 py-4 border-t border-slate-100 bg-slate-50">
            <p className="text-xs text-slate-500">Trang {page + 1} / {totalPages}</p>
            <div className="flex gap-2">
              <button onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}
                className="w-9 h-9 rounded-lg border border-slate-200 flex items-center justify-center disabled:opacity-40 hover:bg-slate-100 transition-colors">
                <span className="material-symbols-outlined text-lg">chevron_left</span>
              </button>
              {Array.from({ length: totalPages }, (_, i) => (
                <button key={i} onClick={() => setPage(i)}
                  className={`w-9 h-9 rounded-lg text-sm font-bold transition-colors
                    ${i === page ? 'bg-primary text-white' : 'border border-slate-200 text-slate-600 hover:bg-slate-100'}`}>
                  {i + 1}
                </button>
              ))}
              <button onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))} disabled={page === totalPages - 1}
                className="w-9 h-9 rounded-lg border border-slate-200 flex items-center justify-center disabled:opacity-40 hover:bg-slate-100 transition-colors">
                <span className="material-symbols-outlined text-lg">chevron_right</span>
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

import React, { useState, useEffect } from 'react';
import http from '../../utils/http';

const QuanLyNhapKho = () => {
  const [barcode, setBarcode] = useState('');
  const [scannedUnits, setScannedUnits] = useState([]);
  const [loading, setLoading] = useState(false);
  const [importing, setImporting] = useState(false);
  const [scanMsg, setScanMsg] = useState(null); // { type: 'success'|'error', text }

  // Danh sách túi máu đã có trong kho
  const [inventoryList, setInventoryList] = useState([]);
  const [inventoryLoading, setInventoryLoading] = useState(true);
  const [searchKho, setSearchKho] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Load danh sách túi máu trong kho
  const fetchInventory = async (page = 0, search = '') => {
    try {
      setInventoryLoading(true);
      const res = await http.get(`/tuimau/blood-units?page=${page}&size=10&search=${search}&status=Nhập kho`);
      setInventoryList(res.content || []);
      setTotalPages(res.totalPages || 0);
      setCurrentPage(page);
    } catch (err) {
      console.error('Lỗi load kho:', err);
    } finally {
      setInventoryLoading(false);
    }
  };

  useEffect(() => {
    fetchInventory(0, '');
  }, []);

  // Xử lý quét mã vạch
  const handleScan = async (e) => {
    e.preventDefault();
    if (!barcode.trim()) return;
    if (scannedUnits.find(u => u.maTuiMau === barcode.trim())) {
      setScanMsg({ type: 'error', text: `Túi máu "${barcode.trim()}" đã có trong danh sách chờ nhập.` });
      setBarcode('');
      return;
    }
    try {
      setLoading(true);
      setScanMsg(null);
      const response = await http.get(`/tuimau/scan/${barcode.trim()}`);
      setScannedUnits([...scannedUnits, response]);
      setScanMsg({ type: 'success', text: `✓ Thêm túi máu "${response.maTuiMau}" (${response.nhomMau}) vào danh sách thành công!` });
      setBarcode('');
    } catch (error) {
      const msg = error.response?.data?.message || `Không tìm thấy túi máu với mã "${barcode.trim()}". Vui lòng kiểm tra lại mã.`;
      setScanMsg({ type: 'error', text: `✗ ${msg}` });
    } finally {
      setLoading(false);
    }
  };

  const removeUnit = (maTuiMau) => {
    setScannedUnits(scannedUnits.filter(u => u.maTuiMau !== maTuiMau));
  };

  const handleImport = async () => {
    if (scannedUnits.length === 0) return;
    if (!confirm(`Xác nhận nhập kho ${scannedUnits.length} túi máu này?`)) return;
    try {
      setImporting(true);
      const maNhanVien = localStorage.getItem('maNhanVien');
      if (!maNhanVien) {
        alert('Phiên đăng nhập hết hạn hoặc không tìm thấy thông tin nhân viên. Vui lòng đăng nhập lại.');
        return;
      }
      await http.post('/phieunhapxuat/import', {
        maNhanVien,
        maTuiMauList: scannedUnits.map(u => u.maTuiMau)
      });
      alert('Nhập kho thành công!');
      setScannedUnits([]);
      fetchInventory(0, searchKho); // Reload danh sách kho
    } catch (error) {
      alert('Có lỗi xảy ra khi lưu phiếu nhập kho.');
    } finally {
      setImporting(false);
    }
  };

  const handleDeleteInventory = async (maTuiMau) => {
    if (!confirm(`Bạn có chắc chắn muốn xóa/hủy túi máu ${maTuiMau} khỏi kho không?`)) return;
    try {
      await http.delete(`/tuimau/blood-units/${maTuiMau}`);
      alert('Đã xóa túi máu thành công!');
      fetchInventory(currentPage, searchKho);
    } catch (error) {
      console.error('Lỗi khi xóa túi máu:', error);
      alert('Không thể xóa túi máu này.');
    }
  };

  const getTinhTrangBadge = (tt) => {
    if (!tt) return <span className="px-2 py-0.5 bg-slate-100 text-slate-500 rounded-full text-xs">Chưa xác định</span>;
    if (tt === 'Sắp hết hạn') return <span className="px-2 py-0.5 bg-yellow-100 text-yellow-700 rounded-full text-xs font-medium">⚠ Sắp hết hạn</span>;
    if (tt === 'Hết hạn') return <span className="px-2 py-0.5 bg-red-100 text-red-700 rounded-full text-xs font-medium">✕ Hết hạn</span>;
    return <span className="px-2 py-0.5 bg-green-100 text-green-700 rounded-full text-xs font-medium">✓ Còn hạn</span>;
  };

  return (
    <div className="bg-slate-50 min-h-screen p-8 text-slate-800">
      <div className="max-w-6xl mx-auto space-y-6">

        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Quản lý nhập kho</h1>
          <p className="text-slate-500 mt-1">Quét mã vạch trên túi máu để đưa vào kho lưu trữ.</p>
        </div>

        {/* Form quét mã */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <form onSubmit={handleScan} className="flex gap-4 items-end">
            <div className="flex-1">
              <label className="block text-sm font-bold text-slate-700 mb-2">Quét mã vạch túi máu</label>
              <div className="relative">
                <span className="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">qr_code_scanner</span>
                <input
                  type="text" value={barcode}
                  onChange={(e) => { setBarcode(e.target.value); setScanMsg(null); }}
                  placeholder="Nhập hoặc quét mã vạch (Vd: TM00001)..."
                  className={`w-full h-12 pl-12 pr-4 bg-slate-50 border rounded-xl focus:ring-2 outline-none transition-all ${
                    scanMsg?.type === 'error' ? 'border-red-400 focus:ring-red-400'
                    : scanMsg?.type === 'success' ? 'border-green-400 focus:ring-green-400'
                    : 'border-slate-200 focus:ring-red-500'
                  }`}
                  autoFocus
                />
              </div>
              {/* Thông báo inline */}
              {scanMsg && (
                <div className={`mt-2 px-4 py-2 rounded-lg text-sm font-medium ${
                  scanMsg.type === 'error'
                    ? 'bg-red-50 text-red-700 border border-red-200'
                    : 'bg-green-50 text-green-700 border border-green-200'
                }`}>
                  {scanMsg.text}
                </div>
              )}
            </div>
            <button type="submit" disabled={loading}
              className="h-12 px-6 bg-[#af101a] text-white rounded-xl font-bold hover:bg-red-800 disabled:opacity-50">
              {loading ? 'Đang quét...' : 'Thêm vào danh sách'}
            </button>
          </form>
        </div>

        {/* Danh sách chờ nhập */}
        {scannedUnits.length > 0 && (
          <div className="bg-white rounded-2xl border border-emerald-200 shadow-sm overflow-hidden">
            <div className="p-4 border-b border-emerald-100 flex justify-between items-center bg-emerald-50">
              <h3 className="font-bold text-emerald-700">Danh sách chờ nhập kho ({scannedUnits.length})</h3>
              <button onClick={handleImport} disabled={importing}
                className="h-10 px-6 bg-green-600 text-white rounded-xl font-bold hover:bg-green-700 disabled:opacity-50 flex items-center gap-2">
                <span className="material-symbols-outlined text-sm">save</span>
                {importing ? 'Đang lưu...' : 'Xác nhận nhập kho'}
              </button>
            </div>
            <table className="w-full text-left">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Mã túi máu</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Chiến dịch</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Nhóm máu</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Thể tích (ml)</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Ngày thu nhận</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Ngày hết hạn</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase">Tình trạng HSD</th>
                  <th className="px-6 py-3 text-xs font-bold text-slate-500 uppercase text-right">Xóa</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {scannedUnits.map((unit, idx) => (
                  <tr key={idx} className="hover:bg-slate-50">
                    <td className="px-6 py-4 font-bold text-slate-700">{unit.maTuiMau}</td>
                    <td className="px-6 py-4 text-sm text-slate-500 font-semibold">{unit.maChienDich || 'N/A'}</td>
                    <td className="px-6 py-4">
                      <span className="px-3 py-1 bg-red-100 text-red-800 rounded-full text-xs font-bold">{unit.nhomMau}</span>
                    </td>
                    <td className="px-6 py-4 text-sm text-slate-600">{unit.theTich ?? '—'}</td>
                    <td className="px-6 py-4 text-sm text-slate-600">
                      {unit.ngayThuNhan ? new Date(unit.ngayThuNhan).toLocaleDateString('vi-VN') : '—'}
                    </td>
                    <td className="px-6 py-4 text-sm text-slate-600">
                      {unit.ngayHetHan ? new Date(unit.ngayHetHan).toLocaleDateString('vi-VN') : '—'}
                    </td>
                    <td className="px-6 py-4">{getTinhTrangBadge(unit.tinhTrangHSD)}</td>
                    <td className="px-6 py-4 text-right">
                      <button onClick={() => removeUnit(unit.maTuiMau)} className="text-slate-400 hover:text-red-600">
                        <span className="material-symbols-outlined">delete</span>
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* ===== BẢNG TÚI MÁU HIỆN CÓ TRONG KHO ===== */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="p-4 border-b border-slate-100 flex justify-between items-center bg-slate-50">
            <h3 className="font-bold text-slate-700">
              <span className="material-symbols-outlined text-sm align-middle mr-1 text-[#af101a]">inventory_2</span>
              Túi máu hiện có trong kho
            </h3>
            <div className="flex gap-2">
              <input
                type="text" value={searchKho}
                onChange={e => setSearchKho(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && fetchInventory(0, searchKho)}
                placeholder="Tìm mã túi / nhóm máu..."
                className="h-9 px-3 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-red-400 w-52"
              />
              <button onClick={() => fetchInventory(0, searchKho)}
                className="h-9 px-4 bg-slate-700 text-white rounded-lg text-sm hover:bg-slate-800">
                Tìm
              </button>
            </div>
          </div>

          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-slate-100 bg-slate-50">
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Mã túi</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Chiến dịch</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Nhóm máu</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Thể tích (ml)</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Ngày thu nhận</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Ngày hết hạn</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Trạng thái</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase">Tình trạng HSD</th>
                <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase text-right">Thao tác</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {inventoryLoading ? (
                <tr><td colSpan="8" className="px-6 py-10 text-center text-slate-400">Đang tải dữ liệu kho...</td></tr>
              ) : inventoryList.length === 0 ? (
                <tr><td colSpan="8" className="px-6 py-10 text-center text-slate-400">Không tìm thấy túi máu nào trong kho.</td></tr>
              ) : (
                inventoryList.map((unit, idx) => (
                  <tr key={idx} className="hover:bg-slate-50">
                    <td className="px-5 py-3 font-mono font-bold text-slate-700 text-sm">{unit.maTuiMau}</td>
                    <td className="px-5 py-3 text-xs text-slate-500 font-semibold">{unit.maChienDich || 'N/A'}</td>
                    <td className="px-5 py-3">
                      <span className="px-2 py-0.5 bg-red-100 text-red-800 rounded-full text-xs font-bold">{unit.nhomMau}</span>
                    </td>
                    <td className="px-5 py-3 text-sm text-slate-600">{unit.theTich ?? '—'}</td>
                    <td className="px-5 py-3 text-sm text-slate-600">
                      {unit.ngayThuNhan ? new Date(unit.ngayThuNhan).toLocaleDateString('vi-VN') : '—'}
                    </td>
                    <td className="px-5 py-3 text-sm text-slate-600">
                      {unit.ngayHetHan ? new Date(unit.ngayHetHan).toLocaleDateString('vi-VN') : '—'}
                    </td>
                    <td className="px-5 py-3">
                      <span className="px-2 py-0.5 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">{unit.trangThai}</span>
                    </td>
                    <td className="px-5 py-3">{getTinhTrangBadge(unit.tinhTrangHSD)}</td>
                    <td className="px-5 py-3 text-right">
                      <button 
                        onClick={() => handleDeleteInventory(unit.maTuiMau)}
                        className="text-slate-400 hover:text-red-600 transition-colors"
                        title="Xóa túi máu khỏi kho"
                      >
                        <span className="material-symbols-outlined">delete</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="p-4 border-t border-slate-100 flex justify-center gap-2">
              <button onClick={() => fetchInventory(currentPage - 1, searchKho)} disabled={currentPage === 0}
                className="px-3 py-1 rounded-lg border text-sm disabled:opacity-40 hover:bg-slate-100">← Trước</button>
              <span className="px-3 py-1 text-sm text-slate-500">Trang {currentPage + 1} / {totalPages}</span>
              <button onClick={() => fetchInventory(currentPage + 1, searchKho)} disabled={currentPage >= totalPages - 1}
                className="px-3 py-1 rounded-lg border text-sm disabled:opacity-40 hover:bg-slate-100">Sau →</button>
            </div>
          )}
        </div>

      </div>
    </div>
  );
};

export default QuanLyNhapKho;

import React, { useState } from 'react';
import http from '../utils/http';

const QuanLyNhapKho = () => {
  const [barcode, setBarcode] = useState('');
  const [scannedUnits, setScannedUnits] = useState([]);
  const [loading, setLoading] = useState(false);
  const [importing, setImporting] = useState(false);

  // Xử lý quét mã vạch
  const handleScan = async (e) => {
    e.preventDefault();
    if (!barcode.trim()) return;

    // Kiểm tra xem đã quét chưa
    if (scannedUnits.find(u => u.maTuiMau === barcode.trim())) {
      alert('Túi máu này đã được quét vào danh sách.');
      setBarcode('');
      return;
    }

    try {
      setLoading(true);
      // Gọi API lấy thông tin túi máu
      const response = await http.get(`/api/tuimau/scan/${barcode.trim()}`);
      
      // Thêm vào danh sách tạm
      setScannedUnits([...scannedUnits, response]);
      setBarcode('');
    } catch (error) {
      console.error('Lỗi khi quét mã:', error);
      alert(error.response?.data?.message || 'Túi máu không hợp lệ hoặc đã được nhập kho.');
    } finally {
      setLoading(false);
    }
  };

  // Xóa khỏi danh sách tạm
  const removeUnit = (maTuiMau) => {
    setScannedUnits(scannedUnits.filter(u => u.maTuiMau !== maTuiMau));
  };

  // Gửi API lưu phiếu nhập kho
  const handleImport = async () => {
    if (scannedUnits.length === 0) return;
    
    if (!confirm(`Xác nhận nhập kho ${scannedUnits.length} túi máu này?`)) return;

    try {
      setImporting(true);
      // Lấy mã nhân viên đang đăng nhập (tạm thời fix cứng hoặc lấy từ localStorage nếu có)
      // Chú ý: Ở đây bạn cần truyền mã nhân viên thật, ví dụ "NV001".
      const maNhanVien = localStorage.getItem('maNhanVien') || 'NV01'; 

      const payload = {
        maNhanVien: maNhanVien,
        maTuiMauList: scannedUnits.map(u => u.maTuiMau)
      };

      await http.post('/api/phieunhapxuat/import', payload);
      alert('Nhập kho thành công!');
      setScannedUnits([]); // Reset danh sách
    } catch (error) {
      console.error('Lỗi khi nhập kho:', error);
      alert('Có lỗi xảy ra khi lưu phiếu nhập kho.');
    } finally {
      setImporting(false);
    }
  };

  return (
    <div className="bg-slate-50 min-h-screen p-8 text-slate-800">
      <div className="max-w-5xl mx-auto space-y-6">
        
        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Quản lý nhập kho</h1>
          <p className="text-slate-500 mt-1">Quét mã vạch trên túi máu để đưa vào kho lưu trữ.</p>
        </div>

        {/* Khung quét mã vạch */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <form onSubmit={handleScan} className="flex gap-4 items-end">
            <div className="flex-1">
              <label className="block text-sm font-bold text-slate-700 mb-2">Quét mã vạch túi máu</label>
              <div className="relative">
                <span className="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">qr_code_scanner</span>
                <input
                  type="text"
                  value={barcode}
                  onChange={(e) => setBarcode(e.target.value)}
                  placeholder="Nhập hoặc quét mã vạch (Vd: TM001)..."
                  className="w-full h-12 pl-12 pr-4 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-red-500 outline-none"
                  autoFocus
                />
              </div>
            </div>
            <button 
              type="submit" 
              disabled={loading}
              className="h-12 px-6 bg-[#af101a] text-white rounded-xl font-bold hover:bg-red-800 disabled:opacity-50"
            >
              {loading ? 'Đang quét...' : 'Thêm vào danh sách'}
            </button>
          </form>
        </div>

        {/* Bảng danh sách chờ nhập */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="p-4 border-b border-slate-100 flex justify-between items-center bg-slate-50">
            <h3 className="font-bold text-slate-700">Danh sách chờ nhập kho ({scannedUnits.length})</h3>
            {scannedUnits.length > 0 && (
              <button 
                onClick={handleImport}
                disabled={importing}
                className="h-10 px-6 bg-green-600 text-white rounded-xl font-bold hover:bg-green-700 disabled:opacity-50 flex items-center gap-2"
              >
                <span className="material-symbols-outlined text-sm">save</span>
                {importing ? 'Đang lưu...' : 'Xác nhận nhập kho'}
              </button>
            )}
          </div>
          
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-slate-100">
                <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase">Mã túi máu</th>
                <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase">Nhóm máu</th>
                <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase">Ngày thu nhận</th>
                <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase text-right">Hành động</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {scannedUnits.length === 0 ? (
                <tr>
                  <td colSpan="4" className="px-6 py-10 text-center text-slate-400">
                    Chưa có túi máu nào được quét.
                  </td>
                </tr>
              ) : (
                scannedUnits.map((unit, idx) => (
                  <tr key={idx} className="hover:bg-slate-50">
                    <td className="px-6 py-4 font-bold text-slate-700">{unit.maTuiMau}</td>
                    <td className="px-6 py-4">
                      <span className="px-3 py-1 bg-red-100 text-red-800 rounded-full text-xs font-bold">
                        {unit.nhomMau}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-slate-600">
                      {unit.ngayThuNhan ? new Date(unit.ngayThuNhan).toLocaleDateString('vi-VN') : 'Không rõ'}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button 
                        onClick={() => removeUnit(unit.maTuiMau)}
                        className="text-slate-400 hover:text-red-600"
                      >
                        <span className="material-symbols-outlined">delete</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

      </div>
    </div>
  );
};

export default QuanLyNhapKho;

import React, { useState, useEffect } from 'react';
import { useOutletContext } from 'react-router-dom';
import { thuNhanMauService } from '../../services/khamLamSangService';

export default function ThuNhanMau() {
  const { nhanVien } = useOutletContext();
  const [collectionList, setCollectionList] = useState([]);
  const [stats, setStats] = useState({
    tongSoTui: 0,
    tongTheTich: 0,
    theoNhomMau: {},
    theoTheTich: {},
  });
  const [loading, setLoading] = useState(false);
  const [showList, setShowList] = useState(true); // Mặc định hiển thị danh sách
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = 'success') => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3500);
  };

  // Fetch danh sách túi máu từ API
  const fetchCollectionList = async () => {
    try {
      setLoading(true);
      const [dataRes, statsRes] = await Promise.all([
        thuNhanMauService.getAll(),
        thuNhanMauService.getStats(),
      ]);
      setCollectionList(Array.isArray(dataRes) ? dataRes : (dataRes.data || []));
      setStats(statsRes.data || statsRes || {
        tongSoTui: 0,
        tongTheTich: 0,
        theoNhomMau: {},
        theoTheTich: {},
      });
    } catch (error) {
      console.error('Lỗi khi lấy dữ liệu:', error);
      showToast('Lỗi khi tải dữ liệu', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCollectionList();
  }, []);

  const getStatusColor = (status) => {
    switch (status) {
      case 'Chờ xét nghiệm':
        return 'bg-yellow-100 text-yellow-700';
      case 'Nhập kho':
        return 'bg-blue-100 text-blue-700';
      case 'Đã xuất':
        return 'bg-green-100 text-green-700';
      case 'Hủy':
        return 'bg-red-100 text-red-700';
      default:
        return 'bg-slate-100 text-slate-700';
    }
  };

  const BLOOD_TYPES_LIST = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];
  const maxInStock = Math.max(...Object.values(stats.theoNhomMau || {}), 10);

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
          <h1 className="text-2xl font-extrabold text-slate-800 tracking-tight">Thu nhận máu</h1>
          <p className="text-slate-500 mt-1 text-sm">Quản lý túi máu được thu nhận từ tình nguyện viên</p>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-2 gap-6">
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm hover:shadow-md transition-shadow">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs uppercase font-semibold text-slate-500">Tổng số túi máu</p>
              <p className="text-3xl font-bold text-slate-800 mt-2">{stats.tongSoTui}</p>
            </div>
            <div className="w-12 h-12 bg-red-50 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-3xl text-red-500">vaccines</span>
            </div>
          </div>
        </div>
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm hover:shadow-md transition-shadow">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs uppercase font-semibold text-slate-500">Tổng thể tích (ml)</p>
              <p className="text-3xl font-bold text-red-600 mt-2">{Math.round(stats.tongTheTich)}</p>
            </div>
            <div className="w-12 h-12 bg-blue-50 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-3xl text-blue-500">water_drop</span>
            </div>
          </div>
        </div>
      </div>

      {/* Blood Type & Volume Stats */}
      <div className="grid grid-cols-2 gap-6">
        {/* By Blood Type */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
          <h3 className="font-bold text-slate-800 mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-red-500">bloodtype</span>
            Thống kê theo nhóm máu
          </h3>
          <div className="grid grid-cols-4 gap-2">
            {BLOOD_TYPES_LIST.map((nhomMau) => (
              <div key={nhomMau} className="p-3 bg-red-50/50 border border-red-100 rounded-xl text-center">
                <p className="text-[10px] uppercase font-bold text-slate-400">{nhomMau}</p>
                <p className="text-xl font-black text-red-700">{stats.theoNhomMau?.[nhomMau] || 0}</p>
                <p className="text-[10px] text-slate-500 font-medium">túi</p>
              </div>
            ))}
          </div>
        </div>

        {/* By Volume */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm">
          <h3 className="font-bold text-slate-800 mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-blue-500">database</span>
            Thống kê theo thể tích
          </h3>
          <div className="grid grid-cols-3 gap-2">
            {['250', '350', '450'].map((theTich) => (
              <div key={theTich} className="p-3 bg-blue-50/50 border border-blue-100 rounded-xl text-center">
                <p className="text-[10px] uppercase font-bold text-slate-400">{theTich} ML</p>
                <p className="text-xl font-black text-blue-700">{stats.theoTheTich?.[theTich] || 0}</p>
                <p className="text-[10px] text-slate-500 font-medium">túi</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Inventory visual */}
      <div className="bg-white border border-slate-200 rounded-2xl p-6 h-[180px] flex flex-col shadow-sm">
        <div className="w-full flex justify-between items-center mb-6">
          <h4 className="text-xs font-black uppercase text-slate-500 tracking-wider flex items-center gap-2">
            <span className="w-2 h-2 bg-red-500 rounded-full animate-pulse"></span>
            Tình trạng lưu trữ tại điểm hiến máu
          </h4>
          <span className="text-xs text-red-700 font-bold bg-red-50 px-3 py-1 rounded-full border border-red-100">
            {stats.tongSoTui} túi trong kho
          </span>
        </div>
        <div className="w-full grid grid-cols-8 gap-4 flex-1">
          {BLOOD_TYPES_LIST.map((bt) => {
            const count = stats.theoNhomMau?.[bt] || 0;
            const fillHeight = `${Math.min((count / maxInStock) * 100, 100)}%`;
            return (
              <div key={bt} className="flex flex-col gap-2">
                <div className="w-full h-16 bg-slate-100 rounded-lg relative overflow-hidden border border-slate-200">
                  <div 
                    className="absolute bottom-0 left-0 w-full bg-gradient-to-t from-red-700 to-red-500 transition-all duration-700 ease-out" 
                    style={{ height: count > 0 ? fillHeight : '0%' }} 
                  />
                  {count > 0 && (
                    <span className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-[10px] font-black text-white mix-blend-overlay">
                      {count}
                    </span>
                  )}
                </div>
                <p className={`text-center text-xs font-bold ${count === 0 ? 'text-slate-300' : 'text-slate-600'}`}>
                  {bt}
                </p>
              </div>
            );
          })}
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
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Mã túi</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Tên TNV</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Nhóm máu</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Thể tích (ml)</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Chiến dịch</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Thời gian lấy</th>
                  <th className="px-6 py-3 text-left text-xs font-bold text-slate-600 uppercase">Trạng thái</th>
                  <th className="px-6 py-3 text-center text-xs font-bold text-slate-600 uppercase">Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {collectionList.length > 0 ? (
                  collectionList.map((item, idx) => (
                    <tr key={item.maTuiMau} className={`border-b border-slate-100 ${idx % 2 === 0 ? 'bg-white' : 'bg-slate-50'} hover:bg-slate-100`}>
                      <td className="px-6 py-4 font-semibold text-slate-700">{item.maTuiMau}</td>
                      <td className="px-6 py-4 text-slate-700">{item.tenTinhNguyenVien}</td>
                      <td className="px-6 py-4 text-slate-600">
                        <span className="font-bold text-red-700">{item.nhomMau}</span>
                      </td>
                      <td className="px-6 py-4 text-slate-600">{item.theTich}</td>
                      <td className="px-6 py-4 text-slate-600">{item.tenChienDich}</td>
                      <td className="px-6 py-4 text-slate-600">
                        {item.thoiGianLayMau
                          ? new Date(item.thoiGianLayMau).toLocaleString('vi-VN')
                          : '---'}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-block px-3 py-1 rounded-full text-xs font-bold ${getStatusColor(item.trangThai)}`}>
                          {item.trangThai}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-center">
                        <div className="flex justify-center gap-1">
                          {item.trangThai === 'Chờ xét nghiệm' && (
                            <button 
                              onClick={async () => {
                                try {
                                  await thuNhanMauService.updateStatus(item.maTuiMau, 'Nhập kho');
                                  showToast('Đã cập nhật trạng thái: Nhập kho');
                                  fetchCollectionList();
                                } catch {
                                  showToast('Lỗi khi cập nhật', 'error');
                                }
                              }}
                              title="Xác nhận nhập kho"
                              className="p-1 hover:bg-blue-50 text-slate-400 hover:text-blue-600 rounded-lg transition-colors"
                            >
                              <span className="material-symbols-outlined text-lg">inventory_2</span>
                            </button>
                          )}
                          <button 
                            onClick={async () => {
                              if (confirm('Xác nhận xóa túi máu này?')) {
                                try {
                                  await thuNhanMauService.delete(item.maTuiMau);
                                  showToast('Đã xóa túi máu');
                                  fetchCollectionList();
                                } catch {
                                  showToast('Lỗi khi xóa', 'error');
                                }
                              }
                            }}
                            title="Xóa túi máu"
                            className="p-1 hover:bg-red-50 text-slate-400 hover:text-red-600 rounded-lg transition-colors"
                          >
                            <span className="material-symbols-outlined text-lg">delete</span>
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7" className="px-6 py-8 text-center text-slate-500">
                      Không có dữ liệu túi máu
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

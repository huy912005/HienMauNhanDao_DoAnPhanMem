import React, { useState, useEffect } from 'react';
import http from "../../utils/http";

const QuanLyHanDung = () => {
    const [viewMode, setViewMode] = useState('all'); // all, expired, near
    const [loading, setLoading] = useState(false);
    const [stats, setStats] = useState({
        expiredCount: 0,
        nearExpiryCount: 0,
        safeCount: 0
    });
    const [bloodUnits, setBloodUnits] = useState([]);

    // Mock loading state (dữ liệu để trống theo yêu cầu)
    useEffect(() => {
        // Sau này sẽ gọi API tại đây
        // http.get('/tuimau/expiry-stats').then(res => setStats(res.data));
    }, []);

    const handleExportReport = () => {
        alert("Chức năng xuất báo cáo đang được xây dựng dựa trên chế độ xem: " + viewMode);
    };

    const handleDeleteExpired = () => {
        if (window.confirm("Bạn có chắc chắn muốn TIÊU HỦY tất cả các túi máu đã hết hạn không?")) {
            alert("Đang thực hiện tiêu hủy...");
        }
    };

    return (
        <div className="bg-gray-50 text-slate-800 antialiased min-h-screen">
            <main className="p-8 space-y-6">
                
                {/* Page Header */}
                <div>
                    <h1 className="text-2xl font-bold text-slate-900">Quản lý Hạn dùng & Tiêu hủy</h1>
                    <p className="text-slate-500 text-sm mt-1">Theo dõi vòng đời và quản lý tiêu hủy các túi máu dựa trên thời gian thu nhận.</p>
                </div>

                {/* Summary Stats Area */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center gap-5">
                        <div className="w-14 h-14 bg-red-50 text-red-600 rounded-2xl flex items-center justify-center">
                            <span className="material-symbols-outlined text-3xl font-variation-fill">dangerous</span>
                        </div>
                        <div>
                            <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Đã hết hạn</p>
                            <h3 className="text-2xl font-black text-red-600">{stats.expiredCount} túi</h3>
                        </div>
                    </div>
                    <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center gap-5">
                        <div className="w-14 h-14 bg-orange-50 text-orange-600 rounded-2xl flex items-center justify-center">
                            <span className="material-symbols-outlined text-3xl">warning</span>
                        </div>
                        <div>
                            <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Sắp hết hạn (&lt; 7 ngày)</p>
                            <h3 className="text-2xl font-black text-orange-600">{stats.nearExpiryCount} túi</h3>
                        </div>
                    </div>
                    <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center gap-5">
                        <div className="w-14 h-14 bg-green-50 text-green-600 rounded-2xl flex items-center justify-center">
                            <span className="material-symbols-outlined text-3xl">check_circle</span>
                        </div>
                        <div>
                            <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">An toàn</p>
                            <h3 className="text-2xl font-black text-green-600">{stats.safeCount} túi</h3>
                        </div>
                    </div>
                </div>

                {/* Controls & Tabs */}
                <div className="flex flex-wrap items-center justify-between gap-4">
                    {/* Tabs */}
                    <div className="flex bg-slate-200 p-1 rounded-xl w-fit">
                        <button 
                            onClick={() => setViewMode('all')}
                            className={`px-6 py-2 rounded-lg text-sm font-bold transition-all ${viewMode === 'all' ? 'bg-white text-primary shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
                        >
                            Tổng quan
                        </button>
                        <button 
                            onClick={() => setViewMode('expired')}
                            className={`px-6 py-2 rounded-lg text-sm font-bold transition-all ${viewMode === 'expired' ? 'bg-white text-primary shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
                        >
                            Đã hết hạn
                        </button>
                        <button 
                            onClick={() => setViewMode('near')}
                            className={`px-6 py-2 rounded-lg text-sm font-bold transition-all ${viewMode === 'near' ? 'bg-white text-primary shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
                        >
                            Sắp hết hạn
                        </button>
                    </div>

                    {/* Actions */}
                    <div className="flex gap-3">
                        <button 
                            onClick={handleExportReport}
                            className="h-11 px-5 bg-white border border-slate-200 rounded-xl font-bold text-slate-600 hover:bg-slate-50 flex items-center gap-2 transition-all active:scale-95 shadow-sm"
                        >
                            <span className="material-symbols-outlined text-lg">file_download</span>
                            Xuất báo cáo
                        </button>
                        {viewMode === 'expired' && (
                            <button 
                                onClick={handleDeleteExpired}
                                className="h-11 px-5 bg-red-600 text-white rounded-xl font-bold hover:bg-red-700 flex items-center gap-2 shadow-md transition-all active:scale-95 animate-pulse"
                            >
                                <span className="material-symbols-outlined text-lg">delete_sweep</span>
                                Tiêu hủy tất cả đã hết hạn
                            </button>
                        )}
                    </div>
                </div>

                {/* Data Table Card */}
                <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
                    <div className="px-6 py-4 border-b border-slate-100 bg-slate-50/50 flex justify-between items-center">
                        <h3 className="font-bold text-slate-900">
                            {viewMode === 'all' && "Danh sách túi máu trong kho"}
                            {viewMode === 'expired' && "Danh sách túi máu ĐÃ HẾT HẠN"}
                            {viewMode === 'near' && "Danh sách túi máu SẮP HẾT HẠN"}
                        </h3>
                        <div className="relative w-64">
                            <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">search</span>
                            <input 
                                type="text" 
                                placeholder="Tìm mã túi máu..." 
                                className="w-full pl-10 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:ring-1 focus:ring-primary focus:border-primary outline-none transition-all"
                            />
                        </div>
                    </div>
                    
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-white border-b border-slate-100">
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Mã túi máu</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Mã chiến dịch</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Nhóm máu</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Thể tích</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Ngày lấy mẫu</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Hạn sử dụng</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest">Trạng thái hạn</th>
                                    <th className="py-4 px-6 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Hành động</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-50">
                                {bloodUnits.length === 0 ? (
                                    <tr>
                                        <td colSpan="8" className="py-20 text-center text-slate-400">
                                            <div className="flex flex-col items-center">
                                                <span className="material-symbols-outlined text-5xl mb-2 opacity-20">inventory_2</span>
                                                <p className="text-sm italic">Chưa có dữ liệu túi máu nào được tìm thấy</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    bloodUnits.map((unit) => (
                                        <tr key={unit.maTuiMau} className="hover:bg-slate-50 transition-colors">
                                            {/* Data will be mapped here later */}
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                    
                    <div className="px-6 py-4 bg-slate-50 border-t border-slate-100 flex justify-between items-center text-xs text-slate-500">
                        <span>Hiển thị 0 đơn vị</span>
                        <div className="flex gap-2">
                            <button className="px-3 py-1 border border-slate-200 rounded hover:bg-white transition-all disabled:opacity-50" disabled>Trước</button>
                            <button className="px-3 py-1 bg-primary text-white rounded font-bold">1</button>
                            <button className="px-3 py-1 border border-slate-200 rounded hover:bg-white transition-all disabled:opacity-50" disabled>Sau</button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default QuanLyHanDung;

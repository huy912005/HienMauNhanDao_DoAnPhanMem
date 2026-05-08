import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import http from '../utils/http';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';

const ThongKeTonKho = () => {
  const [stats, setStats] = useState({
    totalBloodUnits: 0,
    newVolunteers: 0,
    activeCampaigns: 0,
    screeningPassRate: 0
  });

  const [loading, setLoading] = useState(true);

  // Thêm state cho biểu đồ và bảng
  const [barData, setBarData] = useState([]);
  const [bloodUnits, setBloodUnits] = useState({ content: [] });
  const [pieData, setPieData] = useState([]);
  const COLORS = ['#af101a', '#d32f2f', '#ffb3ac', '#8f6f6c']; // Tương ứng O, A, B, AB

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Chạy song song API để lấy dữ liệu
        const [statsData, barDataRes, pieDataRes] = await Promise.all([
          http.get('/tuimau/stats'),
          http.get('/tuimau/charts/bar?year=2026'),
          http.get('/khomau/charts/pie')
        ]);
        
        setStats(statsData);
        
        // Map data cho Bar Chart
        const mappedBarData = barDataRes.length > 0 ? barDataRes : 
          Array.from({length: 12}, (_, i) => ({ month: `T.${i+1}`, quantity: 0 }));
        setBarData(mappedBarData);

        // Map data cho Pie Chart (Gộp thành 4 nhóm O, A, B, AB)
        const aggregatedPie = { 'Nhóm O': 0, 'Nhóm A': 0, 'Nhóm B': 0, 'Nhóm AB': 0 };
        pieDataRes.forEach(item => {
          if (item.bloodType.startsWith('AB')) aggregatedPie['Nhóm AB'] += item.quantity;
          else if (item.bloodType.startsWith('A')) aggregatedPie['Nhóm A'] += item.quantity;
          else if (item.bloodType.startsWith('B')) aggregatedPie['Nhóm B'] += item.quantity;
          else if (item.bloodType.startsWith('O')) aggregatedPie['Nhóm O'] += item.quantity;
        });

        const mappedPieData = Object.keys(aggregatedPie)
          .map(key => ({ name: key, value: aggregatedPie[key] }))
          .filter(item => item.value > 0);
        
        if (mappedPieData.length === 0) mappedPieData.push({name: 'Trống', value: 1});
        setPieData(mappedPieData);

        // Map data cho Bảng Tổng hợp 8 nhóm máu
        const today = new Date().toLocaleDateString('vi-VN');
        const NGUONG_AN_TOAN = 10;
        
        const summaryTable = pieDataRes.map(item => ({
          nhomMau: item.bloodType,
          soLuong: item.quantity,
          nguongAnToan: NGUONG_AN_TOAN,
          trangThai: item.quantity >= NGUONG_AN_TOAN ? 'An toàn' : 'Sắp hết',
          ngayCapNhat: today
        }));
        
        setBloodUnits({ content: summaryTable });

      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu từ Backend:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Hàm xử lý hủy túi máu
  const handleDelete = async (maTuiMau) => {
    if (!window.confirm(`Bạn có chắc chắn muốn hủy túi máu ${maTuiMau} không? Hành động này không thể hoàn tác.`)) {
      return;
    }
    try {
      await http.delete(`/tuimau/blood-units/${maTuiMau}`);
      alert('Đã hủy túi máu thành công!');
      // Tải lại dữ liệu trang hiện tại
      setPage(0);
      window.location.reload();
    } catch (error) {
      console.error('Lỗi khi hủy túi máu:', error);
      alert('Không thể hủy túi máu này. Vui lòng thử lại sau.');
    }
  };

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center font-bold">Đang tải dữ liệu từ máy chủ...</div>;
  }

  return (
    <div className="bg-gray-50 text-slate-800 antialiased min-h-screen">
      <main className="p-8 space-y-8">
        
        {/* Tiêu đề trang */}
        <div className="flex justify-between items-end">
          <div>
            <h1 className="text-2xl font-bold text-slate-800">Thống kê &amp; Tồn kho</h1>
            <p className="text-slate-500 text-sm mt-1">Dữ liệu được cập nhật tự động theo thời gian thực.</p>
          </div>
          <div className="flex gap-3">
            <button className="h-10 px-4 bg-white border border-slate-300 rounded-xl text-sm font-medium text-slate-700 flex items-center gap-2 hover:bg-slate-50">
              <span className="material-symbols-outlined text-sm">file_download</span> Xuất báo cáo
            </button>
            <Link to="/quan-ly-kho/nhap-kho" className="h-10 px-4 bg-[#af101a] text-white rounded-xl text-sm font-medium flex items-center gap-2 hover:bg-red-800">
              <span className="material-symbols-outlined text-sm">add</span> Cập nhật kho
            </Link>
          </div>
        </div>

        {/* 4 Thẻ thống kê (Cards) */}
        <div className="grid grid-cols-4 gap-6">
          
          {/* Thẻ 1: Tổng đơn vị máu */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 relative overflow-hidden flex flex-col justify-between shadow-sm">
            <div className="absolute right-4 top-4 text-[#af101a]/10">
              <span className="material-symbols-outlined text-6xl" style={{fontVariationSettings: "'FILL' 1"}}>bloodtype</span>
            </div>
            <div>
              <p className="text-[10px] font-bold text-slate-500 uppercase tracking-widest mb-1">Tổng đơn vị máu</p>
              <h3 className="text-3xl font-black text-slate-800">{stats.totalBloodUnits.toLocaleString()}</h3>
            </div>
            <p className="text-xs text-green-600 font-medium flex items-center gap-1">
              <span className="material-symbols-outlined text-sm">trending_up</span> Dữ liệu thực tế
            </p>
          </div>

          {/* Thẻ 2: Số TNV mới */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 relative overflow-hidden flex flex-col justify-between shadow-sm">
            <div className="absolute right-4 top-4 text-blue-500/10">
              <span className="material-symbols-outlined text-6xl">person_add</span>
            </div>
            <div>
              <p className="text-[10px] font-bold text-slate-500 uppercase tracking-widest mb-1">Số TNV hệ thống</p>
              <h3 className="text-3xl font-black text-slate-800">{stats.newVolunteers.toLocaleString()}</h3>
            </div>
            <p className="text-xs text-blue-600 font-medium flex items-center gap-1">
              <span className="material-symbols-outlined text-sm">people</span> Dữ liệu thực tế
            </p>
          </div>

          {/* Thẻ 3: Chiến dịch đang chạy */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 relative overflow-hidden flex flex-col justify-between shadow-sm">
            <div className="absolute right-4 top-4 text-orange-500/10">
              <span className="material-symbols-outlined text-6xl">campaign</span>
            </div>
            <div>
              <p className="text-[10px] font-bold text-slate-500 uppercase tracking-widest mb-1">Số chiến dịch</p>
              <h3 className="text-3xl font-black text-slate-800">{stats.activeCampaigns < 10 ? `0${stats.activeCampaigns}` : stats.activeCampaigns}</h3>
            </div>
            <p className="text-xs text-slate-500 font-medium">Đã khai báo trên hệ thống</p>
          </div>

          {/* Thẻ 4: Tỷ lệ đạt sàng lọc */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 relative overflow-hidden flex flex-col justify-between shadow-sm">
            <div className="absolute right-4 top-4 text-emerald-500/10">
              <span className="material-symbols-outlined text-6xl">fact_check</span>
            </div>
            <div>
              <p className="text-[10px] font-bold text-slate-500 uppercase tracking-widest mb-1">Tỷ lệ đạt sàng lọc</p>
              <h3 className="text-3xl font-black text-slate-800">{stats.screeningPassRate}%</h3>
            </div>
            <div className="w-full bg-slate-100 h-1.5 rounded-full mt-2">
              <div className="bg-emerald-500 h-full rounded-full" style={{ width: `${stats.screeningPassRate}%` }}></div>
            </div>
          </div>

        </div>

        {/* PHẦN MỚI: Biểu đồ */}
        <div className="grid grid-cols-12 gap-6 h-[400px]">
          {/* Biểu đồ tròn */}
          <div className="col-span-4 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
            <h4 className="text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
              <span className="material-symbols-outlined text-red-600 text-lg">pie_chart</span> Tỷ lệ tồn kho
            </h4>
            <div className="flex-1">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={pieData}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={100}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {pieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend verticalAlign="bottom" height={36}/>
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Biểu đồ cột */}
          <div className="col-span-8 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
            <h4 className="text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
              <span className="material-symbols-outlined text-red-600 text-lg">bar_chart</span> Lượng máu thu được (2026)
            </h4>
            <div className="flex-1">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={barData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="month" axisLine={false} tickLine={false} tick={{fontSize: 12}} />
                  <YAxis axisLine={false} tickLine={false} />
                  <Tooltip cursor={{fill: '#f1f5f9'}} />
                  <Bar dataKey="quantity" fill="#af101a" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        {/* PHẦN MỚI: Bảng chi tiết */}
        <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
          <div className="px-6 py-5 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
            <h4 className="text-sm font-bold text-slate-700">Trạng thái tồn kho theo 8 nhóm máu</h4>
            <div className="flex items-center gap-3">
              <span className="text-xs text-slate-500 flex items-center gap-1">
                <span className="w-2 h-2 rounded-full bg-red-500"></span> Dưới ngưỡng an toàn
              </span>
              <span className="text-xs text-slate-500 flex items-center gap-1">
                <span className="w-2 h-2 rounded-full bg-emerald-500"></span> Trên ngưỡng an toàn
              </span>
            </div>
          </div>
          <table className="w-full text-left">
            <thead>
              <tr className="bg-slate-50 border-b border-slate-100">
                <th className="h-12 px-6 text-[10px] font-bold text-slate-500 uppercase tracking-widest">Nhóm máu</th>
                <th className="h-12 px-6 text-[10px] font-bold text-slate-500 uppercase tracking-widest text-center">Ngưỡng an toàn</th>
                <th className="h-12 px-6 text-[10px] font-bold text-slate-500 uppercase tracking-widest">Ngày cập nhật</th>
                <th className="h-12 px-6 text-[10px] font-bold text-slate-500 uppercase tracking-widest">Trạng thái</th>
                <th className="h-12 px-6 text-[10px] font-bold text-slate-500 uppercase tracking-widest text-right">Tổng số lượng</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {bloodUnits && bloodUnits.content && bloodUnits.content.length > 0 ? (
                bloodUnits.content.map((unit, idx) => (
                  <tr key={idx} className="h-16 hover:bg-slate-50 transition-colors">
                    <td className="px-6">
                      <span className="w-10 h-6 inline-flex items-center justify-center rounded-full text-[12px] font-black bg-red-100 text-red-800">
                        {unit.nhomMau}
                      </span>
                    </td>
                    <td className="px-6 text-sm font-bold text-slate-600 text-center">{unit.nguongAnToan}</td>
                    <td className="px-6 text-sm text-slate-600">
                      {unit.ngayCapNhat}
                    </td>
                    <td className="px-6">
                      <span className={`px-2.5 py-1 rounded-lg text-[10px] font-bold uppercase flex w-fit items-center gap-1 ${unit.trangThai === 'Sắp hết' ? 'bg-red-50 text-red-600 border border-red-100' : 'bg-emerald-50 text-emerald-700 border border-emerald-100'}`}>
                        <span className="w-1.5 h-1.5 rounded-full" style={{backgroundColor: unit.trangThai === 'Sắp hết' ? '#ef4444' : '#10b981'}}></span>
                        {unit.trangThai}
                      </span>
                    </td>
                    <td className="px-6 text-right font-black text-lg text-slate-800">
                      {unit.soLuong}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center py-8 text-slate-500">Chưa có dữ liệu tồn kho.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

      </main>
    </div>
  );
};

export default ThongKeTonKho;

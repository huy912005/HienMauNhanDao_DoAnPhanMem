import React, { useState, useEffect, use } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { chienDichService } from '../services/chienDichService';
import { DiaDiemService } from '../services/DiaDiemService';
import { tinhNguyenVienService } from '../services/tinhNguyenVienService';
import { useQuery } from '@tanstack/react-query';
import http from '../utils/http';

export default function HomePage() {
    const navigate = useNavigate();
    const [campaigns, setCampaigns] = useState([]);

    useEffect(() => {
        const fetchCampaigns = async () => {
            try {
                const response = await chienDichService.getChienDichs();
                let campaignsData = [];
                if (response?.data && Array.isArray(response.data)) {
                    campaignsData = response.data;
                } else if (Array.isArray(response)) {
                    campaignsData = response;
                }

                const now = new Date();
                let activeCampaigns = campaignsData.filter(campaign => {
                    const endDate = new Date(campaign.thoiGianKT);
                    return now <= endDate;
                });

                activeCampaigns.sort((a, b) => {
                    const aIsActive = now >= new Date(a.thoiGianBD) ? -1 : 1;
                    const bIsActive = now >= new Date(b.thoiGianBD) ? -1 : 1;
                    return aIsActive - bIsActive;
                });

                setCampaigns(activeCampaigns.slice(0, 2));
            } catch (error) {
                console.error("Failed to fetch campaigns:", error);
            }
        };
        fetchCampaigns();
    }, []);
    const { data: countDD, isLoading: loadingDD } = useQuery({
    queryKey: ['countDiaDiem'],
    queryFn: async () => {
        const response = await DiaDiemService.getDiaDiems();
        console.log("Dữ liệu địa điểm:", response);
        const data = response?.data || response;
        if (Array.isArray(data)) {
            return data.length;
        }
        return data?.totalElements || 0;
    },
    staleTime: 10 * 60 * 1000, // 10 phút
});
    const { data: countNH, isLoading, isError } = useQuery({
        queryKey: ['countNguoiHien'],
        queryFn: async () => {
            const response = await tinhNguyenVienService.getAll();
            return response?.totalElements || response?.data?.totalElements || 0;
        }
    });

    const { data: bloodStats } = useQuery({
        queryKey: ['bloodStats'],
        queryFn: async () => {
            const response = await http.get('/tuimau/stats');
            return response?.data || response;
        }
    });

    const getCampaignStatus = (campaign) => {
        const now = new Date();
        const startDate = new Date(campaign.thoiGianBD);
        const endDate = new Date(campaign.thoiGianKT);

        if (now > endDate) {
            return { status: "Đã kết thúc", color: "bg-slate-100 text-slate-700" };
        } else if (now < startDate) {
            return { status: "Sắp diễn ra", color: "bg-amber-100 text-amber-700" };
        } else {
            return { status: "Đang mở", color: "bg-green-100 text-green-700" };
        }
    };
    return (
        <main className="flex-1 w-full">
            {/* Emergency Alert Banner */}
            <div className="w-full h-11 bg-error text-white flex items-center justify-center gap-4 animate-pulse shrink-0">
                <span className="material-symbols-outlined text-xl" style={{ fontVariationSettings: "'FILL' 1" }}>warning</span>
                <span className="font-bold tracking-wide uppercase text-[12px]">CẦN GẤP NHÓM MÁU O VÀ AB TẠI BV ĐÀ NẴNG</span>
                <button className="h-6 px-4 bg-white text-error rounded-full text-[10px] font-black hover:bg-opacity-90 transition-all uppercase flex items-center">Hỗ trợ ngay</button>
            </div>

            {/* Hero Section */}
            <section className="relative h-[480px] w-full overflow-hidden shrink-0">
                <img alt="Hero Banner" className="w-full h-full object-cover"
                    src="https://lh3.googleusercontent.com/aida-public/AB6AXuDLDJIMpiIIYwR-RAVGCwTuYQNHdkZi9FmR54vsaeJFVu7FB_MlvM--0b1AQIRjWz6Adw429PeQBhnpMmWNLyxB4P0z_qX_3LMpl1r2cvnbp3mBWqEkoWjvX88boiqw0R9kjF_TXl4Mu5ZgX9jRdepx5sM4CtVcgqPsLBiBg_Tr_18VqlPTZF1XghZQoagkrLqZGX92uay4YlO-tYZ22c4kdU17N-SovNx5esR-C_YZGkGmwQlfhMXegjekrcGxxmA3iGxL84Ez_F4E" />
                <div className="absolute inset-0 bg-gradient-to-r from-primary/90 via-primary/60 to-transparent flex items-center">
                    <div className="w-[1200px] mx-auto px-4">
                        <div className="w-[640px]">
                            <span className="inline-block h-7 px-4 bg-white/20 backdrop-blur-md rounded-full text-[11px] font-bold uppercase tracking-widest mb-6 flex items-center w-fit text-white">Cổng thông tin Tình nguyện viên</span>
                            <h2 className="text-[56px] font-black mb-4 leading-[1.1] text-white">"Một giọt máu cho đi,<br />một cuộc đời ở lại"</h2>
                            <p className="text-lg text-white/90 mb-8 font-light leading-relaxed">Đồng hành cùng cộng đồng Đà Nẵng trong hành trình nhân ái. Đăng ký hiến máu nhanh chóng, theo dõi hồ sơ an toàn và bảo mật.</p>
                            <div className="flex gap-4">
                                <button
                                    onClick={() => navigate('/chiendich')}
                                    className="h-14 px-8 bg-white text-primary rounded-xl font-black text-sm hover:scale-105 transition-transform flex items-center gap-2 shadow-xl shadow-black/10">
                                    <span className="material-symbols-outlined text-xl" style={{ fontVariationSettings: "'FILL' 1" }}>search</span>
                                    Tìm chiến dịch gần bạn
                                </button>
                                <button onClick={() => document.getElementById('quy-trinh').scrollIntoView({ behavior: 'smooth' })}
                                    className="h-14 px-8 bg-transparent border-2 border-white text-white rounded-xl font-black text-sm hover:bg-white/10 transition-all uppercase tracking-tight">Quy trình hiến máu</button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Statistics Section */}
            <section className="w-[1200px] mx-auto -mt-16 relative z-10 mb-16 px-4">
                <div className="grid grid-cols-3 gap-6">
                    <div className="bg-white rounded-2xl shadow-lg border border-slate-100 p-8 flex flex-col items-center text-center group hover:-translate-y-2 transition-all duration-300">
                        <div className="w-16 h-16 rounded-full bg-red-50 flex items-center justify-center text-primary mb-4 group-hover:bg-primary group-hover:text-white transition-colors">
                            <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>group</span>
                        </div>
                        <h3 className="text-4xl font-black text-slate-900 mb-2">{countNH}</h3>
                        <p className="text-sm text-slate-500 font-bold uppercase tracking-wider">Người hiến máu</p>
                    </div>
                    <div className="bg-white rounded-2xl shadow-lg border border-slate-100 p-8 flex flex-col items-center text-center group hover:-translate-y-2 transition-all duration-300">
                        <div className="w-16 h-16 rounded-full bg-red-50 flex items-center justify-center text-primary mb-4 group-hover:bg-primary group-hover:text-white transition-colors">
                            <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>bloodtype</span>
                        </div>
                        <h3 className="text-4xl font-black text-slate-900 mb-2">
                            {bloodStats?.tongTheTich ? bloodStats.tongTheTich.toLocaleString() : '0'}+
                        </h3>
                        <p className="text-sm text-slate-500 font-bold uppercase tracking-wider">Đơn vị máu tiếp nhận</p>
                    </div>
                    <div className="bg-white rounded-2xl shadow-lg border border-slate-100 p-8 flex flex-col items-center text-center group hover:-translate-y-2 transition-all duration-300">
                        <div className="w-16 h-16 rounded-full bg-red-50 flex items-center justify-center text-primary mb-4 group-hover:bg-primary group-hover:text-white transition-colors">
                            <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>local_hospital</span>
                        </div>
                        <h3 className="text-4xl font-black text-slate-900 mb-2">{countDD}</h3>
                        <p className="text-sm text-slate-500 font-bold uppercase tracking-wider">Địa điểm liên kết</p>
                    </div>
                </div>
            </section>

            {/* Chiến dịch nổi bật */}
            <section id="chien-dich-noi-bat" className="w-[1200px] mx-auto px-4 mb-20">
                <div className="flex items-center justify-between mb-8">
                    <div className="flex items-center gap-3">
                        <div className="w-1.5 h-8 bg-primary rounded-full"></div>
                        <h2 className="text-3xl font-black text-slate-900 tracking-tight">Chiến Dịch Đang Diễn Ra</h2>
                    </div>
                    <Link className="text-primary font-bold text-sm uppercase tracking-widest flex items-center gap-2 hover:underline" to="/chiendich">
                        Xem tất cả chiến dịch <span className="material-symbols-outlined text-sm">arrow_forward</span>
                    </Link>
                </div>
                <div className="grid grid-cols-2 gap-8">
                    {campaigns.map((campaign) => {
                        const statusInfo = getCampaignStatus(campaign);
                        return (
                            <div key={campaign.maChienDich} className="flex bg-white rounded-2xl shadow-sm border border-slate-200 hover:shadow-lg transition-shadow p-5 items-center gap-6">
                                <div className="w-[240px] h-[160px] shrink-0 rounded-xl overflow-hidden shadow-sm border border-slate-100 bg-slate-50">
                                    <img src={campaign.imageUrl ? `/images/${campaign.imageUrl}` : "https://images.unsplash.com/photo-1615461066841-6116e61058f4?auto=format&fit=crop&q=80&w=600"}
                                        className="w-full h-full object-cover" alt="Campaign" />
                                </div>
                                <div className="flex-1 flex flex-col justify-center">
                                    <span className={`px-3 py-1 text-[10px] font-black uppercase tracking-widest rounded-full w-fit mb-3 ${statusInfo.color}`}>{statusInfo.status}</span>
                                    <h3 className="text-xl font-bold text-slate-900 mb-2 line-clamp-2">{campaign.tenChienDich}</h3>
                                    <div className="space-y-2 mb-6">
                                        <p className="text-sm text-slate-500 flex items-center gap-2"><span className="material-symbols-outlined text-[16px]">calendar_month</span> {new Date(campaign.thoiGianBD).toLocaleDateString('vi-VN')}</p>
                                        <p className="text-sm text-slate-500 flex items-center gap-2"><span className="material-symbols-outlined text-[16px]">location_on</span> {campaign.diaDiem?.tenDiaDiem}</p>
                                    </div>
                                    <button onClick={() => navigate('/chiendich')} className="h-10 px-6 bg-primary text-white rounded-lg font-bold text-sm hover:bg-red-800 transition-colors w-full">Xem chi tiết</button>
                                </div>
                            </div>
                        )
                    })}
                </div>
            </section>

            {/* Quy Trình Hiến Máu */}
            <section id="quy-trinh" className="bg-slate-50 py-20 mb-20 border-y border-slate-200">
                <div className="w-[1200px] mx-auto px-4 text-center">
                    <span className="text-primary text-[11px] font-black uppercase tracking-[0.3em] mb-2 block">Quy trình đơn giản</span>
                    <h2 className="text-3xl font-black text-slate-900 tracking-tight mb-16">4 Bước Đơn Giản Để Cứu Người</h2>

                    <div className="grid grid-cols-4 gap-8 relative">
                        <div className="absolute top-10 left-16 right-16 h-0.5 bg-slate-200 hidden md:block"></div>
                        <div className="relative flex flex-col items-center">
                            <div className="w-20 h-20 rounded-full bg-white border-4 border-slate-100 flex items-center justify-center text-primary z-10 shadow-sm mb-6">
                                <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>app_registration</span>
                            </div>
                            <h3 className="text-lg font-bold text-slate-900 mb-2">1. Đăng ký</h3>
                            <p className="text-sm text-slate-500 px-4">Chọn chiến dịch và điền tờ khai y tế online.</p>
                        </div>
                        <div className="relative flex flex-col items-center">
                            <div className="w-20 h-20 rounded-full bg-white border-4 border-slate-100 flex items-center justify-center text-primary z-10 shadow-sm mb-6">
                                <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>stethoscope</span>
                            </div>
                            <h3 className="text-lg font-bold text-slate-900 mb-2">2. Khám sàng lọc</h3>
                            <p className="text-sm text-slate-500 px-4">Bác sĩ kiểm tra sức khỏe và xét nghiệm máu nhanh.</p>
                        </div>
                        <div className="relative flex flex-col items-center">
                            <div className="w-20 h-20 rounded-full bg-white border-4 border-slate-100 flex items-center justify-center text-primary z-10 shadow-sm mb-6">
                                <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>volunteer_activism</span>
                            </div>
                            <h3 className="text-lg font-bold text-slate-900 mb-2">3. Hiến máu</h3>
                            <p className="text-sm text-slate-500 px-4">Quá trình diễn ra an toàn trong 5-10 phút.</p>
                        </div>
                        <div className="relative flex flex-col items-center">
                            <div className="w-20 h-20 rounded-full bg-white border-4 border-slate-100 flex items-center justify-center text-primary z-10 shadow-sm mb-6">
                                <span className="material-symbols-outlined text-3xl" style={{ fontVariationSettings: "'FILL' 1" }}>workspace_premium</span>
                            </div>
                            <h3 className="text-lg font-bold text-slate-900 mb-2">4. Nhận chứng nhận</h3>
                            <p className="text-sm text-slate-500 px-4">Nghỉ ngơi, nhận quà và giấy chứng nhận điện tử.</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Lợi ích hiến máu */}
            <section className="w-[1200px] mx-auto px-4 mb-20">
                <div className="bg-white rounded-[40px] shadow-sm border border-slate-200 overflow-hidden flex">
                    <div className="w-1/2 p-16 flex flex-col justify-center">
                        <span className="text-primary text-[11px] font-black uppercase tracking-[0.3em] mb-4 block">Quyền lợi dành cho bạn</span>
                        <h2 className="text-4xl font-black text-slate-900 tracking-tight mb-6 leading-tight">Hiến máu không chỉ cứu người mà còn cứu chính bạn</h2>
                        <p className="text-slate-500 mb-8 leading-relaxed">Người tham gia hiến máu tình nguyện sẽ được hưởng các quyền lợi chăm sóc sức khỏe đặc biệt theo quy định của Bộ Y tế và được tôn vinh vì nghĩa cử cao đẹp.</p>

                        <div className="space-y-6">
                            <div className="flex gap-4">
                                <div className="w-12 h-12 rounded-2xl bg-red-50 flex items-center justify-center text-primary shrink-0">
                                    <span className="material-symbols-outlined" style={{ fontVariationSettings: "'FILL' 1" }}>favorite</span>
                                </div>
                                <div>
                                    <h4 className="text-lg font-bold text-slate-900 mb-1">Kiểm tra sức khỏe miễn phí</h4>
                                    <p className="text-sm text-slate-500 leading-relaxed">Được khám lâm sàng, đo huyết áp, nhịp tim và xét nghiệm sàng lọc các bệnh lây truyền qua đường máu (Viêm gan B, C, HIV, Giang mai...).</p>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div className="w-12 h-12 rounded-2xl bg-red-50 flex items-center justify-center text-primary shrink-0">
                                    <span className="material-symbols-outlined" style={{ fontVariationSettings: "'FILL' 1" }}>health_and_safety</span>
                                </div>
                                <div>
                                    <h4 className="text-lg font-bold text-slate-900 mb-1">Tái tạo máu mới</h4>
                                    <p className="text-sm text-slate-500 leading-relaxed">Việc hiến máu kích thích tủy xương sản sinh hồng cầu mới, giúp cơ thể khỏe mạnh hơn và giảm nguy cơ quá tải sắt gây bệnh tim mạch.</p>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div className="w-12 h-12 rounded-2xl bg-red-50 flex items-center justify-center text-primary shrink-0">
                                    <span className="material-symbols-outlined" style={{ fontVariationSettings: "'FILL' 1" }}>redeem</span>
                                </div>
                                <div>
                                    <h4 className="text-lg font-bold text-slate-900 mb-1">Quà tặng & Phục hồi sức khỏe</h4>
                                    <p className="text-sm text-slate-500 leading-relaxed">Nhận ngay một phần quà hiện vật (hoặc gói xét nghiệm), chi phí đi lại và suất ăn nhẹ phục hồi sức khỏe tại chỗ.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="w-1/2 relative">
                        <img src="https://images.unsplash.com/photo-1576091160550-2173dba999ef?auto=format&fit=crop&q=80&w=800"
                            alt="Doctor" className="w-full h-full object-cover" />
                        <div className="absolute inset-0 bg-gradient-to-r from-white to-transparent w-1/4"></div>
                    </div>
                </div>
            </section>

            {/* Testimonials (Người hiến máu tiêu biểu) */}
            <section className="bg-slate-900 py-24 mb-20 relative overflow-hidden">
                <div className="absolute top-0 right-0 w-96 h-96 bg-primary rounded-full blur-[150px] opacity-20"></div>
                <div className="w-[1200px] mx-auto px-4 relative z-10">
                    <div className="text-center mb-16">
                        <span className="text-primary text-[11px] font-black uppercase tracking-[0.3em] mb-2 block">Gương sáng cộng đồng</span>
                        <h2 className="text-4xl font-black text-white tracking-tight">Cảm Nhận Từ Những Trái Tim Vàng</h2>
                    </div>

                    <div className="grid grid-cols-3 gap-8">
                        <div className="bg-white/10 backdrop-blur-lg border border-white/10 p-8 rounded-3xl relative">
                            <span className="material-symbols-outlined text-5xl text-white/20 absolute top-6 right-6"
                                style={{ fontVariationSettings: "'FILL' 1" }}>format_quote</span>
                            <div className="flex items-center gap-4 mb-6 relative z-10">
                                <img src="https://i.pravatar.cc/150?u=12"
                                    className="w-14 h-14 rounded-full border-2 border-primary" alt="User" />
                                <div>
                                    <h4 className="text-white font-bold">Trần Hữu Nam</h4>
                                    <p className="text-xs text-primary font-bold uppercase tracking-wider">Hiến máu 25 lần</p>
                                </div>
                            </div>
                            <p className="text-slate-300 text-sm leading-relaxed relative z-10">"Mỗi lần nhìn thấy đơn vị máu của mình chuyển trạng thái 'Đã được sử dụng để cứu người' trên ứng dụng, tôi lại thấy cuộc đời mình thêm phần ý nghĩa. Hệ thống mới này quá tuyệt vời!"</p>
                        </div>
                        <div className="bg-white/10 backdrop-blur-lg border border-white/10 p-8 rounded-3xl relative translate-y-4">
                            <span className="material-symbols-outlined text-5xl text-white/20 absolute top-6 right-6"
                                style={{ fontVariationSettings: "'FILL' 1" }}>format_quote</span>
                            <div className="flex items-center gap-4 mb-6 relative z-10">
                                <img src="https://i.pravatar.cc/150?u=45"
                                    className="w-14 h-14 rounded-full border-2 border-primary" alt="User" />
                                <div>
                                    <h4 className="text-white font-bold">Nguyễn Phương Thảo</h4>
                                    <p className="text-xs text-primary font-bold uppercase tracking-wider">Sinh viên ĐH Bách Khoa</p>
                                </div>
                            </div>
                            <p className="text-slate-300 text-sm leading-relaxed relative z-10">"Lần đầu hiến máu mình rất sợ đau, nhưng các bác sĩ và tình nguyện viên tại điểm hiến rất nhẹ nhàng. Giờ thì mình đã tự tin rủ cả nhóm bạn cùng tham gia mỗi chiến dịch."</p>
                        </div>
                        <div className="bg-white/10 backdrop-blur-lg border border-white/10 p-8 rounded-3xl relative">
                            <span className="material-symbols-outlined text-5xl text-white/20 absolute top-6 right-6"
                                style={{ fontVariationSettings: "'FILL' 1" }}>format_quote</span>
                            <div className="flex items-center gap-4 mb-6 relative z-10">
                                <img src="https://i.pravatar.cc/150?u=33"
                                    className="w-14 h-14 rounded-full border-2 border-primary" alt="User" />
                                <div>
                                    <h4 className="text-white font-bold">Lê Văn Bình</h4>
                                    <p className="text-xs text-primary font-bold uppercase tracking-wider">Nhóm máu O (Rh-)</p>
                                </div>
                            </div>
                            <p className="text-slate-300 text-sm leading-relaxed relative z-10">"Với nhóm máu hiếm, tôi luôn sẵn sàng khi nhận được thông báo khẩn qua SMS từ hệ thống. Cảm giác có thể cứu một mạng người đang nguy kịch thực sự không thể diễn tả bằng lời."</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Medical News Section */}
            <section className="w-[1200px] mx-auto px-4 mb-20">
                <div className="flex items-center justify-between mb-8 h-8">
                    <div className="flex items-center gap-3">
                        <div className="w-1.5 h-8 bg-primary rounded-full"></div>
                        <h2 className="text-2xl font-black text-slate-900 tracking-tight">Tin tức Y tế & Cộng đồng</h2>
                    </div>
                    <Link className="text-primary font-bold text-sm uppercase tracking-widest flex items-center gap-2 hover:underline" to="/news">
                        Xem tất cả <span className="material-symbols-outlined text-sm">arrow_forward</span>
                    </Link>
                </div>
                <div className="grid grid-cols-3 gap-8">
                    <div className="h-[400px] flex flex-col group bg-white border border-slate-200 rounded-2xl overflow-hidden hover:shadow-xl hover:shadow-slate-200/50 transition-all duration-300">
                        <div className="h-48 w-full overflow-hidden relative shrink-0">
                            <img alt="Medical staff"
                                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                                src="https://lh3.googleusercontent.com/aida-public/AB6AXuDPZLY4bcQE7mkm5VnPjZDugPnqaDBDBmcIOK8X8SO8ODaFE9o4InVet3vHXqwWYXa9wRFwtLts3XIupjnXwnPw-3CxBVNK3cv6W_AUESi73RhPPLsi1r_G7zJwrI1bngE8MP_C4rjhWMUBG_CruQiwJ2tH3DDu46oZB_7LCFsvYoN5XXqxz66aJ--rnThrmeezAuE-tqsDOds25e75j2P7h5Du6Du6MAhO6e6CPrCYjawxSmQMCdeX0xKplHro9WCYdaFsPIoNif1E" />
                        </div>
                        <div className="p-6 flex-1 flex flex-col">
                            <p className="text-slate-400 text-[10px] font-black uppercase mb-3">12 Tháng 10, 2024</p>
                            <h3 className="text-lg font-bold text-slate-900 mb-3 group-hover:text-primary transition-colors line-clamp-2 leading-snug">
                                Lợi ích sức khỏe không ngờ từ việc hiến máu định kỳ</h3>
                            <p className="text-slate-500 text-[13px] line-clamp-3 font-light leading-relaxed">Hiến máu không chỉ cứu người mà còn giúp người hiến giảm nguy cơ mắc các bệnh về tim mạch và cân bằng lượng sắt trong cơ thể một cách tự nhiên...</p>
                        </div>
                    </div>
                    <div className="h-[400px] flex flex-col group bg-white border border-slate-200 rounded-2xl overflow-hidden hover:shadow-xl hover:shadow-slate-200/50 transition-all duration-300">
                        <div className="h-48 w-full overflow-hidden relative shrink-0">
                            <img alt="Laboratory"
                                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                                src="https://lh3.googleusercontent.com/aida-public/AB6AXuDSfWJNQeo_FmeCVLdrP8j28NXO0zRTS3WV5ywfjPzjuW1pfpAWcxkTBoiANWWw6IkfUBBrUXeIqRi_X0UgMbR9JaqjpjS6rxSG0dnML5k22XFEdSHsdd7IWFyVAxbPMq0SClJa18PYgmY-RYWTxeoMNQqi6CLAMNz2Ku9j8Po005puKFmk2VXoGQRiY8dgP4GrLYehY_1ar_10lco9_v-uAj3Cni9lm6rGr9PtD0wPYyw7oLlYkAocW21TTAJbpbUlKsphlVPX9Vp5" />
                        </div>
                        <div className="p-6 flex-1 flex flex-col">
                            <p className="text-slate-400 text-[10px] font-black uppercase mb-3">10 Tháng 10, 2024</p>
                            <h3 className="text-lg font-bold text-slate-900 mb-3 group-hover:text-primary transition-colors line-clamp-2 leading-snug">
                                Đà Nẵng triển khai hệ thống quản lý máu bằng mã QR bảo mật cao</h3>
                            <p className="text-slate-500 text-[13px] line-clamp-3 font-light leading-relaxed">Công nghệ mới giúp truy xuất nguồn gốc đơn vị máu một cách chính xác tuyệt đối, rút ngắn thời gian chuẩn bị máu trong các tình huống cấp cứu khẩn cấp...</p>
                        </div>
                    </div>
                    <div className="h-[400px] flex flex-col group bg-white border border-slate-200 rounded-2xl overflow-hidden hover:shadow-xl hover:shadow-slate-200/50 transition-all duration-300">
                        <div className="h-48 w-full overflow-hidden relative shrink-0">
                            <img alt="Volunteer"
                                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                                src="https://lh3.googleusercontent.com/aida-public/AB6AXuAEckmZSeA5zr01RGEf2XSz1-PS3wePPnFxuUA3phpT1_KQ2dbOcWJsrsBzddHJauozPLCI8O5S82SJosIdN_q8f4F5zhdeKJmp7nsrgIKzQxwG_Fn-3l0Z34dkrIBHhNLb1xCN7vsCCfNrXHykui7NGk5zcj52N_6QIyM5LoGHjS6P7Jn9lEyPkf-GHZ-8BdcqxrGowSXLalzrCieQcD-LdaCt8avxN-66Q09ZnjZwZonFE3bVAxWG0z6roeDwV0wSV9ijRfGQixy4" />
                        </div>
                        <div className="p-6 flex-1 flex flex-col">
                            <p className="text-slate-400 text-[10px] font-black uppercase mb-3">08 Tháng 10, 2024</p>
                            <h3 className="text-lg font-bold text-slate-900 mb-3 group-hover:text-primary transition-colors line-clamp-2 leading-snug">
                                Câu chuyện cảm động về người 50 lần hiến máu tình nguyện</h3>
                            <p className="text-slate-500 text-[13px] line-clamp-3 font-light leading-relaxed">Ông Trần Văn B. đã duy trì thói quen hiến máu suốt 20 năm qua, trở thành tấm gương sáng lan tỏa lòng nhân ái trong phong trào tình nguyện của thành phố...</p>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    );
}

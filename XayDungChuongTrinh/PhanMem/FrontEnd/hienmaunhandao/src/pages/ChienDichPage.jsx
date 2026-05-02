import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { chienDichService } from "../services/chienDichService";

export default function ChienDichPage() {
    const navigate = useNavigate();
    const [campaigns, setCampaigns] = useState([]); 
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);  
    
    useEffect(() => {
        const fetchCampaigns = async () => {
            try {
                setLoading(true);
                const response = await chienDichService.getChienDichs();
                // response từ interceptor là ApiResponse {status, message, data}
                if(response?.data && Array.isArray(response.data)) {
                    setCampaigns(response.data);
                }
                setError(null);
            } catch (error) {
                setError("Lỗi tải danh sách chiến dịch");
                console.error("Error fetching campaigns:", error);
            } finally {
                setLoading(false);
            }
        }
        fetchCampaigns();   
    }, []);
    
    if(loading) {
        return (
            <div className="w-full flex justify-center items-center py-20">
                <div className="text-slate-500">Đang tải chiến dịch...</div>
            </div>
        );
    }
    
    if(error) {
        return (
            <div className="w-full flex justify-center items-center py-20">
                <div className="text-red-500">{error}</div>
            </div>
        );
    }
    
    return(
        <div className="w-full px-8 py-8">
            <div className="mb-8">
                <h2 className="text-2xl font-bold text-slate-900 tracking-tight">Chiến dịch hiến máu đang diễn ra</h2>
                <p className="text-slate-500">Tìm kiếm và tham gia các sự kiện hiến máu nhân đạo tại TP. Đà Nẵng</p>
            </div>

            {/* Campaign List Grid */}
            <div id="campaignGrid" className="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {campaigns.map((campaign) => (
                    <div key={campaign.id} className="w-full h-[520px] bg-white rounded-md border border-slate-200 overflow-hidden hover:shadow-xl transition-all flex flex-col group">
                    
                        {/* Ảnh Campaign */}
                        <div className="w-full h-[200px] relative bg-slate-100 overflow-hidden">
                            <img 
                                alt={campaign.tenChienDich}
                                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                                src={campaign.imageUrl ? `/images/${campaign.imageUrl}` : "https://via.placeholder.com/300x200"}
                            />
                            <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent"></div>
                            <div className="absolute top-3 left-3 flex gap-2">
                                {/* Badge status */}
                            </div>
                        </div>
                        
                        {/* Nội dung card */}
                        <div className="p-6 flex-1 flex flex-col">
                            <h3 className="text-lg font-bold text-slate-900 mb-4 h-7 truncate">
                                {campaign.tenChienDich}
                            </h3>
                            
                            {/* Địa điểm */}
                            <div className="space-y-3 mb-6">
                                <div className="flex items-start gap-3 text-sm text-slate-600">
                                    <span className="material-symbols-outlined text-red-700 text-lg">location_on</span>
                                    <span className="line-clamp-2 leading-relaxed">{campaign.diaDiem}</span>
                                </div>
                                
                                {/* Ngày */}
                                <div className="flex items-center gap-3 text-sm text-slate-600">
                                    <span className="material-symbols-outlined text-red-700 text-lg">calendar_month</span>
                                    <span>{campaign.ngayBatDau} - {campaign.ngayKetThuc}</span>
                                </div>
                                
                                {/* Chỉ tiêu */}
                                <div className="flex items-center gap-3 text-sm text-slate-600">
                                    <span className="material-symbols-outlined text-red-700 text-lg">target</span>
                                    <span className="font-semibold text-slate-900">Chỉ tiêu: {campaign.chiTieu} đơn vị</span>
                                </div>
                            </div>
                            
                            {/* Progress bar */}
                            <div className="mt-auto">
                                <div className="flex justify-between text-[10px] mb-2 font-medium text-slate-500 uppercase tracking-wide">
                                    <span>Tiến độ thu nhận</span>
                                    <span className="font-bold text-slate-700">
                                        {campaign.luongMauDaThu} / {campaign.chiTieu} 
                                        ({Math.round((campaign.luongMauDaThu / campaign.chiTieu) * 100)}%)
                                    </span>
                                </div>
                                <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden mb-6">
                                    <div 
                                        className="h-full bg-red-700" 
                                        style={{ width: `${(campaign.luongMauDaThu / campaign.chiTieu) * 100}%` }}
                                    ></div>
                                </div>
                                
                                {/* Nút Đăng ký */}
                                <button 
                                    onClick={() => navigate(`/register?campaign=${campaign.id}`)}
                                    className="w-full h-11 bg-red-700 text-white rounded-md font-bold uppercase tracking-wide text-sm hover:bg-red-800 transition-colors shadow-md active:scale-[0.98]"
                                >
                                    Đăng ký tham gia
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

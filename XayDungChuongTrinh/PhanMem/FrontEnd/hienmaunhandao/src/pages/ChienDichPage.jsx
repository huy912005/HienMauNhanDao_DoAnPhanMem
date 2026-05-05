import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { chienDichService } from "../services/chienDichService";

export default function ChienDichPage() {
    const navigate = useNavigate();
    const [allCampaigns, setAllCampaigns] = useState([]); 
    const [filteredCampaigns, setFilteredCampaigns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchInput, setSearchInput] = useState("");
    const [districtFilter, setDistrictFilter] = useState("all");
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 6;
    const STATUS_PRIORITY = {
        "Đang diễn ra": 1,
        "Đã phê duyệt": 3, 
        "Sắp diễn ra": 2,
        "Đã kết thúc": 4
    };
    const uniqueLocations = [...new Set(allCampaigns.map(item => item.diaDiem.tenDiaDiem))];
    
    useEffect(() => {
        const fetchCampaigns = async () => {
            try {
                setLoading(true);
                const response = await chienDichService.getChienDichs();
                if(response?.data && Array.isArray(response.data)) {
                    setAllCampaigns(response.data);
                    setFilteredCampaigns(response.data);
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
    
    // Filter campaigns when search or filters change
    useEffect(() => {
        let filtered = [...allCampaigns];
        
        // Search filter
        if (searchInput.trim()) {
            const filter = searchInput.toLowerCase();
            filtered = filtered.filter(campaign => {
                const title = campaign.tenChienDich?.toLowerCase() || "";
                const location = `${campaign.diaDiem?.tenDiaDiem || ""} ${campaign.diaDiem?.diaChiChiTiet || ""}`.toLowerCase();
                return title.includes(filter) || location.includes(filter);
            });
        }
        
        // District filter
        if (districtFilter !== "all") {
            filtered = filtered.filter(campaign => {
                const district = campaign.diaDiem?.tenDiaDiem?.toLowerCase() || "";
                return district.includes(districtFilter.toLowerCase());
            });
        }
        
        // Date range filter
        if (fromDate) {
            filtered = filtered.filter(campaign => {
                const campaignDate = new Date(campaign.thoiGianBD);
                const from = new Date(fromDate);
                return campaignDate >= from;
            });
        }
        if (toDate) {
            filtered = filtered.filter(campaign => {
                const campaignDate = new Date(campaign.thoiGianKT);
                const to = new Date(toDate);
                return campaignDate <= to;
            });
        }

        filtered.sort((a, b) => {
            const weightA = STATUS_PRIORITY[a.trangThai] || 99;
            const weightB = STATUS_PRIORITY[b.trangThai] || 99;
            return weightA - weightB;
        });
        
        setFilteredCampaigns(filtered);
        setCurrentPage(1);
    }, [searchInput, districtFilter, fromDate, toDate, allCampaigns]);
    
    const handleFilter = () => {
        // Trigger filtering animation
        const grid = document.getElementById('campaignGrid');
        if (grid) {
            grid.classList.add('opacity-30', 'pointer-events-none');
            setTimeout(() => {
                grid.classList.remove('opacity-30', 'pointer-events-none');
            }, 300);
        }
    };
    
    // Get campaign status badge
    const getCampaignStatus = (campaign) => {
        const now = new Date();
        const startDate = new Date(campaign.thoiGianBD);
        const endDate = new Date(campaign.thoiGianKT);
        
        if (now > endDate) {
            return { status: "Đã kết thúc", color: "gray" };
        } else if (now < startDate) {
            return { status: "Sắp diễn ra", color: "amber" };
        } else {
            return { status: "Đang mở", color: "green" };
        }
    };
    
    const getProgressPercentage = (campaign) => {
        if (campaign.soLuongDuKien === 0) return 0;
        return Math.round((campaign.luongMauDaThu / campaign.soLuongDuKien) * 100);
    };
    
    // Pagination
    const totalPages = Math.ceil(filteredCampaigns.length / itemsPerPage);
    const startIdx = (currentPage - 1) * itemsPerPage;
    const paginatedCampaigns = filteredCampaigns.slice(startIdx, startIdx + itemsPerPage);
    
    const getPaginationPages = () => {
        const pages = [];
        if (totalPages <= 5) {
            for (let i = 1; i <= totalPages; i++) pages.push(i);
        } else {
            pages.push(1);
            if (currentPage > 3) pages.push('...');
            for (let i = Math.max(1, currentPage - 1); i <= Math.min(totalPages, currentPage + 1); i++) {
                if (!pages.includes(i)) pages.push(i);
            }
            if (currentPage < totalPages - 2) pages.push('...');
            pages.push(totalPages);
        }
        return pages;
    };
    
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
        <main className="flex-1 w-[1200px] mx-auto p-8 mt-8 bg-white border border-slate-200 rounded-2xl shadow-sm mb-16">
            <div className="mb-8">
                <h2 className="text-2xl font-bold text-slate-900 tracking-tight">Chiến dịch hiến máu đang diễn ra</h2>
                <p className="text-slate-500">Tìm kiếm và tham gia các sự kiện hiến máu nhân đạo tại TP. Đà Nẵng</p>
            </div>

            {/* Filter Bar */}
            <div className="w-full bg-white p-6 rounded-md border border-slate-200 mb-8 flex flex-wrap items-end gap-6 shadow-sm">
                <div className="w-[320px]">
                    <label className="block text-[10px] font-semibold text-slate-500 uppercase tracking-wider mb-2">Tên chiến dịch</label>
                    <div className="relative">
                        <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-lg">search</span>
                        <input 
                            id="searchInput"
                            value={searchInput}
                            onChange={(e) => setSearchInput(e.target.value)}
                            className="w-full pl-10 pr-4 py-2.5 border border-slate-200 rounded-md text-sm outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all"
                            placeholder="Nhập từ khóa tìm kiếm..." 
                            type="text" 
                        />
                    </div>
                </div>
                <div className="w-[200px]">
                <label className="block text-[10px] font-semibold text-slate-500 uppercase tracking-wider mb-2">Địa điểm</label>
                <div className="relative">
                    <select 
                        value={districtFilter}
                        onChange={(e) => setDistrictFilter(e.target.value)}
                        className="w-full pl-4 pr-10 py-2.5 border border-slate-200 rounded-md text-sm appearance-none cursor-pointer outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all bg-white"
                    >
                        <option value="all">Tất cả</option>
                        
                        {/* Render danh sách địa điểm động */}
                        {uniqueLocations.map((location) => (
                            <option key={location} value={location}>
                                {location}
                            </option>
                        ))}
                    </select>
                    <span className="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none">expand_more</span>
                </div>
            </div>
                <div className="w-[300px] flex gap-2">
                    <div className="w-1/2">
                        <label className="block text-[10px] font-semibold text-slate-500 uppercase tracking-wider mb-2">Từ ngày</label>
                        <input 
                            value={fromDate}
                            onChange={(e) => setFromDate(e.target.value)}
                            className="w-full px-3 py-2.5 border border-slate-200 rounded-md text-sm outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all bg-white"
                            type="date" 
                        />
                    </div>
                    <div className="w-1/2">
                        <label className="block text-[10px] font-semibold text-slate-500 uppercase tracking-wider mb-2">Đến ngày</label>
                        <input 
                            value={toDate}
                            onChange={(e) => setToDate(e.target.value)}
                            className="w-full px-3 py-2.5 border border-slate-200 rounded-md text-sm outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all bg-white"
                            type="date" 
                        />
                    </div>
                </div>
                <button
                    onClick={handleFilter}
                    className="w-[160px] h-[42px] bg-red-700 text-white rounded-md font-bold text-sm hover:bg-red-800 flex items-center justify-center gap-2 shadow-lg shadow-red-700/20 active:scale-[0.98] transition-all"
                >
                    <span className="material-symbols-outlined text-lg">filter_list</span>
                    Lọc kết quả
                </button>
            </div>

            {/* Campaign Grid */}
            <div id="campaignGrid" className="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 transition-opacity duration-300">
                {paginatedCampaigns.map((campaign) => {
                    const status = getCampaignStatus(campaign);
                    const percentage = getProgressPercentage(campaign);
                    const isUpcoming = status.status === "Sắp diễn ra";
                    
                    return (
                        <div key={campaign.id} className="w-full h-[520px] bg-white rounded-md border border-slate-200 overflow-hidden hover:shadow-xl transition-all flex flex-col group">
                            {/* Image */}
                            <div className="w-full h-[200px] relative bg-slate-100 overflow-hidden">
                                <img 
                                    alt={campaign.tenChienDich}
                                    className={`w-full h-full object-cover group-hover:scale-105 transition-transform duration-500 ${isUpcoming ? 'opacity-80' : ''}`}
                                    src={campaign.imageUrl ? `/images/${campaign.imageUrl}` : "https://via.placeholder.com/300x200"}
                                />
                                <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent"></div>
                                <div className="absolute top-3 left-3 flex gap-2">
                                    {(status.status === "Đang mở" || status.status === "Đã phê duyệt") && (
                                        <span className="bg-green-100 text-green-700 text-[10px] font-bold px-2 py-1 rounded-sm uppercase border border-green-200 shadow-sm">Đang mở</span>
                                    )}
                                    {status.status === "Sắp diễn ra" && (
                                        <span className="bg-amber-100 text-amber-700 text-[10px] font-bold px-2 py-1 rounded-sm uppercase border border-amber-200 shadow-sm">Sắp diễn ra</span>
                                    )}
                                    {status.status === "Đã kết thúc" && (
                                        <span className="bg-slate-100 text-slate-600 text-[10px] font-bold px-2 py-1 rounded-sm uppercase border border-slate-200 shadow-sm">Đã kết thúc</span>
                                    )}
                                </div>
                            </div>
                            
                            {/* Content */}
                            <div className="p-6 flex-1 flex flex-col">
                                <h3 className="text-lg font-bold text-slate-900 mb-4 h-7 truncate">
                                    {campaign.tenChienDich}
                                </h3>
                                
                                <div className="space-y-3 mb-6">
                                    {/* Location */}
                                    <div className="flex items-start gap-3 text-sm text-slate-600">
                                        <span className="material-symbols-outlined text-red-700 text-lg">location_on</span>
                                        <span className="line-clamp-2 leading-relaxed">
                                            {campaign.diaDiem?.tenDiaDiem}, {campaign.diaDiem?.diaChiChiTiet}
                                        </span>
                                    </div>
                                    
                                    {/* Date */}
                                    <div className="flex items-center gap-3 text-sm text-slate-600">
                                        <span className="material-symbols-outlined text-red-700 text-lg">calendar_month</span>
                                        <span>{campaign.thoiGianBD} - {campaign.thoiGianKT}</span>
                                    </div>
                                    
                                    {/* Target */}
                                    <div className="flex items-center gap-3 text-sm text-slate-600">
                                        <span className="material-symbols-outlined text-red-700 text-lg">target</span>
                                        <span className="font-semibold text-slate-900">Chỉ tiêu: {campaign.soLuongDuKien} đơn vị</span>
                                    </div>
                                </div>
                                
                                {/* Progress */}
                                <div className="mt-auto">
                                    <div className="flex justify-between text-[10px] mb-2 font-medium text-slate-500 uppercase tracking-wide">
                                        <span>Tiến độ thu nhận</span>
                                        <span className="font-bold text-slate-700">
                                            {campaign.luongMauDaThu} / {campaign.soLuongDuKien} ({percentage}%)
                                        </span>
                                    </div>
                                    <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden mb-6">
                                        <div 
                                            className={`h-full ${isUpcoming ? 'bg-slate-300' : 'bg-red-700'}`}
                                            style={{ width: `${isUpcoming ? 0 : percentage}%` }}
                                        ></div>
                                    </div>
                                    
                                    {/* Button */}
                                    {isUpcoming ? (
                                        <button 
                                            disabled
                                            className="w-full h-11 bg-slate-100 border border-slate-200 text-slate-400 rounded-md font-bold uppercase tracking-wide text-sm cursor-not-allowed"
                                        >
                                            Chưa mở đăng ký
                                        </button>
                                    ) : (
                                        <button 
                                            disabled={status.status === "Đã kết thúc"}
                                            onClick={() => {
                                                localStorage.setItem('selectedCampaign', JSON.stringify(campaign));
                                                navigate('/khai-bao-thong-tin-ca-nhan');
                                            }}
                                            className="w-full h-11 bg-red-700 text-white rounded-md font-bold uppercase tracking-wide text-sm hover:bg-red-800 transition-colors shadow-md active:scale-[0.98] 
                                                    disabled:bg-slate-400 disabled:cursor-not-allowed disabled:hover:bg-slate-400"
                                        >
                                            {status.status === "Đã kết thúc" ? "Đã kết thúc" : "Đăng ký tham gia"}
                                        </button>
                                    )}
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            {filteredCampaigns.length === 0 && (
                <div className="w-full text-center py-12">
                    <p className="text-slate-500">Không tìm thấy chiến dịch phù hợp.</p>
                </div>
            )}

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="mt-12 flex justify-center items-center gap-2">
                    <button
                        onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                        disabled={currentPage === 1}
                        className="w-10 h-10 flex items-center justify-center rounded-md border border-slate-200 text-slate-400 hover:bg-white transition-colors bg-white shadow-sm disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <span className="material-symbols-outlined">chevron_left</span>
                    </button>
                    
                    {getPaginationPages().map((page, idx) => (
                        page === '...' ? (
                            <span key={`dots-${idx}`} className="w-10 text-center text-slate-400 font-bold tracking-widest">...</span>
                        ) : (
                            <button
                                key={page}
                                onClick={() => setCurrentPage(page)}
                                className={`w-10 h-10 flex items-center justify-center rounded-md font-bold shadow-sm transition-colors ${
                                    currentPage === page
                                        ? 'border-2 border-red-700 bg-red-50 text-red-700'
                                        : 'border border-slate-200 text-slate-600 hover:bg-slate-50 bg-white'
                                }`}
                            >
                                {page}
                            </button>
                        )
                    ))}
                    
                    <button
                        onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                        disabled={currentPage === totalPages}
                        className="w-10 h-10 flex items-center justify-center rounded-md border border-slate-200 text-slate-400 hover:bg-white transition-colors bg-white shadow-sm disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <span className="material-symbols-outlined">chevron_right</span>
                    </button>
                </div>
            )}
        </main>
    );
}

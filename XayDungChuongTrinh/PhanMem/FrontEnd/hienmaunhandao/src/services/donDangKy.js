import http from '../utils/http';

export const donDangKyService = {
    /**
     * Tạo đơn đăng ký mới
     * @param {Object} data - Thông tin đơn đăng ký
     * @param {string} data.maTNV - Mã tình nguyện viên (bắt buộc)
     * @param {string} data.maChienDich - Mã chiến dịch (bắt buộc)
     * @param {number} data.theTich - Thể tích máu hiến: 250, 350, hoặc 450
     * @returns {Promise<Object>} Đơn đăng ký vừa tạo với maDon
     */
    create: async (data) => {
        try {
            const response = await http.post('/dondangky', data);
            // response = ApiResponse { status, message, data: DonDangKyResponse }
            // Khi status=false (đã đăng ký rồi), backend trả 201 nhưng status=false
            if (response?.status === false) {
                throw { message: response.message || 'Lỗi khi tạo đơn đăng ký', isBusinessError: true };
            }
            if (!response?.data?.maDon) {
                throw new Error('Không lấy được mã đơn đăng ký từ response');
            }
            return response?.data;
        } catch (error) {
            // Re-throw business errors trực tiếp
            if (error.isBusinessError) throw error;
            console.error('Error creating đơn đăng ký:', error);
            throw {
                message: error.response?.data?.message || error.message || 'Lỗi khi tạo đơn đăng ký',
                status: error.response?.status,
                data: error.response?.data
            };
        }
    },


    /**
     * Lấy danh sách đơn đăng ký
     * @returns {Promise<Array>} Danh sách đơn đăng ký
     */
    getAll: async () => {
        try {
            const response = await http.get('/dondangky');
            return response?.data || [];
        } catch (error) {
            console.error('Error fetching danh sách đơn đăng ký:', error);
            throw error;
        }
    },

    /**
     * Lấy đơn đăng ký theo ID
     * @param {string} id - ID đơn đăng ký
     * @returns {Promise<Object>} Thông tin đơn đăng ký
     */
    getById: async (id) => {
        try {
            const response = await http.get(`/dondangky/${id}`);
            return response?.data || response;
        } catch (error) {
            console.error(`Error fetching đơn đăng ký ${id}:`, error);
            throw error;
        }
    },

    /**
     * Cập nhật đơn đăng ký
     * @param {string} id - ID đơn đăng ký
     * @param {Object} data - Dữ liệu cập nhật
     * @returns {Promise<Object>} Đơn đăng ký đã cập nhật
     */
    /**
     * Kiểm tra TNV đã đăng ký chiến dịch chưa
     * @param {string} maTNV - Mã tình nguyện viên
     * @param {string} maChienDich - Mã chiến dịch
     * @returns {Promise<Object|null>} Đơn đăng ký nếu đã có, null nếu chưa
     */
    checkDaDangKy: async (maTNV, maChienDich) => {
        try {
            const response = await http.get(`/dondangky/check?maTNV=${maTNV}&maChienDich=${maChienDich}`);
            return response?.data || null;
        } catch (error) {
            // 404 = chưa đăng ký, không phải lỗi
            if (error.response?.status === 404) return null;
            console.error('Error checking đơn đăng ký:', error);
            return null;
        }
    }
};


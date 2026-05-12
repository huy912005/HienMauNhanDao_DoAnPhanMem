import http from '../utils/http';

export const tinhNguyenVienService = {
  /**
   * Tạo mới hoặc cập nhật tình nguyện viên theo maTaiKhoan/email.
   * Đây là endpoint chính khi TNV điền thông tin cá nhân khi đăng ký.
   * @param {Object} data - Thông tin tình nguyện viên
   * @returns {Promise<Object>} Thông tin TNV với maTNV
   */
  createOrUpdate: async (data) => {
    try {
      const response = await http.put('/tinhnguyenvien/dang-ky', data);
      return response?.data || response;
    } catch (error) {
      console.error('Error creating/updating tình nguyện viên:', error);
      throw {
        message: error.response?.data?.message || 'Lỗi khi lưu thông tin tình nguyện viên',
        status: error.response?.status,
        data: error.response?.data
      };
    }
  },

  /**
   * Lấy thông tin TNV theo maTaiKhoan hoặc email (để pre-fill form).
   * @param {string} maTaiKhoan - maTaiKhoan hoặc email
   * @returns {Promise<Object>} Thông tin TNV hoặc null nếu chưa có
   */
  getByMaTaiKhoan: async (maTaiKhoan) => {
    try {
      const response = await http.get(`/tinhnguyenvien/tai-khoan/${encodeURIComponent(maTaiKhoan)}`);
      // status = false nghĩa là chưa có TNV
      if (!response?.status) return null;
      return response?.data || null;
    } catch (error) {
      console.error('Error fetching tình nguyện viên:', error);
      return null; // Không throw, chỉ trả null nếu chưa có
    }
  },

  /**
   * Tạo mới tình nguyện viên (dùng khi cần force create)
   */
  create: async (data) => {
    try {
      const response = await http.post('/tinhnguyenvien', data);
      return response?.data || response;
    } catch (error) {
      console.error('Error creating tình nguyện viên:', error);
      throw {
        message: error.response?.data?.message || 'Lỗi khi tạo tình nguyện viên',
        status: error.response?.status,
        data: error.response?.data
      };
    }
  },
  getAll: async () => {
    try {
      const response = await http.get('/tinhnguyenvien');
      return response?.data || response;
    } catch (error) {
      console.error('Error fetching tình nguyện viên list:', error);
      throw {
        message: error.response?.data?.message || 'Lỗi khi lấy danh sách tình nguyện viên',
        status: error.response?.status,
        data: error.response?.data
      };
    }
  }
};

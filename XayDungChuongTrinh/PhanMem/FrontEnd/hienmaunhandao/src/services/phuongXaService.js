import http from '../utils/http';

export const phuongXaService = {
  getAll: async () => {
    try {
      const response = await http.get('/phuongxa');
      return response?.data || response;
    } catch (error) {
      console.error('Error fetching phuong xa:', error);
      throw error;
    }
  }
};

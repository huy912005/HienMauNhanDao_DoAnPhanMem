import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  login: (data) => axios.post('http://localhost:8080/auth/login', data),
  register: (data) => axios.post('http://localhost:8080/auth/register', data),
  sendOtp: (email) => axios.post('http://localhost:8080/auth/send-otp', { email }),
  verifyOtp: (data) => axios.post('http://localhost:8080/auth/verify-otp', data),
};

export const homeService = {
  getHomeData: () => api.get('/public/trang-chu'),
};

export default api;

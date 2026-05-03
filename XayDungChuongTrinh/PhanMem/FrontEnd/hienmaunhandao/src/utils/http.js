import axios from 'axios';
import { API_BASE_URL } from '../constants/api';

const http = axios.create({
  baseURL: API_BASE_URL,
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor: auto extract .data từ ApiResponse
http.interceptors.response.use(
  (response) => {
    // Trả về response.data thay vì toàn bộ response
    // Vì axios wrap response vào {data: {...}, status, headers, ...}
    return response.data;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default http;

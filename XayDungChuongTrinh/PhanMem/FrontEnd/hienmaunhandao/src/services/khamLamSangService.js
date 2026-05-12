import http from '../utils/http';

export const thuNhanMauService = {
  getAll: () => http.get('/tuimau'),
  getStats: () => http.get('/tuimau/stats'),
  create: (data) => http.post('/tuimau', data),
  updateStatus: (id, status) => http.patch(`/tuimau/${id}/status?status=${encodeURIComponent(status)}`),
  delete: (id) => http.delete(`/tuimau/${id}`),
};

export const khamLamSangService = {
  getAll: () => http.get('/ketqualamsang'),
  getWaiting: () => http.get('/ketqualamsang/waiting'),
  getStats: () => http.get('/ketqualamsang/stats'),
  save: (data) => http.post('/ketqualamsang', data),
  update: (maKQ, data) =>
    http.post(`/ketqualamsang/${encodeURIComponent(String(maKQ).trim())}/cap-nhat`, data),
  delete: (id) => http.delete(`/ketqualamsang/${id}`),
};

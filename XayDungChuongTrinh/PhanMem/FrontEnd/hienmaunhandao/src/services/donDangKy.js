import http from '../utils/http';
export const donDangKyService = {
    createDonDangKy : (data) => http.post('/dondangky', data),
}
import http from "../utils/http";

export const hoSoSucKhoeService = {
    getHoSo  :()=>http.get('/hososuckhoe'),
    getHoSoById : (id) => http.get(`/hososuckhoe/${id}`),
    createHoSo : (data) => http.post('/hososuckhoe', data),
    updateHoSo : (id, data) => http.put(`/hososuckhoe/${id}`, data),
}
import http from "../utils/http";

export const chienDichService = {
    getChienDichs: () => http.get('/chiendich'),
    getChienDichById: (id) => http.get(`/chiendich/${id}`),
    createChienDich: (data) => http.post('/chiendich', data),
    updateChienDich: (id, data) => http.put(`/chiendich/${id}`, data),
    deleteChienDich: (id) => http.delete(`/chiendich/${id}`),
};
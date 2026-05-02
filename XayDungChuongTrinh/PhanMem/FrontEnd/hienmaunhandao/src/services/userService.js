import http from '../utils/http';

export const userService = {
  getUsers: () => http.get('/users'),
  getUserById: (id) => http.get(`/users/${id}`),
  createUser: (data) => http.post('/users', data),
  updateUser: (id, data) => http.put(`/users/${id}`, data),
};

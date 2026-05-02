package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.request.UserRequest;
import com.Nhom20.DoAnPhamMem.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse update(String id, UserRequest request);
    UserResponse getById(String id);
    List<UserResponse> getAll();
}

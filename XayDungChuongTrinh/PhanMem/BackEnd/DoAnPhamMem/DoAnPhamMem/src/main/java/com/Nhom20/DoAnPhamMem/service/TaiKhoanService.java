package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.LoginRequest;
import com.Nhom20.DoAnPhamMem.dto.LoginResponse;
import com.Nhom20.DoAnPhamMem.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface TaiKhoanService {
    LoginResponse login(LoginRequest loginRequest);
    void logout(HttpServletRequest request);
    void register(RegisterRequest registerRequest);
}

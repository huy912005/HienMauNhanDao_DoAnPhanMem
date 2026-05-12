package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.request.LoginRequest;
import com.Nhom20.DoAnPhamMem.dto.response.LoginResponse;
import com.Nhom20.DoAnPhamMem.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface TaiKhoanService {
    LoginResponse login(LoginRequest loginRequest);
    void logout(HttpServletRequest request);
    void register(RegisterRequest registerRequest);
//    String getMaTaiKhoanByEmail(String email);
}

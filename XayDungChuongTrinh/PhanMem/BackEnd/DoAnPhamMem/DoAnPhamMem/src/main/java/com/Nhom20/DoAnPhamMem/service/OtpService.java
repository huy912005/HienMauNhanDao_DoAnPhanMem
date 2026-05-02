package com.Nhom20.DoAnPhamMem.service;

public interface OtpService {
    String generateOtp(String email);
    boolean validateOtp(String email, String otp);
}

package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.LoginRequest;
import com.Nhom20.DoAnPhamMem.dto.LoginResponse;
import com.Nhom20.DoAnPhamMem.dto.RegisterRequest;
import com.Nhom20.DoAnPhamMem.service.TaiKhoanService;
import com.Nhom20.DoAnPhamMem.service.OtpService;
import com.Nhom20.DoAnPhamMem.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final TaiKhoanService taiKhoanService;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthController(TaiKhoanService taiKhoanService, OtpService otpService, EmailService emailService) {
        this.taiKhoanService = taiKhoanService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(taiKhoanService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        taiKhoanService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        taiKhoanService.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty())
            return ResponseEntity.badRequest().body("Bắt buộc nhập Email");
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("Gửi OTP thành công!");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (email == null || otp == null)
            return ResponseEntity.badRequest().body("Email and OTP are required");
        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid)
            return ResponseEntity.ok("OTP is valid");
        else
            return ResponseEntity.status(400).body("Invalid or expired OTP");
    }
}

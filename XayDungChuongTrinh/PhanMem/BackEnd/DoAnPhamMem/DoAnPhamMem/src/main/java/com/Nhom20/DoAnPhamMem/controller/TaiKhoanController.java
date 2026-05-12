package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taikhoan")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    /**
     * Lấy mã tài khoản từ email
     * GET /api/taikhoan/ma-by-email?email=...
     */
    @GetMapping("/ma-by-email")
    public ResponseEntity<?> getMaTaiKhoanByEmail(@RequestParam String email) {
        try {
            String maTaiKhoan = taiKhoanService.getMaTaiKhoanByEmail(email);
            if (maTaiKhoan == null) {
                return ResponseEntity.status(404).body(new ErrorResponse("Email không tồn tại trong hệ thống", 404));
            }
            return ResponseEntity.ok(new SuccessResponse("Lấy mã tài khoản thành công", maTaiKhoan));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Lỗi server: " + e.getMessage(), 500));
        }
    }

    // Inner classes để response
    static class SuccessResponse {
        public String message;
        public Object data;

        public SuccessResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    static class ErrorResponse {
        public String message;
        public int code;

        public ErrorResponse(String message, int code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }
}

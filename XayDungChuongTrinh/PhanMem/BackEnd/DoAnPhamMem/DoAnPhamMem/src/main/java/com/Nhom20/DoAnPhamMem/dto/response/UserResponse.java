package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String maTaiKhoan;
    private String email;
    private String vaiTro;
    private Boolean trangThai;
}

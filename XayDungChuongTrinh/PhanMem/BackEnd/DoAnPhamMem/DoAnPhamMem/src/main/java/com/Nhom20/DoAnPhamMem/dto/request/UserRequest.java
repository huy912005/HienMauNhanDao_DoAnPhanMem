package com.Nhom20.DoAnPhamMem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String matKhau;
    private String maVaiTro;
    private Boolean trangThai;
}

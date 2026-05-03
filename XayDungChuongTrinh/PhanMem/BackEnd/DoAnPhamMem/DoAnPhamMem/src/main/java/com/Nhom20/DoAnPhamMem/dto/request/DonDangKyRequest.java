package com.Nhom20.DoAnPhamMem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonDangKyRequest {

    @NotBlank(message = "Mã đơn không được để trống")
    @Size(max = 20, message = "Mã đơn không vượt quá 20 ký tự")
    private String maDon;

    @NotBlank(message = "Mã tình nguyện viên không được để trống")
    private String maTNV;

    @NotBlank(message = "Mã chiến dịch không được để trống")
    private String maChienDich;

    // Có thể null nên không dùng @NotBlank
    private String maNhanVien;

    @NotBlank(message = "Mã QR không được để trống")
    private String maQR;
}

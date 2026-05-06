package com.Nhom20.DoAnPhamMem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonDangKyRequest {

    @NotBlank(message = "Mã tình nguyện viên không được để trống")
    private String maTNV;

    @NotBlank(message = "Mã chiến dịch không được để trống")
    private String maChienDich;

    // Có thể null nên không dùng @NotBlank
    private String maNhanVien;

    // Thể tích máu hiến (250, 350, 450), gửi từ frontend
    private Integer theTich;
}

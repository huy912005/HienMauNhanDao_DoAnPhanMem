package com.Nhom20.DoAnPhamMem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiendichHienMauRequest {
    @NotBlank(message = "Mã chiến dịch không được để trống")
    private String maChienDich;

    @NotBlank(message = "Mã địa điểm không được để trống")
    private String maDiaDiem;

    private String maNhanVien;

    @NotBlank(message = "Tên chiến dịch không được để trống")
    private String tenChienDich;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime thoiGianBD;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime thoiGianKT;

    @Positive(message = "Số lượng dự kiến phải lớn hơn 0")
    private Integer soLuongDuKien;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai;

    private String maQR;

    private String imageUrl;
}

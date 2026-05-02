package com.Nhom20.DoAnPhamMem.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChienDichHienMauDto {
    private String maChienDich;
    private String tenChienDich;
    private LocalDateTime thoiGianBD;
    private LocalDateTime thoiGianKT;
    private Integer soLuongDuKien;
    private String trangThai;
}

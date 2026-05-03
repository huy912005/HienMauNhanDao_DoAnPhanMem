package com.Nhom20.DoAnPhamMem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiendichHienMauResponse {
    private String maChienDich;
    private DiaDiemResponse diaDiem;
    private String maNhanVien;
    private String tenChienDich;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime thoiGianBD;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime thoiGianKT;

    private Integer soLuongDuKien;
    private String trangThai;
    private String maQR;
    private String imageUrl;
    private Integer luongMauDaThu;
}
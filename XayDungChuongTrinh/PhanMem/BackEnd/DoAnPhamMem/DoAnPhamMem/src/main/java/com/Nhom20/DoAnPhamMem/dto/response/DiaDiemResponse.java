package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaDiemResponse {
    private String maDiaDiem;
    private String tenDiaDiem;
    private String diaChiChiTiet;
    private String loaiDiaDiem;
}

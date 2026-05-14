package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuiMauResponse {
    private String maTuiMau;
    private String maDon;
    private String tenTinhNguyenVien;
    private String nhomMau;
    private String theTich;
    private LocalDateTime thoiGianLayMau;
    private String trangThai;
    private String tenChienDich;
    private String tenBacSi;
    private String maBacSi;
}

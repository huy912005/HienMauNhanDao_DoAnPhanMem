package com.Nhom20.DoAnPhamMem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuiMauRequest {
    private String maDon;
    private String maNV;
    private Integer theTich;
    private LocalDateTime thoiGianLayMau;
    private Double nhietDoVanChuyen;
}

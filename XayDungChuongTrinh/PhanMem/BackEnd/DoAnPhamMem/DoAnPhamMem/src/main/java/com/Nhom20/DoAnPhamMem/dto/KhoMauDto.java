package com.Nhom20.DoAnPhamMem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KhoMauDto {
    private String maKho;
    private String nhomMau;
    private Integer soLuongTon;
    private Integer nguongAnToan;
}

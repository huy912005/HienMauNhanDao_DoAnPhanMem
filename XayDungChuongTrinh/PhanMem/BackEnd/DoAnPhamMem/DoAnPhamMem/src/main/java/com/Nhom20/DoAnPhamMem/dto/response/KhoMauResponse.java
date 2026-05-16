package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhoMauResponse {
    private String maKho;
    private String nhomMau;
    private Integer soLuongTon;
    private Integer nguongAnToan;
}

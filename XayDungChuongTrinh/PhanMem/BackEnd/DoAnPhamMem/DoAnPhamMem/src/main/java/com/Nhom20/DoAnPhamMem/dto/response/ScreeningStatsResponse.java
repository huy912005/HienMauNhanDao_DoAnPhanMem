package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningStatsResponse {
    private long tongSo;
    private long datYeuCau;
    private long khongDat;
}

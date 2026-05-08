package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionStatsResponse {
    private long tongSoTui;
    private double tongTheTich;
    private Map<String, Long> theoNhomMau;
    private Map<String, Long> theoTheTich;
}

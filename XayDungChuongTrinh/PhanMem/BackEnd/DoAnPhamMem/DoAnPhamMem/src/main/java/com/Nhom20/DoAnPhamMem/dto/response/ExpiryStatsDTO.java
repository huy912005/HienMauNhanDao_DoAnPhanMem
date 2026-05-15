package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiryStatsDTO {
    private long expiredCount;
    private long nearExpiryCount;
    private long safeCount;
    private boolean hasCritical; // Cờ báo hiệu có túi quá hạn 20-30 ngày
}

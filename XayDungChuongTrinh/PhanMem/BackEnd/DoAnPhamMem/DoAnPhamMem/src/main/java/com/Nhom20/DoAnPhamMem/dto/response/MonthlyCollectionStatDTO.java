package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyCollectionStatDTO {
    private int month; // Tháng thu nhận
    private int totalUnits; // Tổng số túi máu thu được trong tháng
}

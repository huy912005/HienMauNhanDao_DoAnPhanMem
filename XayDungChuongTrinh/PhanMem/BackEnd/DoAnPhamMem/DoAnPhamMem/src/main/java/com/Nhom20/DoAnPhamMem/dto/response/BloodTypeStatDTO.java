package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodTypeStatDTO {
    private String bloodType; // Nhóm máu (O+, A+, ...)
    private int quantity; // Số lượng tồn kho
}

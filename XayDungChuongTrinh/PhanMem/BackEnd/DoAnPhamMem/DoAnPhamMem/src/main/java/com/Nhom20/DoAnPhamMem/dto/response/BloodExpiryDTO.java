package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodExpiryDTO {
    private String maTuiMau;
    private String maChienDich;
    private String nhomMau;
    private Integer theTich;
    private LocalDateTime thoiGianLayMau;
    private LocalDateTime ngayHetHan;
    private String trangThaiHan; // EXPIRED, NEAR_EXPIRY, SAFE
    private Long daysRemaining;
}

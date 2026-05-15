package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private String maTuiMau;
    private String nhomMau;
    private Date ngayThuNhan;
    private String trangThai;
    private Integer theTich;
    private Double nhietDoVanChuyen;
    private Date ngayHetHan;
    private String tinhTrangHSD;
    private String maChienDich; // Thêm trường mã chiến dịch
}

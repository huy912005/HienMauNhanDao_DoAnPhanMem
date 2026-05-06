package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienResponse {
    private String maNV;
    private String hoVaTen;
    private String maKhoa;
    private String cccd;
    private String gioiTinh;
    private String soDienThoai;
}

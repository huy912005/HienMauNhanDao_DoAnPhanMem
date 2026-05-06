package com.Nhom20.DoAnPhamMem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonDangKyResponse {

    private String maDon;
    private TinhNguyenVienReSponse tinhNguyenVien;
    private String maTNV;
    private String maChienDich;
    private String maNV;
    private String maQR;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianDangKy;
    private String trangThai;
}
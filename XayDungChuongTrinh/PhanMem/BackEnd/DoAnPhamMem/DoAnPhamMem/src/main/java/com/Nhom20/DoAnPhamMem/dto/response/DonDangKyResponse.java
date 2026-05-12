package com.Nhom20.DoAnPhamMem.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonDangKyResponse {

    private String maDon;
    private TinhNguyenVienReSponse tinhNguyenVien;
    private ChienDichDonDangKyResponse chienDich;
    private String maTNV;
    private String maChienDich;
    private String maNV;
    private String maQR;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianDangKy;
    private String trangThai;
    private Integer theTich;
    private String tenBacSi;
}
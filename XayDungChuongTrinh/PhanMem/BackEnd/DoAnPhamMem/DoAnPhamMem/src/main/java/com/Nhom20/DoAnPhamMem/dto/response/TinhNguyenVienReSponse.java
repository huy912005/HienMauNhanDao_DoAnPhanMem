package com.Nhom20.DoAnPhamMem.dto.response;

import com.Nhom20.DoAnPhamMem.enums.GioiTinh;
import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinhNguyenVienReSponse {
    private String maTNV;
    private String hoVaTen;
    private String soCCCD;
    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;
    private String soDienThoai;
    private NhomMau nhomMau;
    private String diaChi;
    private String maTaiKhoan;
    private String email;
    private Boolean trangThai;
    private String maPhuongXa;
}

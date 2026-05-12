package com.Nhom20.DoAnPhamMem.dto.request;

import com.Nhom20.DoAnPhamMem.enums.GioiTinh;
import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TinhNguyenVienRequest {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100)
    private String hoVaTen;
    @NotBlank(message = "CCCD không được để trống")
    @Size(min = 12, max = 12, message = "CCCD phải 12 số")
    private String soCCCD;
    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9])[0-9]{8}$", message = "SĐT không hợp lệ")
    private String soDienThoai;
    private NhomMau nhomMau;
    @Size(max = 255)
    private String diaChi;
//    @NotBlank(message = "Mã tài khoản không được để trống")
    private String maTaiKhoan;
    @NotBlank(message = "Mã phường xã không được để trống")
    private String maPhuongXa;
}

package com.Nhom20.DoAnPhamMem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TuiMauRequest {
    private String maTuiMau;

    @NotBlank(message = "Mã đơn không được để trống")
    private String maDon;

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNhanVien;

    @NotBlank(message = "Mã kho không được để trống")
    private String maKho;

    @NotNull(message = "Thể tích không được để trống")
    @Positive(message = "Thể tích phải lớn hơn 0")
    private Integer theTich;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianLayMau;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai;

    private Double nhietDoVanChuyen;
}
package com.Nhom20.DoAnPhamMem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class PhuongXaRequest {
    @NotBlank(message = "Tên phường/xã không được để trống")
    @Size(max = 100, message = "Tên phường/xã tối đa 100 ký tự")
    private String tenPhuongXa;
}

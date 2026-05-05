package com.Nhom20.DoAnPhamMem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TuiMauResponse {
    private String maTuiMau;
    private String maDon;
    private String maNhanVien;
    private String maKho;
    private Integer theTich;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianLayMau;

    private String trangThai;
    private Double nhietDoVanChuyen;
}
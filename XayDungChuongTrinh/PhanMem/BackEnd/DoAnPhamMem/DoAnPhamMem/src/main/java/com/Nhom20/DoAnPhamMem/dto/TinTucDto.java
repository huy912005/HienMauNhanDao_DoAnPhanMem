package com.Nhom20.DoAnPhamMem.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TinTucDto {
    private String maTinTuc;
    private String tieuDe;
    private String noiDung;
    private String hinhAnh;
    private LocalDateTime ngayDang;
    private String trangThai;
}

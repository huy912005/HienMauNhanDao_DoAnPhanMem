package com.Nhom20.DoAnPhamMem.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TrangChuResponse {
    private List<ChienDichHienMauDto> chienDichDangHoatDong;
    private int tongLuongMauDaThu;
    private List<KhoMauDto> luongMauTonKho;
    private List<TinTucDto> hoatDongGanDay;
}

package com.Nhom20.DoAnPhamMem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportBloodRequestDTO {
    private String maNhanVien;
    private List<String> maTuiMauList;
}

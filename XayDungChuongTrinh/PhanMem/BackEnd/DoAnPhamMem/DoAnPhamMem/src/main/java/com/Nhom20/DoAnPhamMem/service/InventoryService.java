package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO;
import com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO;
import com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO;
import com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO;
import com.Nhom20.DoAnPhamMem.dto.request.ImportBloodRequestDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface InventoryService {
    // 1. Lấy thống kê cho 4 thẻ thông tin
    DashboardStatsDTO getDashboardStats();

    // 2. Lấy dữ liệu cho biểu đồ tròn (Tồn kho theo nhóm máu)
    List<BloodTypeStatDTO> getInventoryByBloodType();

    // 3. Lấy dữ liệu cho biểu đồ cột (Lượng máu thu được theo tháng trong năm)
    List<MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year);

    // 4. Lấy danh sách túi máu (phân trang, tìm kiếm)
    Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType);

    // 5. Hủy/xóa một túi máu
    void deleteBloodUnit(String maTuiMau);

    // 6. Quét mã vạch túi máu
    BloodUnitDTO scanBloodUnit(String maTuiMau);

    // 7. Hoàn tất lưu phiếu nhập kho
    void importBloodUnits(ImportBloodRequestDTO requestDTO);
}

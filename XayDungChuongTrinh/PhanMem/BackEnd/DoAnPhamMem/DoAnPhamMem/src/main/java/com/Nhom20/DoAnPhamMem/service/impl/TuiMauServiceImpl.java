package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.response.CollectionStatsResponse;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.enums.TheTichTuiMau;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.mapper.TuiMauMapper;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuiMauServiceImpl implements TuiMauService {

        private final TuiMauRepository tuiMauRepository;
        private final TuiMauMapper tuiMauMapper;

        @Override
        public List<TuiMauResponse> getAll() {
                return tuiMauRepository.findAll().stream()
                                .map(tuiMauMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public CollectionStatsResponse getStats() {
                List<TuiMauEntity> all = tuiMauRepository.findAll();
                long tongSoTui = all.size();
                double tongTheTich = all.stream()
                                .mapToDouble(tm -> tm.getTheTich() != null ? tm.getTheTich().getMl() : 0)
                                .sum();

                Map<String, Long> theoNhomMau = all.stream()
                                .filter(tm -> tm.getDonDangKy() != null && tm.getDonDangKy().getTinhNguyenVien() != null
                                                && tm.getDonDangKy().getTinhNguyenVien().getNhomMau() != null)
                                .collect(Collectors.groupingBy(
                                                tm -> tm.getDonDangKy().getTinhNguyenVien().getNhomMau().getDbValue(),
                                                Collectors.counting()));

                Map<String, Long> theoTheTich = all.stream()
                                .filter(tm -> tm.getTheTich() != null)
                                .collect(Collectors.groupingBy(tm -> String.valueOf(tm.getTheTich().getMl()),
                                                Collectors.counting()));

                return CollectionStatsResponse.builder()
                                .tongSoTui(tongSoTui)
                                .tongTheTich(tongTheTich)
                                .theoNhomMau(theoNhomMau)
                                .theoTheTich(theoTheTich)
                                .build();
        }

        @Override
        public void delete(String maTuiMau) {
                tuiMauRepository.deleteById(maTuiMau);
        }

        @Override
        public void updateStatus(String maTuiMau, String status) {
                tuiMauRepository.findById(maTuiMau).ifPresent(tuiMau -> {
                        tuiMau.setTrangThai(TrangThaiTuiMau.fromDbValue(status));
                        tuiMauRepository.save(tuiMau);
                });
        }
}

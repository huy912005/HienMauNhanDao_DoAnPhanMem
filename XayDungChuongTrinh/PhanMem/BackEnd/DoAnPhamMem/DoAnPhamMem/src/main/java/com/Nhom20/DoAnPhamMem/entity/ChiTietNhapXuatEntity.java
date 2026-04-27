package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHITIETNHAPXUAT")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietNhapXuatEntity {
    @EmbeddedId
    private ChiTietNhapXuatId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPhieu")
    @JoinColumn(name = "maPhieu")
    private PhieuNhapXuatEntity phieuNhapXuat;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maTuiMau")
    @JoinColumn(name = "maTuiMau")
    private TuiMauEntity tuiMau;
}

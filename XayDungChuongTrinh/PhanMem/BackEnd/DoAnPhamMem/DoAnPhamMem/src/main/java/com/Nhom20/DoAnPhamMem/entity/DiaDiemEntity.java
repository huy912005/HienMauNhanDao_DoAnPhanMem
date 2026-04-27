package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DIADIEM")
@Getter
@Setter
@NoArgsConstructor
public class DiaDiemEntity {
    @Id
    @Column(name = "maDiaDiem", length = 10)
    private String maDiaDiem;
    @Column(name = "tenDiaDiem", nullable = false, length = 150)
    private String tenDiaDiem;
    @Column(name = "diaChiChiTiet", nullable = false, length = 255)
    private String diaChiChiTiet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhuongXa")
    private PhuongXaEntity phuongXa;
    @Column(name = "loaiDiaDiem", length = 50)
    private String loaiDiaDiem;
}

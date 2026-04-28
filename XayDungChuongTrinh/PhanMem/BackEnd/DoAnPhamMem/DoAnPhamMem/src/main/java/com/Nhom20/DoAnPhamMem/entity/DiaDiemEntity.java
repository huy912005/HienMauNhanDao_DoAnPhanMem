package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.LoaiDiaDiem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
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
    private LoaiDiaDiem loaiDiaDiem;
}

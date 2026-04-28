package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HOSOSUCKHOE")
@Getter
@Setter
@NoArgsConstructor
public class HoSoSucKhoeEntity {
    @Id
    @Column(name = "maHoSo", length = 10)
    private String maHoSo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;
    @Column(name = "benhLyNen")
    private String benhLyNen;
    @Column(name = "moTaKhac")
    private String moTaKhac;
}

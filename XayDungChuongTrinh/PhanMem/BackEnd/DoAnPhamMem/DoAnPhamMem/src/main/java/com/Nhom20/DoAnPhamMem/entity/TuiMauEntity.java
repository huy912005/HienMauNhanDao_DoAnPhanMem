package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "TUIMAU")
@Getter
@Setter
public class TuiMauEntity {
    @Id
    @Column(name = "maTuiMau", length = 15)
    private String maTuiMau;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKho")
    private KhoMauEntity khoMau;
    @Column(name = "theTich")
    private Integer theTich;
    @Column(name = "thoiGianLayMau")
    private LocalDateTime thoiGianLayMau;
    @Column(name = "trangThai", length = 50)
    private String trangThai;
}

package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "CHUNGNHAN")
@Getter
@Setter @NoArgsConstructor
public class ChungNhanEntity {
    @Id
    @Column(name = "maChungNhan", length = 10)
    private String maChungNhan;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVienKy;
    private String filePDF;
    private LocalDate ngayCap;
}

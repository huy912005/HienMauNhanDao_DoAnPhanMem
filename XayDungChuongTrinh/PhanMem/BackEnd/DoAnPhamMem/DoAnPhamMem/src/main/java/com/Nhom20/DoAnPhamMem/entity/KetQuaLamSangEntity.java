package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KETQUALAMSANG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaLamSangEntity {

    @Id
    @Column(name = "maKQ", length = 7)
    private String maKQ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVien;

    @Column(name = "huyetAp", length = 20)
    private String huyetAp;

    @Column(name = "nhipTim")
    private Integer nhipTim;

    @Column(name = "canNang")
    private Double canNang;

    @Column(name = "nhietDo")
    private Double nhietDo;

    @Column(name = "ketQua")
    private Boolean ketQua;

    @Column(name = "lyDoTuChoi")
    private String lyDoTuChoi;
}
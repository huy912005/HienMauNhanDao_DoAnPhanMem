package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KETQUALAMSANG")
@Getter
@Setter
@NoArgsConstructor
public class KetQuaLamSangEntity {
    @Id
    @Column(name = "maKQ", length = 10)
    private String maKQ;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity bacSiKham;
    private String huyetAp;
    private Integer nhipTim;
    private Double canNang;
    private Double nhietDo;
    private Boolean ketQua;
    private String lyDoTuChoi;
}

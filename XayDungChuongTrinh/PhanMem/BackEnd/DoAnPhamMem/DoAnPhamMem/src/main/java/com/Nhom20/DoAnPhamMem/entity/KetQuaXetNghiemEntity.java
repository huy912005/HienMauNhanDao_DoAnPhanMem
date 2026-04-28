package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KETQUAXETNGHIEM")
@Getter
@Setter
@NoArgsConstructor
public class KetQuaXetNghiemEntity {
    @Id
    @Column(name = "maKQ", length = 10)
    private String maKQ;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTuiMau")
    private TuiMauEntity tuiMau;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVienXetNghiem;
    @Column(name = "nhomMau", length = 5)
    private String nhomMau;
    private String moTa;
}

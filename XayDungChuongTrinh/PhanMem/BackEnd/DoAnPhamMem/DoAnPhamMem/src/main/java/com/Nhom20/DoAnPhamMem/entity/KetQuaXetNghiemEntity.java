package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.NhomMau;
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
    // NhomMauConverter (autoApply=true) sẽ tự chuyển đổi - không dùng @Enumerated
    private NhomMau nhomMau;
    @Column(name = "soLanXetNghiem")
    private Integer soLanXetNghiem;
    @Column(name = "ketQua")
    private Boolean ketQua;
    @Column(name = "moTa", length = 500)
    private String moTa;
}
package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.LoaiPhieuNhapXuat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PHIEUNHAPXUAT")
@Getter
@Setter
@NoArgsConstructor
public class PhieuNhapXuatEntity {
    @Id
    @Column(name = "maPhieu", length = 10)
    private String maPhieu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVienThucHien;
    @Column(name = "loaiPhieu", nullable = false, length = 50)
    private LoaiPhieuNhapXuat loaiPhieu;
    @Column(name = "ngayNhapXuat")
    private LocalDate ngayNhapXuat;
    @OneToMany(mappedBy = "phieuNhapXuat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietNhapXuatEntity> chiTietPhieu = new ArrayList<>();
}

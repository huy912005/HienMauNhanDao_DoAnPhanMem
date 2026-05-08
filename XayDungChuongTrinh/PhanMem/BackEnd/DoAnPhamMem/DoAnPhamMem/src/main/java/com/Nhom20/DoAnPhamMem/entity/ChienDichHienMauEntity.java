package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiChienDich;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHIENDICHHIENMAU")
@Getter
@Setter
@NoArgsConstructor
public class ChienDichHienMauEntity {
    @Id
    @Column(name = "maChienDich", length = 10)
    private String maChienDich;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDiaDiem")
    private DiaDiemEntity diaDiem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVienPhuTrach;
    @Column(name = "tenChienDich", nullable = false, length = 255)
    private String tenChienDich;
    @Column(name = "thoiGianBD", nullable = false)
    private LocalDateTime thoiGianBD;
    @Column(name = "thoiGianKT", nullable = false)
    private LocalDateTime thoiGianKT;
    @Column(name = "soLuongDuKien")
    private Integer soLuongDuKien;
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiChienDich trangThai;
    @Column(name = "maQR")
    private String maQR;
    @Column(name = "imageUrl")
    private String imageUrl;
}

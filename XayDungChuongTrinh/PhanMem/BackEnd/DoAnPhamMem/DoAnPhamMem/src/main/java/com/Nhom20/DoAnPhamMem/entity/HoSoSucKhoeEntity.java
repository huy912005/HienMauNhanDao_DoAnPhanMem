package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HOSOSUCKHOE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoSoSucKhoeEntity {

    @Id
    @Column(name = "maHoSo", length = 10)
    private String maHoSo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;

    // Thay thế benhLyNen bằng các trường Boolean theo ERD mới
    @Column(name = "khangSinh")
    private Boolean khangSinh;

    @Column(name = "truyenNhiem")
    private Boolean truyenNhiem;

    @Column(name = "dauHong")
    private Boolean dauHong;

    @Column(name = "coThai")
    private Boolean coThai;

    @Column(name = "moTaKhac", columnDefinition = "NVARCHAR(255)")
    private String moTaKhac;
}

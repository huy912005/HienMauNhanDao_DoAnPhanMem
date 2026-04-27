package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAIKHOAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanEntity {
    @Id
    @Column(name = "maTaiKhoan", length = 10)
    private String maTaiKhoan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maVaiTro")
    private VaiTroEntity vaiTro;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "matKhau", nullable = false)
    private String matKhau;
    @Column(name = "trangThai")
    private Boolean trangThai = true;
}

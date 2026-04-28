package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Quan trọng để Hibernate so sánh khóa
public class ChiTietNhapXuatId implements Serializable {
    @Column(name = "maPhieu", length = 10)
    private String maPhieu;
    @Column(name = "maTuiMau", length = 15)
    private String maTuiMau;
}

package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "VAITRO")
@Getter
@Setter
@NoArgsConstructor
public class VaiTroEntity {
    @Id
    @Column(name = "maVaiTro", length = 10)
    private String maVaiTro;
    @Column(name = "tenVaiTro", nullable = false, length = 50)
    private String tenVaiTro;
}

package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PHUONGXA")
@Getter
@Setter
@NoArgsConstructor
public class PhuongXaEntity {
    @Id
    @Column(name = "maPhuongXa", length = 10)
    private String maPhuongXa;
    @Column(name = "tenPhuongXa", nullable = false, length = 100)
    private String tenPhuongXa;
}

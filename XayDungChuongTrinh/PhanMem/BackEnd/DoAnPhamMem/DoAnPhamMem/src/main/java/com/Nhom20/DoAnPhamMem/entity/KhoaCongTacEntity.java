package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KHOACONGTAC")
@Getter
@Setter
@NoArgsConstructor
public class KhoaCongTacEntity {
    @Id
    @Column(name = "maKhoa", length = 10)
    private String maKhoa;
    @Column(name = "tenKhoa", nullable = false, length = 100)
    private String tenKhoa;
}

package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KHOMAU")
@Getter
@Setter
@NoArgsConstructor
public class KhoMauEntity {
    @Id
    @Column(name = "maKho", length = 10)
    private String maKho;
    @Column(name = "nhomMau", length = 10)
    private String nhomMau;
    private Integer soLuongTon;
    private Integer nguongAnToan ;
}

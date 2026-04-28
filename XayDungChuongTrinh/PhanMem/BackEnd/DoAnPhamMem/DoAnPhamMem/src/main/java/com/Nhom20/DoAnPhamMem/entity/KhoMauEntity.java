package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.NhomMau;
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
    private NhomMau nhomMau;
    private Integer soLuongTon;
    private Integer nguongAnToan ;
}

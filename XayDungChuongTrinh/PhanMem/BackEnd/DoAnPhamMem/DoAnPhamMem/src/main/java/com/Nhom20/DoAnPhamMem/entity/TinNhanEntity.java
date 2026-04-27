package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TINNHAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TinNhanEntity {
    @Id
    @Column(name = "maTinNhan", length = 10)
    private String maTinNhan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTaiKhoanGui")
    private TaiKhoanEntity nguoiGui;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTaiKhoanNhan")
    private TaiKhoanEntity nguoiNhan;
    @Lob
    @Column(name = "noiDung", columnDefinition = "TEXT")
    private String noiDung;
    @Column(name = "thoiGian")
    private LocalDateTime thoiGian = LocalDateTime.now();
    @Column(name = "trangThai")
    private Boolean daDoc = false;
}

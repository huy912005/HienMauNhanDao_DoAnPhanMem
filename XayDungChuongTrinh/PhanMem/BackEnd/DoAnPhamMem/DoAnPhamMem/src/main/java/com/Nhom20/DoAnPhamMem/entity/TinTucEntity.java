package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiTinTuc;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TINTUC")
@Getter
@Setter
@NoArgsConstructor
public class TinTucEntity {
    @Id
    @Column(name = "maTinTuc", length = 10)
    private String maTinTuc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nguoiDang;
    @Column(nullable = false)
    private String tieuDe;
    @Lob
    private String noiDung;
    private String hinhAnh;
    private LocalDateTime ngayDang = LocalDateTime.now();
    private TrangThaiTinTuc trangThai;
}

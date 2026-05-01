package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "INVALIDATED_TOKEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidatedTokenEntity {
    @Id
    private String id; // Token ID (usually the JTI or the token itself)

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;
}

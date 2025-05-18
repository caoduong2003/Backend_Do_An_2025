package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PasswordResetToken")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne(targetEntity = NguoiDung.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private NguoiDung nguoiDung;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    private boolean used;
}
package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CapDoHSK")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapDoHSK {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CapDo", nullable = false)
    private Integer capDo;

    @Column(name = "TenCapDo", nullable = false)
    private String tenCapDo;

    @Column(name = "MoTa")
    private String moTa;
}
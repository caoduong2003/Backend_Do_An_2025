package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LoaiBaiGiang")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoaiBaiGiang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "TenLoai", nullable = false)
    private String tenLoai;
    
    @Column(name = "MoTa")
    private String moTa;
}
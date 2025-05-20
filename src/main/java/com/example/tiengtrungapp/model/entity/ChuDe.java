package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChuDe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChuDe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "TenChuDe", nullable = false)
    private String tenChuDe;
    
    @Column(name = "MoTa")
    private String moTa;
    
    @Column(name = "HinhAnh")
    private String hinhAnh;
}
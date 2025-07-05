package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DapAn")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DapAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CauHoiId", nullable = false)
    private CauHoi cauHoi;

    @Column(name = "NoiDung", nullable = false)
    private String noiDung;

    @Column(name = "LaDapAnDung")
    private Boolean laDapAnDung;

    @Column(name = "ThuTu")
    private Integer thuTu;
}
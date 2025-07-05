package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietKetQua")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietKetQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KetQuaBaiTapId", nullable = false)
    private KetQuaBaiTap ketQuaBaiTap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CauHoiId", nullable = false)
    private CauHoi cauHoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DapAnDaChonId")
    private DapAn dapAnDaChon;

    @Column(name = "LaDung")
    private Boolean laDung;
}
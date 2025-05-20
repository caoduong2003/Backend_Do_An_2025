package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TuVung")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuVung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "BaiGiangID", nullable = false)
    private BaiGiang baiGiang;
    
    @Column(name = "TiengTrung", nullable = false)
    private String tiengTrung;
    
    @Column(name = "PhienAm")
    private String phienAm;
    
    @Column(name = "TiengViet", nullable = false)
    private String tiengViet;
    
    @Column(name = "LoaiTu")
    private String loaiTu;
    
    @Column(name = "ViDu", columnDefinition = "NVARCHAR(MAX)")
    private String viDu;
    
    @Column(name = "HinhAnh")
    private String hinhAnh;
    
    @Column(name = "AudioURL")
    private String audioURL;
    
    @Column(name = "GhiChu")
    private String ghiChu;
    
    @ManyToOne
    @JoinColumn(name = "CapDoHSK_ID")
    private CapDoHSK capDoHSK;
}
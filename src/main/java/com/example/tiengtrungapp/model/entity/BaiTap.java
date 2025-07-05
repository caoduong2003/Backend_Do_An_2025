package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BaiTap")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TieuDe", nullable = false)
    private String tieuDe;

    @Column(name = "MoTa")
    private String moTa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaiGiangId")
    private BaiGiang baiGiang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CapDoHSKId")
    private CapDoHSK capDoHSK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChuDeId")
    private ChuDe chuDe;

    @Column(name = "ThoiGianLam")
    private Integer thoiGianLam;

    @Column(name = "SoCauHoi")
    private Integer soCauHoi;

    @Column(name = "DiemToiDa")
    private Float diemToiDa;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "baiTap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CauHoi> cauHoiList;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
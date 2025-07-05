package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KetQuaBaiTap")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaBaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaiTapId", nullable = false)
    private BaiTap baiTap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HocVienId", nullable = false)
    private NguoiDung hocVien;

    @Column(name = "DiemSo")
    private Float diemSo;

    @Column(name = "ThoiGianLam")
    private Integer thoiGianLam;

    @Column(name = "SoCauDung")
    private Integer soCauDung;

    @Column(name = "TongSoCau")
    private Integer tongSoCau;

    @Column(name = "NgayLamBai")
    private LocalDateTime ngayLamBai;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @OneToMany(mappedBy = "ketQuaBaiTap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietKetQua> chiTietKetQuaList;

    @PrePersist
    protected void onCreate() {
        ngayLamBai = LocalDateTime.now();
    }
}
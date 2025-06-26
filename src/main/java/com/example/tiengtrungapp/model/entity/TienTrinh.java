package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TienTrinh")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TienTrinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "HocVienID", nullable = false)
    private NguoiDung hocVien;
    
    @ManyToOne
    @JoinColumn(name = "BaiGiangID", nullable = false)
    private BaiGiang baiGiang;
    
    @Column(name = "TrangThai", nullable = false)
    private Integer trangThai; // 0: Chưa học, 1: Đang học, 2: Đã hoàn thành
    
    @Column(name = "TienDo")
    private Integer tienDo; // Phần trăm hoàn thành (0-100)
    
    @Column(name = "ThoiGianHoc")
    private Integer thoiGianHoc; // Thời gian học tính bằng giây
    
    @Column(name = "DiemSo")
    private Integer diemSo; // Điểm số bài kiểm tra (0-100)
    
    @Column(name = "LanHoc")
    private Integer lanHoc; // Số lần học bài này
    
    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;
    
    @Column(name = "NgayHoanThanh")
    private LocalDateTime ngayHoanThanh;
    
    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;
    
    @Column(name = "GhiChu")
    private String ghiChu;
    
    @PrePersist
    protected void onCreate() {
        ngayCapNhat = LocalDateTime.now();
        if (ngayBatDau == null) {
            ngayBatDau = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
} 
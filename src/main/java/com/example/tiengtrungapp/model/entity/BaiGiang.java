package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BaiGiang")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaiGiang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "MaBaiGiang", nullable = false)
    private String maBaiGiang;
    
    @Column(name = "TieuDe", nullable = false)
    private String tieuDe;
    
    @Column(name = "MoTa")
    private String moTa;
    
    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;
    
    @Column(name = "NgayTao", nullable = false)
    private LocalDateTime ngayTao;
    
    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;
    
    @Column(name = "GiangVienID", nullable = false)
    private Long giangVienID;
    
    @ManyToOne
    @JoinColumn(name = "LoaiBaiGiangID", nullable = false)
    private LoaiBaiGiang loaiBaiGiang;
    
    @ManyToOne
    @JoinColumn(name = "CapDoHSK_ID", nullable = false)
    private CapDoHSK capDoHSK;
    
    @ManyToOne
    @JoinColumn(name = "ChuDeID", nullable = false)
    private ChuDe chuDe;
    
    @Column(name = "LuotXem", nullable = false)
    private Integer luotXem;
    
    @Column(name = "ThoiLuong")
    private Integer thoiLuong;
    
    @Column(name = "HinhAnh")
    private String hinhAnh;
    
    @Column(name = "VideoURL")
    private String videoURL;
    
    @Column(name = "AudioURL")
    private String audioURL;
    
    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai;
    
    @Column(name = "LaBaiGiangGoi", nullable = false)
    private Boolean laBaiGiangGoi;
    
    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
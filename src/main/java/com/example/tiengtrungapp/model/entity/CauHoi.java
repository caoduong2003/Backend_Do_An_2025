package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CauHoi")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaiTapId", nullable = false)
    private BaiTap baiTap;

    @Column(name = "NoiDung", nullable = false)
    private String noiDung;

    @Column(name = "LoaiCauHoi")
    private Integer loaiCauHoi;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    @Column(name = "AudioURL")
    private String audioURL;

    @Column(name = "ThuTu")
    private Integer thuTu;

    @Column(name = "DiemSo")
    private Float diemSo;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @OneToMany(mappedBy = "cauHoi", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("thuTu ASC")
    private List<DapAn> dapAnList;
}
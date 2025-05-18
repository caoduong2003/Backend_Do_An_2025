package com.example.tiengtrungapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TenDangNhap", nullable = false)
    private String tenDangNhap;

    @Column(name = "Email")
    private String email;

    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "VaiTro", nullable = false)
    private Integer vaiTro; // 0: Admin, 1: GiangVien, 2: HocVien

    @Column(name = "TrinhDoHSK")
    private Integer trinhDoHSK;

    @Column(name = "HinhDaiDien")
    private String hinhDaiDien;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "LanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "TrangThai")
    private Boolean trangThai;
}
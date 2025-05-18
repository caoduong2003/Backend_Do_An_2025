package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DangKyRequest {
        private String tenDangNhap;
        private String email;
        private String matKhau;
        private String hoTen;
        private String soDienThoai;
        private Integer vaiTro;
        private Integer trinhDoHSK;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DangNhapRequest {
        private String tenDangNhap;
        private String matKhau;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String tenDangNhap;
        private String email;
        private Integer vaiTro;
        private String hoTen;
    }
}
package com.example.tiengtrungapp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUserRequest {
        @NotBlank(message = "Tên đăng nhập không được để trống")
        @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4-50 ký tự")
        private String tenDangNhap;

        @Email(message = "Email không hợp lệ")
        private String email;

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
        private String matKhau;

        @NotBlank(message = "Họ tên không được để trống")
        @Size(max = 100, message = "Họ tên không được quá 100 ký tự")
        private String hoTen;

        @Size(max = 20, message = "Số điện thoại không được quá 20 ký tự")
        private String soDienThoai;

        @NotNull(message = "Vai trò không được để trống")
        private Integer vaiTro; // 0: Admin, 1: Giảng viên, 2: Học viên

        private Integer trinhDoHSK;

        private String hinhDaiDien;

        @Builder.Default
        private Boolean trangThai = true;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserRequest {
        @Email(message = "Email không hợp lệ")
        private String email;

        @Size(max = 100, message = "Họ tên không được quá 100 ký tự")
        private String hoTen;

        @Size(max = 20, message = "Số điện thoại không được quá 20 ký tự")
        private String soDienThoai;

        private Integer vaiTro;

        private Integer trinhDoHSK;

        private String hinhDaiDien;

        private Boolean trangThai;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String tenDangNhap;
        private String email;
        private String hoTen;
        private String soDienThoai;
        private Integer vaiTro;
        private String vaiTroText;
        private Integer trinhDoHSK;
        private String hinhDaiDien;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ngayTao;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ngayCapNhat;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lanDangNhapCuoi;
        
        private Boolean trangThai;
        private String trangThaiText;
        
        // Thông tin thống kê bổ sung
        private Integer soBaiGiangDaTao; // Cho giáo viên
        private Integer soBaiGiangDaHoc;  // Cho học viên
        private Integer tienDoHocTap;     // Cho học viên (%)
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatistics {
        private Long tongSoNguoiDung;
        private Long soAdmin;
        private Long soGiangVien;
        private Long soHocVien;
        private Long soNguoiDungHoatDong;
        private Long soNguoiDungBiKhoa;
        private Long soNguoiDungMoi7Ngay;    // Đăng ký trong 7 ngày qua
        private Long soNguoiDungMoi30Ngay;   // Đăng ký trong 30 ngày qua
        private Long soNguoiDungDangNhapHomNay;
        private Long soNguoiDungDangNhap7Ngay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank(message = "Mật khẩu cũ không được để trống")
        private String matKhauCu;

        @NotBlank(message = "Mật khẩu mới không được để trống")
        @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự")
        private String matKhauMoi;

        @NotBlank(message = "Xác nhận mật khẩu không được để trống")
        private String xacNhanMatKhau;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        @Email(message = "Email không hợp lệ")
        private String email;

        @Size(max = 100, message = "Họ tên không được quá 100 ký tự")
        private String hoTen;

        @Size(max = 20, message = "Số điện thoại không được quá 20 ký tự")
        private String soDienThoai;

        private Integer trinhDoHSK;

        private String hinhDaiDien;
    }

    /**
     * Chuyển đổi vai trò từ số sang text
     */
    public static String getVaiTroText(Integer vaiTro) {
        if (vaiTro == null) return "Không xác định";
        switch (vaiTro) {
            case 0: return "Quản trị viên";
            case 1: return "Giảng viên";
            case 2: return "Học viên";
            default: return "Không xác định";
        }
    }

    /**
     * Chuyển đổi trạng thái từ boolean sang text
     */
    public static String getTrangThaiText(Boolean trangThai) {
        if (trangThai == null) return "Không xác định";
        return trangThai ? "Hoạt động" : "Bị khóa";
    }
}
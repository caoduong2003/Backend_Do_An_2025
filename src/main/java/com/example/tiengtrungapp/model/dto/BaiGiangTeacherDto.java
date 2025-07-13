package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO classes specific for Teacher CRUD operations
 * This file is separate from existing DTOs to avoid conflicts
 */
public class BaiGiangTeacherDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherBaiGiangResponse {
        private Long id;
        private String maBaiGiang;
        private String tieuDe;
        private String moTa;
        private LocalDateTime ngayTao;
        private LocalDateTime ngayCapNhat;
        private Integer luotXem;
        private Integer thoiLuong;
        private String hinhAnh;
        private String videoURL;
        private String audioURL;
        private Boolean trangThai;
        private Boolean laBaiGiangGoi;

        // Nested info objects
        private LoaiBaiGiangInfo loaiBaiGiang;
        private CapDoHSKInfo capDoHSK;
        private ChuDeInfo chuDe;
        private GiangVienInfo giangVien;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherBaiGiangDetailResponse {
        private Long id;
        private String maBaiGiang;
        private String tieuDe;
        private String moTa;
        private String noiDung;
        private LocalDateTime ngayTao;
        private LocalDateTime ngayCapNhat;
        private Integer luotXem;
        private Integer thoiLuong;
        private String hinhAnh;
        private String videoURL;
        private String audioURL;
        private Boolean trangThai;
        private Boolean laBaiGiangGoi;

        // Nested info objects
        private LoaiBaiGiangInfo loaiBaiGiang;
        private CapDoHSKInfo capDoHSK;
        private ChuDeInfo chuDe;
        private GiangVienInfo giangVien;

        // Additional stats
        private Integer soBaiTap;
        private Integer soHocVienDangHoc;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTeacherBaiGiangRequest {
        @NotBlank(message = "Tiêu đề không được để trống")
        @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
        private String tieuDe;

        @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
        private String moTa;

        private String noiDung;

        @NotNull(message = "Loại bài giảng không được để trống")
        private Integer loaiBaiGiangId;

        @NotNull(message = "Cấp độ HSK không được để trống")
        private Integer capDoHSKId;

        @NotNull(message = "Chủ đề không được để trống")
        private Integer chuDeId;

        @Min(value = 0, message = "Thời lượng phải lớn hơn hoặc bằng 0")
        private Integer thoiLuong;

        private String hinhAnh;
        private String videoURL;
        private String audioURL;

        @Builder.Default
        private Boolean trangThai = false;

        @Builder.Default
        private Boolean laBaiGiangGoi = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTeacherBaiGiangRequest {
        @NotBlank(message = "Tiêu đề không được để trống")
        @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
        private String tieuDe;

        @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
        private String moTa;

        private String noiDung;

        @NotNull(message = "Loại bài giảng không được để trống")
        private Integer loaiBaiGiangId;

        @NotNull(message = "Cấp độ HSK không được để trống")
        private Integer capDoHSKId;

        @NotNull(message = "Chủ đề không được để trống")
        private Integer chuDeId;

        @Min(value = 0, message = "Thời lượng phải lớn hơn hoặc bằng 0")
        private Integer thoiLuong;

        private String hinhAnh;
        private String videoURL;
        private String audioURL;
        private Boolean trangThai;
        private Boolean laBaiGiangGoi;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherBaiGiangStatsResponse {
        private Long tongSoBaiGiang;
        private Long soBaiGiangCongKhai;
        private Long soBaiGiangBanNhap;
        private Long tongLuotXem;
        private Double thoiLuongTrungBinh;
        private String baiGiangPhoBienNhat;
        private Long luotXemCaoNhat;
        private LocalDateTime lanCapNhatCuoi;

        // Stats by HSK level
        private Long soBaiGiangHSK1;
        private Long soBaiGiangHSK2;
        private Long soBaiGiangHSK3;
        private Long soBaiGiangHSK4;
        private Long soBaiGiangHSK5;
        private Long soBaiGiangHSK6;
    }

    // Nested info classes
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoaiBaiGiangInfo {
        private Integer id;
        private String tenLoai;
        private String moTa;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CapDoHSKInfo {
        private Integer id;
        private String tenCapDo;
        private String moTa;
        private Integer capDo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChuDeInfo {
        private Integer id;
        private String tenChuDe;
        private String moTa;
        private String hinhAnh;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GiangVienInfo {
        private Long id;
        private String hoTen;
        private String email;
        private String hinhDaiDien;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponse<T> {
        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
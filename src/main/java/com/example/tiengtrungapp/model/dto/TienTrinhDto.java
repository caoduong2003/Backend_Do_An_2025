package com.example.tiengtrungapp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TienTrinhDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTienTrinhRequest {
        @NotNull(message = "ID bài giảng không được để trống")
        private Long baiGiangId;
        
        @NotNull(message = "Trạng thái không được để trống")
        @Min(value = 0, message = "Trạng thái phải từ 0-2")
        @Max(value = 2, message = "Trạng thái phải từ 0-2")
        private Integer trangThai; // 0: Chưa học, 1: Đang học, 2: Đã hoàn thành
        
        @Min(value = 0, message = "Tiến độ phải từ 0-100")
        @Max(value = 100, message = "Tiến độ phải từ 0-100")
        private Integer tienDo;
        
        @Min(value = 0, message = "Thời gian học không được âm")
        private Integer thoiGianHoc;
        
        @Min(value = 0, message = "Điểm số phải từ 0-100")
        @Max(value = 100, message = "Điểm số phải từ 0-100")
        private Integer diemSo;
        
        private String ghiChu;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTienTrinhRequest {
        @Min(value = 0, message = "Trạng thái phải từ 0-2")
        @Max(value = 2, message = "Trạng thái phải từ 0-2")
        private Integer trangThai;
        
        @Min(value = 0, message = "Tiến độ phải từ 0-100")
        @Max(value = 100, message = "Tiến độ phải từ 0-100")
        private Integer tienDo;
        
        @Min(value = 0, message = "Thời gian học không được âm")
        private Integer thoiGianHoc;
        
        @Min(value = 0, message = "Điểm số phải từ 0-100")
        @Max(value = 100, message = "Điểm số phải từ 0-100")
        private Integer diemSo;
        
        private String ghiChu;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TienTrinhResponse {
        private Long id;
        private Long hocVienId;
        private String hocVienTen;
        private Long baiGiangId;
        private String baiGiangTieuDe;
        private String baiGiangMaBaiGiang;
        private Integer trangThai;
        private String trangThaiText;
        private Integer tienDo;
        private Integer thoiGianHoc;
        private Integer diemSo;
        private Integer lanHoc;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ngayBatDau;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ngayHoanThanh;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ngayCapNhat;
        
        private String ghiChu;
        
        // Thông tin bổ sung từ bài giảng
        private String baiGiangHinhAnh;
        private String baiGiangVideoURL;
        private Integer baiGiangThoiLuong;
        private String chuDeTen;
        private String capDoHSKTen;
        private String loaiBaiGiangTen;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TienTrinhStatistics {
        private Long tongSoBaiGiang;
        private Long soBaiGiangDaHoc;
        private Long soBaiGiangDangHoc;
        private Long soBaiGiangHoanThanh;
        private Integer tienDoTongThe; // Phần trăm hoàn thành tổng thể
        private Integer tongThoiGianHoc; // Tổng thời gian học (giây)
        private Double diemTrungBinh;
        private Integer soNgayHocLienTiep;
        private Integer soBaiGiangHomNay;
        private Integer soBaiGiangTuanNay;
        private Integer soBaiGiangThangNay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TienTrinhFilter {
        private Integer trangThai;
        private Integer capDoHSKId;
        private Integer chuDeId;
        private Integer loaiBaiGiangId;
        private LocalDateTime tuNgay;
        private LocalDateTime denNgay;
        private String keyword;
    }

    /**
     * Chuyển đổi trạng thái từ số sang text
     */
    public static String getTrangThaiText(Integer trangThai) {
        if (trangThai == null) return "Không xác định";
        
        switch (trangThai) {
            case 0: return "Chưa học";
            case 1: return "Đang học";
            case 2: return "Đã hoàn thành";
            default: return "Không xác định";
        }
    }
} 
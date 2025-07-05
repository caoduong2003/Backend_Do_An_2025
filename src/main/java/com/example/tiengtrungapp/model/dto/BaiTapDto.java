package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BaiTapDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaiTapResponse {
        private Long id;
        private String tieuDe;
        private String moTa;
        private Long baiGiangId;
        private String baiGiangTieuDe;
        private Integer capDoHSKId;
        private String capDoHSKTen;
        private Integer chuDeId;
        private String chuDeTen;
        private Integer thoiGianLam;
        private Integer soCauHoi;
        private Float diemToiDa;
        private Boolean trangThai;
        private LocalDateTime ngayTao;
        private LocalDateTime ngayCapNhat;
        private Float diemCaoNhat; // Điểm cao nhất của học viên
        private Integer soLanLam; // Số lần học viên đã làm
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaiTapDetailResponse {
        private Long id;
        private String tieuDe;
        private String moTa;
        private Integer thoiGianLam;
        private Integer soCauHoi;
        private Float diemToiDa;
        private List<CauHoiDto.CauHoiResponse> cauHoiList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LamBaiTapRequest {
        private Long baiTapId;
        private Integer thoiGianLam; // thời gian thực tế làm bài (giây)
        private List<TraLoiRequest> danhSachTraLoi;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TraLoiRequest {
        private Long cauHoiId;
        private Long dapAnId;
    }
}
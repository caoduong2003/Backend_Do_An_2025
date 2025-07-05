package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class KetQuaBaiTapDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KetQuaBaiTapResponse {
        private Long id;
        private Long baiTapId;
        private String baiTapTieuDe;
        private Float diemSo;
        private Float diemToiDa;
        private Integer thoiGianLam;
        private Integer soCauDung;
        private Integer tongSoCau;
        private LocalDateTime ngayLamBai;
        private Integer trangThai;
        private Float tiLeDung; // Tỉ lệ % câu trả lời đúng
        private String xepLoai; // Xuất sắc, Giỏi, Khá, Trung bình, Yếu
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KetQuaDetailResponse {
        private Long id;
        private Long baiTapId;
        private String baiTapTieuDe;
        private Float diemSo;
        private Float diemToiDa;
        private Integer thoiGianLam;
        private Integer soCauDung;
        private Integer tongSoCau;
        private LocalDateTime ngayLamBai;
        private Float tiLeDung;
        private String xepLoai;
        private List<ChiTietKetQuaDto.ChiTietResponse> chiTietList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThongKeResponse {
        private Long tongSoBaiTap;
        private Long soLanLamBai;
        private Float diemTrungBinh;
        private Float diemCaoNhat;
        private Long soCauDungTong;
        private Long tongSoCauHoi;
        private Float tiLeDungTong;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaiTapThongKeResponse {
        private Long baiTapId;
        private String baiTapTieuDe;
        private Integer soLanLam;
        private Float diemCaoNhat;
        private Float diemTrungBinh;
        private LocalDateTime lanLamGanNhat;
    }
}
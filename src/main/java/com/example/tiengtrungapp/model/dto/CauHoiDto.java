package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CauHoiDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CauHoiResponse {
        private Long id;
        private String noiDung;
        private Integer loaiCauHoi;
        private String hinhAnh;
        private String audioURL;
        private Integer thuTu;
        private Float diemSo;
        private List<DapAnDto.DapAnResponse> dapAnList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CauHoiRequest {
        private String noiDung;
        private Integer loaiCauHoi;
        private String hinhAnh;
        private String audioURL;
        private Integer thuTu;
        private Float diemSo;
        private List<DapAnDto.DapAnRequest> dapAnList;
    }
}
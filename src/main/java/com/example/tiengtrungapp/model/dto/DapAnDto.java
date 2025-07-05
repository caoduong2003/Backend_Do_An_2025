package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DapAnDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DapAnResponse {
        private Long id;
        private String noiDung;
        private Integer thuTu;
        // Không trả về thông tin đáp án đúng để tránh gian lận
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DapAnRequest {
        private String noiDung;
        private Boolean laDapAnDung;
        private Integer thuTu;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DapAnDetailResponse {
        private Long id;
        private String noiDung;
        private Boolean laDapAnDung; // Chỉ dùng khi xem kết quả
        private Integer thuTu;
    }
}
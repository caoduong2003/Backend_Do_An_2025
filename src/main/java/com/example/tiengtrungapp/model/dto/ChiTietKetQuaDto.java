package com.example.tiengtrungapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChiTietKetQuaDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietResponse {
        private Long id;
        private Long cauHoiId;
        private String cauHoiNoiDung;
        private Long dapAnDaChonId;
        private String dapAnDaChonNoiDung;
        private Long dapAnDungId;
        private String dapAnDungNoiDung;
        private Boolean laDung;
        private Float diemSo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietRequest {
        private Long cauHoiId;
        private Long dapAnDaChonId;
        private Boolean laDung;
    }
}
package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.BaiGiangTeacherDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Teacher BaiGiang operations
 * This is separate from existing services to avoid conflicts
 */
public interface TeacherBaiGiangService {

        /**
         * Get paginated list of teacher's lectures
         */
        BaiGiangTeacherDto.PageResponse<BaiGiangTeacherDto.TeacherBaiGiangResponse> getTeacherBaiGiangs(
                        Long teacherId,
                        String search,
                        Integer capDoHSKId,
                        Integer chuDeId,
                        Integer loaiBaiGiangId,
                        Boolean trangThai,
                        Pageable pageable);

        /**
         * Get lecture detail by ID (with ownership validation)
         */
        BaiGiangTeacherDto.TeacherBaiGiangDetailResponse getTeacherBaiGiangById(Long teacherId, Long baiGiangId);

        /**
         * Create new lecture
         */
        BaiGiangTeacherDto.TeacherBaiGiangResponse createTeacherBaiGiang(
                        Long teacherId,
                        BaiGiangTeacherDto.CreateTeacherBaiGiangRequest request);

        /**
         * Update existing lecture
         */
        BaiGiangTeacherDto.TeacherBaiGiangResponse updateTeacherBaiGiang(
                        Long teacherId,
                        Long baiGiangId,
                        BaiGiangTeacherDto.UpdateTeacherBaiGiangRequest request);

        /**
         * Delete lecture (soft delete)
         */
        void deleteTeacherBaiGiang(Long teacherId, Long baiGiangId);

        /**
         * Toggle lecture status (published/draft)
         */
        BaiGiangTeacherDto.TeacherBaiGiangResponse toggleBaiGiangStatus(Long teacherId, Long baiGiangId);

        /**
         * Duplicate lecture
         */
        BaiGiangTeacherDto.TeacherBaiGiangResponse duplicateTeacherBaiGiang(
                        Long teacherId,
                        Long baiGiangId,
                        String newTitle);

        /**
         * Get teacher's lecture statistics
         */
        BaiGiangTeacherDto.TeacherBaiGiangStatsResponse getTeacherBaiGiangStats(Long teacherId);

        /**
         * Search teacher's lectures
         */
        List<BaiGiangTeacherDto.TeacherBaiGiangResponse> searchTeacherBaiGiangs(
                        Long teacherId,
                        String keyword,
                        int limit);

        /**
         * Increment view count
         */
        Integer incrementBaiGiangViews(Long teacherId, Long baiGiangId);

        /**
         * Validate teacher ownership
         */
        boolean validateTeacherOwnership(Long teacherId, Long baiGiangId);

        /**
         * Get quick stats for dashboard
         */
        Map<String, Object> getQuickStats(Long teacherId);
}
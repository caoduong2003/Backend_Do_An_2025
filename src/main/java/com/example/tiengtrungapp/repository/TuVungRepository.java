package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.TuVung;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuVungRepository extends JpaRepository<TuVung, Long> {

        // ===== SỬA LỖI: Sử dụng relationship object =====

        /**
         * Tìm từ vựng theo bài giảng - SỬA: sử dụng t.baiGiang.id
         */
        @Query("SELECT t FROM TuVung t WHERE t.baiGiang.id = :baiGiangId")
        List<TuVung> findByBaiGiangId(@Param("baiGiangId") Long baiGiangId);

        /**
         * Tìm từ vựng theo bài giảng với ordering - SỬA: sử dụng t.baiGiang.id
         */
        @Query("SELECT t FROM TuVung t WHERE t.baiGiang.id = :baiGiangId ORDER BY t.id ASC")
        List<TuVung> findByBaiGiangIdOrderById(@Param("baiGiangId") Long baiGiangId);

        /**
         * Tìm từ vựng theo bài giảng với pagination
         */
        @Query("SELECT t FROM TuVung t WHERE t.baiGiang.id = :baiGiangId ORDER BY t.id ASC")
        List<TuVung> findByBaiGiangId(@Param("baiGiangId") Long baiGiangId, Pageable pageable);

        // ===== UTILITY METHODS =====

        /**
         * Tìm theo tiếng Việt
         */
        List<TuVung> findByTiengVietContainingIgnoreCase(String keyword);

        /**
         * Tìm theo tiếng Trung
         */
        List<TuVung> findByTiengTrungContainingIgnoreCase(String keyword);

        /**
         * Tìm theo phiên âm
         */
        List<TuVung> findByPhienAmContainingIgnoreCase(String keyword);

        /**
         * Đếm số từ vựng theo bài giảng
         */
        @Query("SELECT COUNT(t) FROM TuVung t WHERE t.baiGiang.id = :baiGiangId")
        Long countByBaiGiangId(@Param("baiGiangId") Long baiGiangId);

        /**
         * Tìm từ vựng cơ bản nhất của bài giảng (guest mode)
         */
        @Query("SELECT t FROM TuVung t WHERE t.baiGiang.id = :baiGiangId ORDER BY t.id ASC")
        List<TuVung> findBasicVocabulary(@Param("baiGiangId") Long baiGiangId, Pageable pageable);

        /**
         * Tìm kiếm từ vựng cho guest
         */
        @Query("SELECT t FROM TuVung t WHERE " +
                        "(LOWER(t.tiengTrung) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.tiengViet) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.phienAm) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "ORDER BY t.id ASC")
        List<TuVung> searchForGuest(@Param("keyword") String keyword, Pageable pageable);

        /**
         * Tìm kiếm từ vựng (tất cả)
         */
        @Query("SELECT t FROM TuVung t WHERE " +
                        "LOWER(t.tiengTrung) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.tiengViet) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.phienAm) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.loaiTu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<TuVung> searchAll(@Param("keyword") String keyword);

        /**
         * Tìm từ vựng theo cấp độ HSK
         */
        @Query("SELECT t FROM TuVung t WHERE t.capDoHSK.id = :capDoHSKId")
        List<TuVung> findByCapDoHSKId(@Param("capDoHSKId") Integer capDoHSKId);

        /**
         * Tìm từ vựng theo loại từ
         */
        List<TuVung> findByLoaiTuContainingIgnoreCase(String loaiTu);

        /**
         * Tìm theo tiếng Việt (case sensitive)
         */
        List<TuVung> findByTiengVietContaining(String keyword);

        /**
         * Tìm theo tiếng Trung (case sensitive)
         */
        List<TuVung> findByTiengTrungContaining(String keyword);

        /**
         * Đếm tổng số từ vựng - REQUIRED for guest stats
         */
        @Query("SELECT COUNT(t) FROM TuVung t")
        Long countAllVocabulary();
}
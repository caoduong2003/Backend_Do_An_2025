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

    // Existing methods - GIỮ NGUYÊN
    List<TuVung> findByBaiGiangId(Long baiGiangId);

    @Query("SELECT t FROM TuVung t WHERE t.baiGiangId = :baiGiangId ORDER BY t.ID ASC")
    List<TuVung> findByBaiGiangIdOrderById(@Param("baiGiangId") Long baiGiangId);

    // 🚀 MISSING: Methods cho TuVungController
    List<TuVung> findByTiengVietContaining(String keyword);

    List<TuVung> findByTiengTrungContaining(String keyword);

    // 🚀 NEW: Methods for Guest Mode

    /**
     * Lấy từ vựng theo bài giảng với pagination (cho guest)
     */
    @Query("SELECT t FROM TuVung t WHERE t.baiGiangId = :baiGiangId ORDER BY t.ID ASC")
    List<TuVung> findByBaiGiangId(@Param("baiGiangId") Long baiGiangId, Pageable pageable);

    /**
     * Lấy từ vựng cơ bản nhất của bài giảng (guest mode)
     */
    @Query("SELECT t FROM TuVung t WHERE t.baiGiangId = :baiGiangId ORDER BY t.ID ASC")
    List<TuVung> findBasicVocabulary(@Param("baiGiangId") Long baiGiangId, Pageable pageable);

    /**
     * Đếm số từ vựng theo bài giảng
     */
    @Query("SELECT COUNT(t) FROM TuVung t WHERE t.baiGiangId = :baiGiangId")
    Long countByBaiGiangId(@Param("baiGiangId") Long baiGiangId);

    /**
     * Tìm kiếm từ vựng cho guest
     */
    @Query("SELECT t FROM TuVung t WHERE " +
            "(LOWER(t.tiengTrung) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.tiengViet) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.phienAm) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY t.ID ASC")
    List<TuVung> searchForGuest(@Param("keyword") String keyword, Pageable pageable);
}
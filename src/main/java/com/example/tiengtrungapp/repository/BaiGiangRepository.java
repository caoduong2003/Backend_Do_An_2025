package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Long>, JpaSpecificationExecutor<BaiGiang> {

        // ===== EXISTING METHODS - KEEP AS IS =====
        List<BaiGiang> findByGiangVienID(Long giangVienId);

        // ===== SỬA LỖI: Sử dụng relationship objects =====

        /**
         * Tìm theo loại bài giảng
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.loaiBaiGiang.id = :loaiBaiGiangId")
        List<BaiGiang> findByLoaiBaiGiangId(@Param("loaiBaiGiangId") Integer loaiBaiGiangId);

        /**
         * Tìm theo cấp độ HSK
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK.id = :capDoHSKId")
        List<BaiGiang> findByCapDoHSKId(@Param("capDoHSKId") Integer capDoHSKId);

        /**
         * Tìm theo chủ đề
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.chuDe.id = :chuDeId")
        List<BaiGiang> findByChuDeId(@Param("chuDeId") Integer chuDeId);

        // ===== METHODS CHO GUEST CONTROLLER =====

        /**
         * Tìm tất cả bài giảng đã publish
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.ngayTao DESC")
        List<BaiGiang> findAllPublished();

        /**
         * Tìm tất cả bài giảng đã publish với phân trang
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.ngayTao DESC")
        List<BaiGiang> findAllPublished(Pageable pageable);

        /**
         * Tìm theo cấp độ HSK với phân trang
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK.id = :capDoHSKId AND b.trangThai = true ORDER BY b.ngayTao DESC")
        List<BaiGiang> findByCapDoHSKId(@Param("capDoHSKId") Integer capDoHSKId, Pageable pageable);

        /**
         * Tìm theo chủ đề với phân trang
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.chuDe.id = :chuDeId AND b.trangThai = true ORDER BY b.ngayTao DESC")
        List<BaiGiang> findByChuDeId(@Param("chuDeId") Integer chuDeId, Pageable pageable);

        /**
         * Tìm bài giảng phổ biến nhất (guest mode)
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.luotXem DESC, b.ngayTao DESC")
        List<BaiGiang> findTopPublishedLessons(Pageable pageable);

        // ===== METHODS CHO PROFILE SERVICE =====

        /**
         * Đếm số bài giảng theo giảng viên - TRẢ VỀ INTEGER CHO UI
         */
        @Query("SELECT CAST(COUNT(b) AS int) FROM BaiGiang b WHERE b.giangVienID = :giangVienId")
        Integer countByGiangVienID(@Param("giangVienId") Long giangVienId);

        /**
         * Đếm số bài giảng theo giảng viên và trạng thái
         */
        @Query("SELECT CAST(COUNT(b) AS int) FROM BaiGiang b WHERE b.giangVienID = :giangVienId AND b.trangThai = :trangThai")
        Integer countByGiangVienIDAndTrangThai(@Param("giangVienId") Long giangVienId,
                        @Param("trangThai") Boolean trangThai);

        /**
         * Đếm số bài giảng theo trạng thái - CHO TIEN TRINH SERVICE
         */
        @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.trangThai = :trangThai")
        Long countByTrangThai(@Param("trangThai") Boolean trangThai);

        // ===== UTILITY METHODS =====

        /**
         * Tìm theo trạng thái
         */
        List<BaiGiang> findByTrangThai(Boolean trangThai);

        /**
         * Tìm theo tiêu đề
         */
        List<BaiGiang> findByTieuDeContainingIgnoreCase(String tieuDe);

        /**
         * Tìm bài giảng theo giảng viên và trạng thái
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.giangVienID = :giangVienId AND b.trangThai = :trangThai")
        List<BaiGiang> findByGiangVienIDAndTrangThai(@Param("giangVienId") Long giangVienId,
                        @Param("trangThai") Boolean trangThai);

        /**
         * Đếm số bài giảng đã publish
         */
        @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.trangThai = true")
        Long countPublished();

        /**
         * Tìm kiếm bài giảng
         */
        @Query("SELECT b FROM BaiGiang b WHERE " +
                        "LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(b.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(b.noiDung) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<BaiGiang> search(@Param("keyword") String keyword);

        /**
         * Tìm theo cấp độ HSK (string) - CHO BAIGIANG CONTROLLER
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK.capDo = :level")
        List<BaiGiang> findByCapDoHSKLevel(@Param("level") String level);

        /**
         * Tìm kiếm bài giảng cho guest
         */
        @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true AND " +
                        "(LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(b.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "ORDER BY b.ngayTao DESC")
        List<BaiGiang> searchForGuest(@Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT COUNT(b) > 0 FROM BaiGiang b WHERE b.id = :lectureId AND b.giangVienID = :teacherId")
        boolean existsByIdAndGiangVienID(@Param("lectureId") Long lectureId, @Param("teacherId") Long teacherId);

        @Query("SELECT b FROM BaiGiang b WHERE b.giangVienID = :teacherId " +
                        "AND b.ngayTao BETWEEN :startDate AND :endDate ORDER BY b.ngayTao DESC")
        List<BaiGiang> findByTeacherAndDateRange(
                        @Param("teacherId") Long teacherId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}
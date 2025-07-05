package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Long> {

    // Existing methods - GIỮ NGUYÊN
    List<BaiGiang> findByGiangVienID(Long giangVienId);

    List<BaiGiang> findByLoaiBaiGiangId(Integer loaiBaiGiangId);

    List<BaiGiang> findByCapDoHSKId(Integer capDoHSKId);

    List<BaiGiang> findByChuDeId(Integer chuDeId);

    // 🚀 MISSING: Methods cho BaiGiangController
    @Query("SELECT b FROM BaiGiang b WHERE " +
            "LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.noiDung) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BaiGiang> search(@Param("keyword") String keyword);

    @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK_ID = :level")
    List<BaiGiang> findByCapDoHSKLevel(@Param("level") String level);

    // 🚀 FIXED: Sử dụng TrangThai thay vì published
    @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.ngayTao DESC")
    List<BaiGiang> findAllPublished();

    /**
     * Lấy bài giảng đã active với pagination
     */
    @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.ngayTao DESC")
    List<BaiGiang> findAllPublished(Pageable pageable);

    /**
     * Lấy bài giảng theo cấp độ HSK với pagination
     */
    @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK_ID = :capDoHSKId AND b.trangThai = true ORDER BY b.ngayTao DESC")
    List<BaiGiang> findByCapDoHSKId(@Param("capDoHSKId") Integer capDoHSKId, Pageable pageable);

    /**
     * Lấy bài giảng theo chủ đề với pagination
     */
    @Query("SELECT b FROM BaiGiang b WHERE b.chuDeId = :chuDeId AND b.trangThai = true ORDER BY b.ngayTao DESC")
    List<BaiGiang> findByChuDeId(@Param("chuDeId") Integer chuDeId, Pageable pageable);

    /**
     * Lấy bài giảng phổ biến nhất (guest mode)
     */
    @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true ORDER BY b.luotXem DESC, b.ngayTao DESC")
    List<BaiGiang> findTopPublishedLessons(Pageable pageable);

    /**
     * Đếm số bài giảng đã active
     */
    @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.trangThai = true")
    Long countPublished();

    /**
     * Tìm kiếm bài giảng cho guest
     */
    @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true AND " +
            "(LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY b.ngayTao DESC")
    List<BaiGiang> searchForGuest(@Param("keyword") String keyword, Pageable pageable);
}
package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TienTrinhRepository extends JpaRepository<TienTrinh, Long>, JpaSpecificationExecutor<TienTrinh> {
    
    // Tìm tiến trình theo học viên
    List<TienTrinh> findByHocVienId(Long hocVienId);
    
    // Tìm tiến trình theo bài giảng
    List<TienTrinh> findByBaiGiangId(Long baiGiangId);
    
    // Tìm tiến trình theo học viên và bài giảng
    Optional<TienTrinh> findByHocVienIdAndBaiGiangId(Long hocVienId, Long baiGiangId);
    
    // Tìm tiến trình theo trạng thái
    List<TienTrinh> findByHocVienIdAndTrangThai(Long hocVienId, Integer trangThai);
    
    // Đếm số bài giảng đã hoàn thành của học viên
    long countByHocVienIdAndTrangThai(Long hocVienId, Integer trangThai);
    
    // Đếm tổng số bài giảng học viên đã học
    long countByHocVienId(Long hocVienId);
    
    // Tìm tiến trình theo khoảng thời gian
    List<TienTrinh> findByHocVienIdAndNgayBatDauBetween(Long hocVienId, LocalDateTime start, LocalDateTime end);
    
    // Tìm tiến trình hoàn thành trong khoảng thời gian
    List<TienTrinh> findByHocVienIdAndNgayHoanThanhBetween(Long hocVienId, LocalDateTime start, LocalDateTime end);
    
    // Tính tổng thời gian học của học viên
    @Query("SELECT COALESCE(SUM(t.thoiGianHoc), 0) FROM TienTrinh t WHERE t.hocVien.id = :hocVienId")
    Integer getTotalStudyTimeByHocVienId(@Param("hocVienId") Long hocVienId);
    
    // Tính điểm trung bình của học viên
    @Query("SELECT COALESCE(AVG(t.diemSo), 0) FROM TienTrinh t WHERE t.hocVien.id = :hocVienId AND t.diemSo IS NOT NULL")
    Double getAverageScoreByHocVienId(@Param("hocVienId") Long hocVienId);
    
    // Tìm tiến trình theo cấp độ HSK
    @Query("SELECT t FROM TienTrinh t WHERE t.hocVien.id = :hocVienId AND t.baiGiang.capDoHSK.id = :capDoHSKId")
    List<TienTrinh> findByHocVienIdAndCapDoHSKId(@Param("hocVienId") Long hocVienId, @Param("capDoHSKId") Integer capDoHSKId);
    
    // Tìm tiến trình theo chủ đề
    @Query("SELECT t FROM TienTrinh t WHERE t.hocVien.id = :hocVienId AND t.baiGiang.chuDe.id = :chuDeId")
    List<TienTrinh> findByHocVienIdAndChuDeId(@Param("hocVienId") Long hocVienId, @Param("chuDeId") Integer chuDeId);
    
    // Tìm tiến trình theo loại bài giảng
    @Query("SELECT t FROM TienTrinh t WHERE t.hocVien.id = :hocVienId AND t.baiGiang.loaiBaiGiang.id = :loaiBaiGiangId")
    List<TienTrinh> findByHocVienIdAndLoaiBaiGiangId(@Param("hocVienId") Long hocVienId, @Param("loaiBaiGiangId") Integer loaiBaiGiangId);
    
    // Tìm tiến trình gần đây nhất
    @Query("SELECT t FROM TienTrinh t WHERE t.hocVien.id = :hocVienId ORDER BY t.ngayCapNhat DESC")
    List<TienTrinh> findRecentProgressByHocVienId(@Param("hocVienId") Long hocVienId);
    
    // Tìm tiến trình có điểm cao nhất
    @Query("SELECT t FROM TienTrinh t WHERE t.hocVien.id = :hocVienId AND t.diemSo IS NOT NULL ORDER BY t.diemSo DESC")
    List<TienTrinh> findTopScoresByHocVienId(@Param("hocVienId") Long hocVienId);
    
    // Thống kê tiến trình theo trạng thái
    @Query("SELECT t.trangThai, COUNT(t) FROM TienTrinh t WHERE t.hocVien.id = :hocVienId GROUP BY t.trangThai")
    List<Object[]> getProgressStatisticsByHocVienId(@Param("hocVienId") Long hocVienId);
} 
package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.KetQuaBaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KetQuaBaiTapRepository extends JpaRepository<KetQuaBaiTap, Long> {

    // Tìm kết quả theo học viên
    List<KetQuaBaiTap> findByHocVienIdOrderByNgayLamBaiDesc(Long hocVienId);

    // Tìm kết quả theo học viên và bài tập
    List<KetQuaBaiTap> findByHocVienIdAndBaiTapIdOrderByNgayLamBaiDesc(Long hocVienId, Long baiTapId);

    // Tìm kết quả tốt nhất của học viên cho bài tập
    Optional<KetQuaBaiTap> findTopByHocVienIdAndBaiTapIdOrderByDiemSoDesc(Long hocVienId, Long baiTapId);

    // Tìm kết quả với thông tin đầy đủ
    @Query("SELECT k FROM KetQuaBaiTap k " +
            "LEFT JOIN FETCH k.baiTap " +
            "LEFT JOIN FETCH k.hocVien " +
            "WHERE k.hocVien.id = :hocVienId " +
            "ORDER BY k.ngayLamBai DESC")
    List<KetQuaBaiTap> findByHocVienIdWithDetails(@Param("hocVienId") Long hocVienId);

    // Tính điểm trung bình của học viên
    @Query("SELECT COALESCE(AVG(k.diemSo), 0) FROM KetQuaBaiTap k WHERE k.hocVien.id = :hocVienId")
    Double getAverageScoreByHocVienId(@Param("hocVienId") Long hocVienId);

    // Đếm số bài tập đã làm
    long countByHocVienId(Long hocVienId);

    // Tìm điểm cao nhất của học viên
    @Query("SELECT COALESCE(MAX(k.diemSo), 0) FROM KetQuaBaiTap k WHERE k.hocVien.id = :hocVienId")
    Float getMaxScoreByHocVienId(@Param("hocVienId") Long hocVienId);
}
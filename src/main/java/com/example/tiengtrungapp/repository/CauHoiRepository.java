package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Long> {

    // Tìm câu hỏi theo bài tập
    List<CauHoi> findByBaiTapIdAndTrangThaiTrueOrderByThuTuAsc(Long baiTapId);

    // Tìm câu hỏi với đáp án
    @Query("SELECT c FROM CauHoi c " +
            "LEFT JOIN FETCH c.dapAnList " +
            "WHERE c.baiTap.id = :baiTapId AND c.trangThai = true " +
            "ORDER BY c.thuTu ASC")
    List<CauHoi> findByBaiTapIdWithDapAn(@Param("baiTapId") Long baiTapId);

    // Đếm số câu hỏi của bài tập
    long countByBaiTapIdAndTrangThaiTrue(Long baiTapId);
}
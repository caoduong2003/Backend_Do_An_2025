package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.ChiTietKetQua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietKetQuaRepository extends JpaRepository<ChiTietKetQua, Long> {

    // Tìm chi tiết kết quả theo kết quả bài tập
    List<ChiTietKetQua> findByKetQuaBaiTapId(Long ketQuaBaiTapId);

    // Tìm chi tiết kết quả với thông tin đầy đủ
    @Query("SELECT c FROM ChiTietKetQua c " +
            "LEFT JOIN FETCH c.cauHoi " +
            "LEFT JOIN FETCH c.dapAnDaChon " +
            "WHERE c.ketQuaBaiTap.id = :ketQuaBaiTapId")
    List<ChiTietKetQua> findByKetQuaBaiTapIdWithDetails(@Param("ketQuaBaiTapId") Long ketQuaBaiTapId);

    // Đếm số câu đúng trong một kết quả
    long countByKetQuaBaiTapIdAndLaDungTrue(Long ketQuaBaiTapId);
}
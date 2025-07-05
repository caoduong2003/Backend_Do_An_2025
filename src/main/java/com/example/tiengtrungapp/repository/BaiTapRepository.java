package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaiTapRepository extends JpaRepository<BaiTap, Long> {

    // Tìm bài tập theo trạng thái
    List<BaiTap> findByTrangThaiTrue();

    // Tìm bài tập theo cấp độ HSK
    List<BaiTap> findByCapDoHSKIdAndTrangThaiTrue(Integer capDoHSKId);

    // Tìm bài tập theo chủ đề
    List<BaiTap> findByChuDeIdAndTrangThaiTrue(Integer chuDeId);

    // Tìm bài tập theo cấp độ HSK và chủ đề
    List<BaiTap> findByCapDoHSKIdAndChuDeIdAndTrangThaiTrue(Integer capDoHSKId, Integer chuDeId);

    // Tìm bài tập theo bài giảng
    List<BaiTap> findByBaiGiangIdAndTrangThaiTrue(Long baiGiangId);

    // Tìm bài tập với thông tin đầy đủ
    @Query("SELECT b FROM BaiTap b " +
            "LEFT JOIN FETCH b.capDoHSK " +
            "LEFT JOIN FETCH b.chuDe " +
            "LEFT JOIN FETCH b.baiGiang " +
            "WHERE b.trangThai = true")
    List<BaiTap> findAllWithDetails();

    // Tìm bài tập theo ID với thông tin đầy đủ
    @Query("SELECT b FROM BaiTap b " +
            "LEFT JOIN FETCH b.capDoHSK " +
            "LEFT JOIN FETCH b.chuDe " +
            "LEFT JOIN FETCH b.baiGiang " +
            "WHERE b.id = :id AND b.trangThai = true")
    Optional<BaiTap> findByIdWithDetails(@Param("id") Long id);
}
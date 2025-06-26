package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface BaiGiangRepository extends JpaRepository<BaiGiang, Long> {
    List<BaiGiang> findByGiangVienID(Long giangVienID);

    List<BaiGiang> findByLoaiBaiGiangId(Integer loaiBaiGiangID);

    List<BaiGiang> findByCapDoHSKId(Integer capDoHSK_ID);

    List<BaiGiang> findByChuDeId(Integer chuDeID);

    @Query("SELECT b FROM BaiGiang b WHERE b.trangThai = true")
    List<BaiGiang> findAllPublished();

    @Query("SELECT b FROM BaiGiang b WHERE b.tieuDe LIKE %?1% OR b.moTa LIKE %?1% OR b.noiDung LIKE %?1%")
    List<BaiGiang> search(String keyword);

    // Nếu chưa có method này, thêm vào BaiGiangRepository
    @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.giangVienID = :giangVienID")
    int countByGiangVienID(@Param("giangVienID") Long giangVienID);

    // Method mới cần thêm để fix lỗi
    @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.giangVienID = :giangVienID AND b.trangThai = :trangThai")
    int countByGiangVienIDAndTrangThai(@Param("giangVienID") Long giangVienID, @Param("trangThai") Boolean trangThai);

    // Các method bổ sung khác cho thống kê
    @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.trangThai = :trangThai")
    long countByTrangThai(@Param("trangThai") Boolean trangThai);

    @Query("SELECT COUNT(b) FROM BaiGiang b WHERE b.ngayTao >= :ngayTao")
    long countByNgayTaoAfter(@Param("ngayTao") LocalDateTime ngayTao);

    // Tìm bài giảng theo giảng viên và trạng thái
    List<BaiGiang> findByGiangVienIDAndTrangThai(Long giangVienID, Boolean trangThai);

    @Query("SELECT b FROM BaiGiang b WHERE b.capDoHSK.capDo = :level")
    List<BaiGiang> findByCapDoHSKLevel(@Param("level") String level);
}
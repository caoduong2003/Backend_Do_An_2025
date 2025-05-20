package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
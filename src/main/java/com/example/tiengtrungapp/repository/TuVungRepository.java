package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.TuVung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuVungRepository extends JpaRepository<TuVung, Long> {
    List<TuVung> findByBaiGiangId(Long baiGiangID);
    
    List<TuVung> findByCapDoHSKId(Integer capDoHSK_ID);
    
    List<TuVung> findByTiengTrungContaining(String keyword);
    
    List<TuVung> findByTiengVietContaining(String keyword);
}
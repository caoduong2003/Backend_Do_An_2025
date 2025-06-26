package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.ChuDe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuDeRepository extends JpaRepository<ChuDe, Integer> {
    List<ChuDe> findByTenChuDeContaining(String keyword);
}
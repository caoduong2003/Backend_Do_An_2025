package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.ChuDe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuDeRepository extends JpaRepository<ChuDe, Integer> {
}
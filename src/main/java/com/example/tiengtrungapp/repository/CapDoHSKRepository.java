package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.CapDoHSK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapDoHSKRepository extends JpaRepository<CapDoHSK, Integer> {
    CapDoHSK findByCapDo(Integer capDo);
}
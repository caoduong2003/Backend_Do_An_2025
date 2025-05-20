package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.LoaiBaiGiang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiBaiGiangRepository extends JpaRepository<LoaiBaiGiang, Integer> {
}
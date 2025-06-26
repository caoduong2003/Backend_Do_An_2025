package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.LoaiBaiGiang;
import com.example.tiengtrungapp.repository.LoaiBaiGiangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loaibaigiang")
@CrossOrigin(origins = "*")
public class LoaiBaiGiangController {

    @Autowired
    private LoaiBaiGiangRepository loaiBaiGiangRepository;

    /**
     * Lấy tất cả loại bài giảng
     */
    @GetMapping
    public ResponseEntity<List<LoaiBaiGiang>> getAllLoaiBaiGiang() {
        try {
            List<LoaiBaiGiang> loaiBaiGiangs = loaiBaiGiangRepository.findAll();
            return new ResponseEntity<>(loaiBaiGiangs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lấy loại bài giảng theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoaiBaiGiang> getLoaiBaiGiangById(@PathVariable Integer id) {
        Optional<LoaiBaiGiang> loaiBaiGiang = loaiBaiGiangRepository.findById(id);
        if (loaiBaiGiang.isPresent()) {
            return ResponseEntity.ok(loaiBaiGiang.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tạo loại bài giảng mới
     */
    @PostMapping
    public ResponseEntity<LoaiBaiGiang> createLoaiBaiGiang(@RequestBody LoaiBaiGiang loaiBaiGiang) {
        try {
            LoaiBaiGiang savedLoaiBaiGiang = loaiBaiGiangRepository.save(loaiBaiGiang);
            return new ResponseEntity<>(savedLoaiBaiGiang, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cập nhật loại bài giảng
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoaiBaiGiang> updateLoaiBaiGiang(@PathVariable Integer id, @RequestBody LoaiBaiGiang loaiBaiGiang) {
        Optional<LoaiBaiGiang> loaiBaiGiangData = loaiBaiGiangRepository.findById(id);

        if (loaiBaiGiangData.isPresent()) {
            LoaiBaiGiang updatedLoaiBaiGiang = loaiBaiGiangData.get();
            updatedLoaiBaiGiang.setTenLoai(loaiBaiGiang.getTenLoai());
            updatedLoaiBaiGiang.setMoTa(loaiBaiGiang.getMoTa());

            return new ResponseEntity<>(loaiBaiGiangRepository.save(updatedLoaiBaiGiang), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Xóa loại bài giảng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLoaiBaiGiang(@PathVariable Integer id) {
        try {
            loaiBaiGiangRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Tìm kiếm loại bài giảng theo tên
     */
    @GetMapping("/search")
    public ResponseEntity<List<LoaiBaiGiang>> searchLoaiBaiGiang(@RequestParam String keyword) {
        try {
            // Tìm kiếm theo tên loại hoặc mô tả
            List<LoaiBaiGiang> loaiBaiGiangs = loaiBaiGiangRepository.findAll().stream()
                    .filter(lbg -> lbg.getTenLoai().toLowerCase().contains(keyword.toLowerCase()) ||
                            (lbg.getMoTa() != null && lbg.getMoTa().toLowerCase().contains(keyword.toLowerCase())))
                    .toList();
            return ResponseEntity.ok(loaiBaiGiangs);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.CapDoHSK;
import com.example.tiengtrungapp.repository.CapDoHSKRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/capdohsk")
@CrossOrigin(origins = "*")
public class CapDoHSKController {

    @Autowired
    private CapDoHSKRepository capDoHSKRepository;

    /**
     * Lấy tất cả cấp độ HSK
     */
    @GetMapping
    public ResponseEntity<List<CapDoHSK>> getAllCapDoHSK() {
        try {
            List<CapDoHSK> capDoHSKs = capDoHSKRepository.findAll();
            return new ResponseEntity<>(capDoHSKs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lấy cấp độ HSK theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CapDoHSK> getCapDoHSKById(@PathVariable Integer id) {
        Optional<CapDoHSK> capDoHSK = capDoHSKRepository.findById(id);
        if (capDoHSK.isPresent()) {
            return ResponseEntity.ok(capDoHSK.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy cấp độ HSK theo số cấp độ
     */
    @GetMapping("/level/{capDo}")
    public ResponseEntity<CapDoHSK> getCapDoHSKByLevel(@PathVariable Integer capDo) {
        CapDoHSK capDoHSK = capDoHSKRepository.findByCapDo(capDo);
        if (capDoHSK != null) {
            return ResponseEntity.ok(capDoHSK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tạo cấp độ HSK mới
     */
    @PostMapping
    public ResponseEntity<CapDoHSK> createCapDoHSK(@RequestBody CapDoHSK capDoHSK) {
        try {
            CapDoHSK savedCapDoHSK = capDoHSKRepository.save(capDoHSK);
            return new ResponseEntity<>(savedCapDoHSK, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cập nhật cấp độ HSK
     */
    @PutMapping("/{id}")
    public ResponseEntity<CapDoHSK> updateCapDoHSK(@PathVariable Integer id, @RequestBody CapDoHSK capDoHSK) {
        Optional<CapDoHSK> capDoHSKData = capDoHSKRepository.findById(id);

        if (capDoHSKData.isPresent()) {
            CapDoHSK updatedCapDoHSK = capDoHSKData.get();
            updatedCapDoHSK.setCapDo(capDoHSK.getCapDo());
            updatedCapDoHSK.setTenCapDo(capDoHSK.getTenCapDo());
            updatedCapDoHSK.setMoTa(capDoHSK.getMoTa());

            return new ResponseEntity<>(capDoHSKRepository.save(updatedCapDoHSK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Xóa cấp độ HSK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCapDoHSK(@PathVariable Integer id) {
        try {
            capDoHSKRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Tìm kiếm cấp độ HSK theo từ khóa
     */
    @GetMapping("/search")
    public ResponseEntity<List<CapDoHSK>> searchCapDoHSK(@RequestParam String keyword) {
        try {
            // Tìm theo tên cấp độ hoặc mô tả
            List<CapDoHSK> capDoHSKs = capDoHSKRepository.findAll().stream()
                    .filter(cap -> cap.getTenCapDo().toLowerCase().contains(keyword.toLowerCase()) ||
                                 (cap.getMoTa() != null && cap.getMoTa().toLowerCase().contains(keyword.toLowerCase())))
                    .toList();
            return ResponseEntity.ok(capDoHSKs);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
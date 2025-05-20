package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import com.example.tiengtrungapp.repository.BaiGiangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/baigiang")
@CrossOrigin(origins = "*")
public class BaiGiangController {

    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @GetMapping
    public ResponseEntity<List<BaiGiang>> getAllBaiGiang(
            @RequestParam(required = false) Long giangVienId,
            @RequestParam(required = false) Integer loaiBaiGiangId,
            @RequestParam(required = false) Integer capDoHSK_ID,
            @RequestParam(required = false) Integer chuDeId,
            @RequestParam(required = false) Boolean published) {
        
        List<BaiGiang> baiGiangs;
        
        if (giangVienId != null) {
            baiGiangs = baiGiangRepository.findByGiangVienID(giangVienId);
        } else if (loaiBaiGiangId != null) {
            baiGiangs = baiGiangRepository.findByLoaiBaiGiangId(loaiBaiGiangId);
        } else if (capDoHSK_ID != null) {
            baiGiangs = baiGiangRepository.findByCapDoHSKId(capDoHSK_ID);
        } else if (chuDeId != null) {
            baiGiangs = baiGiangRepository.findByChuDeId(chuDeId);
        } else if (published != null && published) {
            baiGiangs = baiGiangRepository.findAllPublished();
        } else {
            baiGiangs = baiGiangRepository.findAll();
        }
        
        return ResponseEntity.ok(baiGiangs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaiGiang> getBaiGiangById(@PathVariable Long id) {
        Optional<BaiGiang> baiGiang = baiGiangRepository.findById(id);
        if (baiGiang.isPresent()) {
            // Tăng lượt xem
            BaiGiang baiGiangData = baiGiang.get();
            baiGiangData.setLuotXem(baiGiangData.getLuotXem() + 1);
            baiGiangRepository.save(baiGiangData);
            
            return ResponseEntity.ok(baiGiangData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<BaiGiang> createBaiGiang(@RequestBody BaiGiang baiGiang) {
        try {
            baiGiang.setNgayTao(LocalDateTime.now());
            baiGiang.setLuotXem(0);
            BaiGiang savedBaiGiang = baiGiangRepository.save(baiGiang);
            return new ResponseEntity<>(savedBaiGiang, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaiGiang> updateBaiGiang(@PathVariable Long id, @RequestBody BaiGiang baiGiang) {
        Optional<BaiGiang> baiGiangData = baiGiangRepository.findById(id);
        
        if (baiGiangData.isPresent()) {
            BaiGiang updatedBaiGiang = baiGiangData.get();
            updatedBaiGiang.setTieuDe(baiGiang.getTieuDe());
            updatedBaiGiang.setMoTa(baiGiang.getMoTa());
            updatedBaiGiang.setNoiDung(baiGiang.getNoiDung());
            updatedBaiGiang.setNgayCapNhat(LocalDateTime.now());
            updatedBaiGiang.setLoaiBaiGiang(baiGiang.getLoaiBaiGiang());
            updatedBaiGiang.setCapDoHSK(baiGiang.getCapDoHSK());
            updatedBaiGiang.setChuDe(baiGiang.getChuDe());
            updatedBaiGiang.setThoiLuong(baiGiang.getThoiLuong());
            updatedBaiGiang.setHinhAnh(baiGiang.getHinhAnh());
            updatedBaiGiang.setVideoURL(baiGiang.getVideoURL());
            updatedBaiGiang.setAudioURL(baiGiang.getAudioURL());
            updatedBaiGiang.setTrangThai(baiGiang.getTrangThai());
            updatedBaiGiang.setLaBaiGiangGoi(baiGiang.getLaBaiGiangGoi());
            
            return new ResponseEntity<>(baiGiangRepository.save(updatedBaiGiang), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBaiGiang(@PathVariable Long id) {
        try {
            baiGiangRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BaiGiang>> searchBaiGiang(@RequestParam String keyword) {
        List<BaiGiang> baiGiangs = baiGiangRepository.search(keyword);
        return ResponseEntity.ok(baiGiangs);
    }
}
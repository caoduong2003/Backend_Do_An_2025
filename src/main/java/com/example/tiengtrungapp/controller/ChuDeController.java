package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.ChuDe;
import com.example.tiengtrungapp.repository.ChuDeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chude")
@CrossOrigin(origins = "*")
public class ChuDeController {

    @Autowired
    private ChuDeRepository chuDeRepository;

    @GetMapping
    public ResponseEntity<List<ChuDe>> getAllChuDe() {
        try {
            List<ChuDe> chuDes = chuDeRepository.findAll();
            return new ResponseEntity<>(chuDes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChuDe> getChuDeById(@PathVariable Integer id) {
        Optional<ChuDe> chuDe = chuDeRepository.findById(id);
        if (chuDe.isPresent()) {
            return ResponseEntity.ok(chuDe.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ChuDe> createChuDe(@RequestBody ChuDe chuDe) {
        try {
            ChuDe savedChuDe = chuDeRepository.save(chuDe);
            return new ResponseEntity<>(savedChuDe, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChuDe> updateChuDe(@PathVariable Integer id, @RequestBody ChuDe chuDe) {
        Optional<ChuDe> chuDeData = chuDeRepository.findById(id);

        if (chuDeData.isPresent()) {
            ChuDe updatedChuDe = chuDeData.get();
            updatedChuDe.setTenChuDe(chuDe.getTenChuDe());
            updatedChuDe.setMoTa(chuDe.getMoTa());
            updatedChuDe.setHinhAnh(chuDe.getHinhAnh());

            return new ResponseEntity<>(chuDeRepository.save(updatedChuDe), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteChuDe(@PathVariable Integer id) {
        try {
            chuDeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChuDe>> searchChuDe(@RequestParam String keyword) {
        List<ChuDe> chuDes = chuDeRepository.findByTenChuDeContaining(keyword);
        return ResponseEntity.ok(chuDes);
    }
} 
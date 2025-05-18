package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.AuthDto.DangKyRequest;
import com.example.tiengtrungapp.model.dto.AuthDto.DangNhapRequest;
import com.example.tiengtrungapp.model.dto.AuthDto.JwtResponse;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/dangky")
    public ResponseEntity<?> dangKy(@RequestBody DangKyRequest request) {
        log.info("Yêu cầu đăng ký: {}", request.getTenDangNhap());
        try {
            NguoiDung nguoiDung = authService.dangKy(request);
            return ResponseEntity.ok(nguoiDung);
        } catch (Exception e) {
            log.error("Lỗi đăng ký: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/dangnhap")
    public ResponseEntity<?> dangNhap(@RequestBody DangNhapRequest request) {
        log.info("Yêu cầu đăng nhập: {}", request.getTenDangNhap());
        try {
            JwtResponse response = authService.dangNhap(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi đăng nhập: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
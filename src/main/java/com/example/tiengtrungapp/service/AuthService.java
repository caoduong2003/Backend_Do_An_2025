package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.AuthDto.DangKyRequest;
import com.example.tiengtrungapp.model.dto.AuthDto.DangNhapRequest;
import com.example.tiengtrungapp.model.dto.AuthDto.JwtResponse;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import com.example.tiengtrungapp.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Đăng ký
    public NguoiDung dangKy(DangKyRequest request) {
        log.info("Đăng ký người dùng mới: {}", request.getTenDangNhap());

        // Kiểm tra username
        if (nguoiDungRepository.existsByTenDangNhap(request.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Kiểm tra email
        if (request.getEmail() != null && nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Tạo người dùng mới
        NguoiDung nguoiDung = NguoiDung.builder()
                .tenDangNhap(request.getTenDangNhap())
                .email(request.getEmail())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .hoTen(request.getHoTen())
                .soDienThoai(request.getSoDienThoai())
                .vaiTro(request.getVaiTro() != null ? request.getVaiTro() : 2) // Default: Học viên
                .trinhDoHSK(request.getTrinhDoHSK())
                .ngayTao(LocalDateTime.now())
                .ngayCapNhat(LocalDateTime.now())
                .trangThai(true)
                .build();

        return nguoiDungRepository.save(nguoiDung);
    }

    // Đăng nhập
    public JwtResponse dangNhap(DangNhapRequest request) {
        log.info("Đăng nhập: {}", request.getTenDangNhap());

        // Tìm người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(request.getTenDangNhap())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getMatKhau(), nguoiDung.getMatKhau())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        // Tạo token
        String token = jwtUtils.generateToken(nguoiDung.getTenDangNhap());

        // Cập nhật thời gian đăng nhập
        nguoiDung.setLanDangNhapCuoi(LocalDateTime.now());
        nguoiDungRepository.save(nguoiDung);

        // Trả về kết quả
        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .id(nguoiDung.getId())
                .tenDangNhap(nguoiDung.getTenDangNhap())
                .email(nguoiDung.getEmail())
                .vaiTro(nguoiDung.getVaiTro())
                .hoTen(nguoiDung.getHoTen())
                .build();
    }
}
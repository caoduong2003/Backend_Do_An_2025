package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.UserDto;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.repository.BaiGiangRepository;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final NguoiDungRepository nguoiDungRepository;
    private final BaiGiangRepository baiGiangRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lấy thông tin profile của người dùng
     */
    public UserDto.UserResponse getProfile(String username) {
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        return convertToUserResponse(user);
    }

    /**
     * Cập nhật thông tin profile
     */
    @Transactional
    public UserDto.UserResponse updateProfile(String username, UserDto.UpdateProfileRequest request) {
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Kiểm tra email đã tồn tại (nếu thay đổi)
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (nguoiDungRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã được sử dụng bởi người dùng khác");
            }
        }

        // Cập nhật thông tin
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getHoTen() != null) {
            user.setHoTen(request.getHoTen());
        }
        if (request.getSoDienThoai() != null) {
            user.setSoDienThoai(request.getSoDienThoai());
        }
        if (request.getTrinhDoHSK() != null) {
            user.setTrinhDoHSK(request.getTrinhDoHSK());
        }
        if (request.getHinhDaiDien() != null) {
            user.setHinhDaiDien(request.getHinhDaiDien());
        }

        user.setNgayCapNhat(LocalDateTime.now());
        NguoiDung updatedUser = nguoiDungRepository.save(user);
        
        log.info("Người dùng {} đã cập nhật profile", username);
        return convertToUserResponse(updatedUser);
    }

    /**
     * Thay đổi mật khẩu
     */
    @Transactional
    public void changePassword(String username, UserDto.ChangePasswordRequest request) {
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getMatKhauCu(), user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        // Cập nhật mật khẩu mới
        user.setMatKhau(passwordEncoder.encode(request.getMatKhauMoi()));
        user.setNgayCapNhat(LocalDateTime.now());
        nguoiDungRepository.save(user);
        
        log.info("Người dùng {} đã thay đổi mật khẩu", username);
    }

    /**
     * Cập nhật avatar
     */
    @Transactional
    public void updateAvatar(String username, String avatarUrl) {
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setHinhDaiDien(avatarUrl);
        user.setNgayCapNhat(LocalDateTime.now());
        nguoiDungRepository.save(user);
        
        log.info("Người dùng {} đã cập nhật avatar", username);
    }

    /**
     * Lấy thống kê cá nhân
     */
    public Object getPersonalStatistics(String username) {
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Map<String, Object> statistics = new HashMap<>();
        
        if (user.getVaiTro() == 1) {
            // Thống kê cho giáo viên
            int soBaiGiangDaTao = baiGiangRepository.countByGiangVienID(user.getId());
            int soBaiGiangDangHoatDong = baiGiangRepository.countByGiangVienIDAndTrangThai(user.getId(), true);
            
            statistics.put("soBaiGiangDaTao", soBaiGiangDaTao);
            statistics.put("soBaiGiangDangHoatDong", soBaiGiangDangHoatDong);
            statistics.put("vaiTro", "giangvien");
            
        } else if (user.getVaiTro() == 2) {
            // Thống kê cho học viên
            // TODO: Implement khi có bảng TienTrinh
            statistics.put("soBaiGiangDaHoc", 0);
            statistics.put("soBaiGiangDangHoc", 0);
            statistics.put("tienDoHocTap", 0);
            statistics.put("vaiTro", "hocvien");
        }
        
        statistics.put("ngayThamGia", user.getNgayTao());
        statistics.put("lanDangNhapCuoi", user.getLanDangNhapCuoi());
        
        return statistics;
    }

    /**
     * Chuyển đổi NguoiDung entity sang UserResponse DTO
     */
    private UserDto.UserResponse convertToUserResponse(NguoiDung user) {
        UserDto.UserResponse.UserResponseBuilder builder = UserDto.UserResponse.builder()
                .id(user.getId())
                .tenDangNhap(user.getTenDangNhap())
                .email(user.getEmail())
                .hoTen(user.getHoTen())
                .soDienThoai(user.getSoDienThoai())
                .vaiTro(user.getVaiTro())
                .vaiTroText(UserDto.getVaiTroText(user.getVaiTro()))
                .trinhDoHSK(user.getTrinhDoHSK())
                .hinhDaiDien(user.getHinhDaiDien())
                .ngayTao(user.getNgayTao())
                .ngayCapNhat(user.getNgayCapNhat())
                .lanDangNhapCuoi(user.getLanDangNhapCuoi())
                .trangThai(user.getTrangThai())
                .trangThaiText(UserDto.getTrangThaiText(user.getTrangThai()));

        // Thêm thông tin thống kê nếu cần
        if (user.getVaiTro() == 1) {
            int soBaiGiangDaTao = baiGiangRepository.countByGiangVienID(user.getId());
            builder.soBaiGiangDaTao(soBaiGiangDaTao);
        }

        return builder.build();
    }
}
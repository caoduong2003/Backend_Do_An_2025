package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.UserDto;
import com.example.tiengtrungapp.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Lấy thông tin profile của người dùng hiện tại
     */
    @GetMapping
    public ResponseEntity<UserDto.UserResponse> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserDto.UserResponse profile = profileService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    /**
     * Cập nhật thông tin profile
     */
    @PutMapping
    public ResponseEntity<UserDto.UserResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UserDto.UpdateProfileRequest request) {
        String username = authentication.getName();
        log.info("Người dùng {} cập nhật profile", username);
        UserDto.UserResponse updatedProfile = profileService.updateProfile(username, request);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Thay đổi mật khẩu
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody UserDto.ChangePasswordRequest request) {
        String username = authentication.getName();
        log.info("Người dùng {} thay đổi mật khẩu", username);

        if (!request.getMatKhauMoi().equals(request.getXacNhanMatKhau())) {
            return ResponseEntity.badRequest().body("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        profileService.changePassword(username, request);
        return ResponseEntity.ok("Đã thay đổi mật khẩu thành công");
    }

    /**
     * Upload avatar
     */
    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(
            Authentication authentication,
            @RequestParam String avatarUrl) {
        String username = authentication.getName();
        log.info("Người dùng {} cập nhật avatar", username);
        profileService.updateAvatar(username, avatarUrl);
        return ResponseEntity.ok("Đã cập nhật avatar thành công");
    }

    /**
     * Lấy thống kê cá nhân (cho giáo viên và học viên)
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getPersonalStatistics(Authentication authentication) {
        String username = authentication.getName();
        Object statistics = profileService.getPersonalStatistics(username);
        return ResponseEntity.ok(statistics);
    }
}
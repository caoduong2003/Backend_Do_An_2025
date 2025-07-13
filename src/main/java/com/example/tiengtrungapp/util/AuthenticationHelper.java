package com.example.tiengtrungapp.util;

import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationHelper {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    /**
     * Lấy ID người dùng hiện tại từ authentication
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String username = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        return user.getId();
    }

    /**
     * Lấy role người dùng hiện tại
     */
    public Integer getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String username = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        return user.getVaiTro();
    }

    /**
     * Kiểm tra xem người dùng có phải teacher không
     */
    public boolean isCurrentUserTeacher() {
        try {
            Integer role = getCurrentUserRole();
            return role != null && role.equals(1); // 1 = TEACHER
        } catch (Exception e) {
            log.warn("Error checking teacher role: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validate quyền truy cập teacher
     */
    public void validateTeacherAccess(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        Integer currentUserRole = getCurrentUserRole();

        // Admin có thể truy cập tất cả
        if (currentUserRole.equals(0)) {
            return;
        }

        // Teacher chỉ có thể truy cập resource của mình
        if (currentUserRole.equals(1) && currentUserId.equals(resourceOwnerId)) {
            return;
        }

        throw new RuntimeException("Bạn không có quyền truy cập tài nguyên này");
    }

    /**
     * Lấy teacher ID từ authentication - Sử dụng trong Controller
     */
    public Long getTeacherIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        String username = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        // Kiểm tra role teacher
        if (!user.getVaiTro().equals(1)) { // 1 = TEACHER
            throw new RuntimeException("Người dùng không có quyền giảng viên");
        }

        return user.getId();
    }
}
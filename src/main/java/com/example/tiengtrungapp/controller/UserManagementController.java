package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.UserDto;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserManagementController {

    private final UserManagementService userManagementService;

    /**
     * Lấy danh sách tất cả người dùng với phân trang
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto.UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngayTao") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer vaiTro,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "false") Boolean includeDeleted) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserDto.UserResponse> users = userManagementService.getAllUsers(pageable, vaiTro, keyword, includeDeleted);
        return ResponseEntity.ok(users);
    }

    /**
     * Lấy danh sách tất cả người dùng (bao gồm đã xóa)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto.UserResponse>> getAllUsersIncludingDeleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngayTao") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer vaiTro,
            @RequestParam(required = false) String keyword) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserDto.UserResponse> users = userManagementService.getAllUsers(pageable, vaiTro, keyword, true);
        return ResponseEntity.ok(users);
    }

    /**
     * Lấy danh sách giáo viên
     */
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto.UserResponse>> getAllTeachers() {
        List<UserDto.UserResponse> teachers = userManagementService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    /**
     * Lấy danh sách học viên
     */
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto.UserResponse>> getAllStudents() {
        List<UserDto.UserResponse> students = userManagementService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Lấy thông tin chi tiết của một người dùng
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> getUserById(@PathVariable Long id) {
        UserDto.UserResponse user = userManagementService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Tạo tài khoản mới (Admin tạo cho giáo viên/học viên)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> createUser(@Valid @RequestBody UserDto.CreateUserRequest request) {
        log.info("Admin tạo tài khoản mới: {}", request.getTenDangNhap());
        UserDto.UserResponse user = userManagementService.createUser(request);
        return ResponseEntity.ok(user);
    }

    /**
     * Cập nhật thông tin người dùng
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserDto.UpdateUserRequest request) {
        log.info("Admin cập nhật thông tin người dùng ID: {}", id);
        UserDto.UserResponse user = userManagementService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    /**
     * Thay đổi trạng thái người dùng (kích hoạt/vô hiệu hóa)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> changeUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean trangThai) {
        log.info("Admin thay đổi trạng thái người dùng ID: {} thành: {}", id, trangThai);
        UserDto.UserResponse user = userManagementService.changeUserStatus(id, trangThai);
        return ResponseEntity.ok(user);
    }

    /**
     * Đặt lại mật khẩu cho người dùng
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetUserPassword(@PathVariable Long id) {
        log.info("Admin đặt lại mật khẩu cho người dùng ID: {}", id);
        String newPassword = userManagementService.resetUserPassword(id);
        return ResponseEntity.ok("Mật khẩu mới: " + newPassword);
    }

    /**
     * Khôi phục người dùng đã xóa
     */
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> restoreUser(@PathVariable Long id) {
        log.info("Admin khôi phục người dùng ID: {}", id);
        UserDto.UserResponse restoredUser = userManagementService.restoreUser(id);
        return ResponseEntity.ok(restoredUser);
    }

    /**
     * Xóa người dùng (soft delete)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserResponse> deleteUser(@PathVariable Long id) {
        log.info("Admin xóa người dùng ID: {}", id);
        UserDto.UserResponse deletedUser = userManagementService.deleteUser(id);
        return ResponseEntity.ok(deletedUser);
    }

    /**
     * Lấy thống kê người dùng
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto.UserStatistics> getUserStatistics() {
        UserDto.UserStatistics statistics = userManagementService.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Tìm kiếm người dùng
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto.UserResponse>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer vaiTro) {
        List<UserDto.UserResponse> users = userManagementService.searchUsers(keyword, vaiTro);
        return ResponseEntity.ok(users);
    }

    /**
     * Xuất danh sách người dùng (CSV)
     */
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> exportUsers(@RequestParam(required = false) Integer vaiTro) {
        String csvData = userManagementService.exportUsersToCSV(vaiTro);
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=users.csv")
                .body(csvData);
    }
}
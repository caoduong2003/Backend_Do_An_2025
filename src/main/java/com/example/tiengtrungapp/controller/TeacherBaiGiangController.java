package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.BaiGiangTeacherDto;
import com.example.tiengtrungapp.service.TeacherBaiGiangService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.tiengtrungapp.util.AuthenticationHelper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller for Teacher BaiGiang CRUD operations
 * This is separate from existing controllers to avoid conflicts
 * Base URL: /api/teacher-baigiang
 */
@RestController
@RequestMapping("/api/teacher-baigiang")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherBaiGiangController {

        private final TeacherBaiGiangService teacherBaiGiangService;
        @Autowired
        private AuthenticationHelper authenticationHelper;

        /**
         * GET /api/teacher-baigiang
         * Lấy danh sách bài giảng của giáo viên với phân trang và filter
         */
        @GetMapping
        public ResponseEntity<BaiGiangTeacherDto.PageResponse<BaiGiangTeacherDto.TeacherBaiGiangResponse>> getMyBaiGiangs(
                        Authentication authentication,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "ngayTao") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDir,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) Integer capDoHSKId,
                        @RequestParam(required = false) Integer chuDeId,
                        @RequestParam(required = false) Integer loaiBaiGiangId,
                        @RequestParam(required = false) Boolean trangThai) {

                log.info("🎯 Teacher {} requesting bai giang list - page: {}, size: {}",
                                authentication.getName(), page, size);

                Long teacherId = getTeacherIdFromAuthentication(authentication);

                Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

                BaiGiangTeacherDto.PageResponse<BaiGiangTeacherDto.TeacherBaiGiangResponse> response = teacherBaiGiangService
                                .getTeacherBaiGiangs(
                                                teacherId, search, capDoHSKId, chuDeId, loaiBaiGiangId, trangThai,
                                                pageable);

                log.info("✅ Returning {} bai giang items for teacher {}",
                                response.getTotalElements(), teacherId);

                return ResponseEntity.ok(response);
        }

        /**
         * GET /api/teacher-baigiang/{id}
         * Lấy chi tiết bài giảng theo ID
         */
        @GetMapping("/{id}")
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangDetailResponse> getBaiGiangById(
                        Authentication authentication,
                        @PathVariable Long id) {

                log.info("🔍 Teacher {} requesting bai giang details for ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                BaiGiangTeacherDto.TeacherBaiGiangDetailResponse response = teacherBaiGiangService
                                .getTeacherBaiGiangById(teacherId, id);

                return ResponseEntity.ok(response);
        }

        /**
         * POST /api/teacher-baigiang
         * Tạo bài giảng mới
         */
        @PostMapping
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangResponse> createBaiGiang(
                        Authentication authentication,
                        @Valid @RequestBody BaiGiangTeacherDto.CreateTeacherBaiGiangRequest request) {

                log.info("➕ Teacher {} creating new bai giang: {}",
                                authentication.getName(), request.getTieuDe());

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                BaiGiangTeacherDto.TeacherBaiGiangResponse response = teacherBaiGiangService.createTeacherBaiGiang(
                                teacherId,
                                request);

                log.info("✅ Created bai giang with ID: {} for teacher: {}",
                                response.getId(), teacherId);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * PUT /api/teacher-baigiang/{id}
         * Cập nhật bài giảng
         */
        @PutMapping("/{id}")
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangResponse> updateBaiGiang(
                        Authentication authentication,
                        @PathVariable Long id,
                        @Valid @RequestBody BaiGiangTeacherDto.UpdateTeacherBaiGiangRequest request) {

                log.info("✏️ Teacher {} updating bai giang ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                BaiGiangTeacherDto.TeacherBaiGiangResponse response = teacherBaiGiangService.updateTeacherBaiGiang(
                                teacherId,
                                id, request);

                log.info("✅ Updated bai giang ID: {} for teacher: {}", id, teacherId);

                return ResponseEntity.ok(response);
        }

        /**
         * DELETE /api/teacher-baigiang/{id}
         * Xóa bài giảng (soft delete)
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Map<String, String>> deleteBaiGiang(
                        Authentication authentication,
                        @PathVariable Long id) {

                log.info("🗑️ Teacher {} deleting bai giang ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                teacherBaiGiangService.deleteTeacherBaiGiang(teacherId, id);

                log.info("✅ Deleted bai giang ID: {} for teacher: {}", id, teacherId);

                return ResponseEntity.ok(Map.of("message", "Xóa bài giảng thành công"));
        }

        /**
         * PATCH /api/teacher-baigiang/{id}/toggle-status
         * Thay đổi trạng thái công khai/ẩn bài giảng
         */
        @PatchMapping("/{id}/toggle-status")
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangResponse> toggleBaiGiangStatus(
                        Authentication authentication,
                        @PathVariable Long id) {

                log.info("🔄 Teacher {} toggling status for bai giang ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                BaiGiangTeacherDto.TeacherBaiGiangResponse response = teacherBaiGiangService.toggleBaiGiangStatus(
                                teacherId,
                                id);

                log.info("✅ Toggled status for bai giang ID: {} - new status: {}",
                                id, response.getTrangThai());

                return ResponseEntity.ok(response);
        }

        /**
         * POST /api/teacher-baigiang/{id}/duplicate
         * Sao chép bài giảng
         */
        @PostMapping("/{id}/duplicate")
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangResponse> duplicateBaiGiang(
                        Authentication authentication,
                        @PathVariable Long id,
                        @RequestBody(required = false) Map<String, String> options) {

                log.info("📋 Teacher {} duplicating bai giang ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                String newTitle = options != null ? options.get("newTitle") : null;

                BaiGiangTeacherDto.TeacherBaiGiangResponse response = teacherBaiGiangService.duplicateTeacherBaiGiang(
                                teacherId,
                                id, newTitle);

                log.info("✅ Duplicated bai giang - original ID: {}, new ID: {}",
                                id, response.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * GET /api/teacher-baigiang/statistics
         * Lấy thống kê bài giảng của giáo viên
         */
        @GetMapping("/statistics")
        public ResponseEntity<BaiGiangTeacherDto.TeacherBaiGiangStatsResponse> getBaiGiangStatistics(
                        Authentication authentication) {

                log.info("📊 Teacher {} requesting bai giang statistics",
                                authentication.getName());

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                BaiGiangTeacherDto.TeacherBaiGiangStatsResponse response = teacherBaiGiangService
                                .getTeacherBaiGiangStats(teacherId);

                return ResponseEntity.ok(response);
        }

        /**
         * GET /api/teacher-baigiang/search
         * Tìm kiếm bài giảng của giáo viên
         */
        @GetMapping("/search")
        public ResponseEntity<List<BaiGiangTeacherDto.TeacherBaiGiangResponse>> searchMyBaiGiangs(
                        Authentication authentication,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "10") int limit) {

                log.info("🔍 Teacher {} searching bai giang with keyword: {}",
                                authentication.getName(), keyword);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                List<BaiGiangTeacherDto.TeacherBaiGiangResponse> response = teacherBaiGiangService
                                .searchTeacherBaiGiangs(teacherId, keyword, limit);

                return ResponseEntity.ok(response);
        }

        /**
         * PATCH /api/teacher-baigiang/{id}/increment-views
         * Tăng số lượt xem bài giảng
         */
        @PatchMapping("/{id}/increment-views")
        public ResponseEntity<Map<String, Object>> incrementViews(
                        Authentication authentication,
                        @PathVariable Long id) {

                log.info("👀 Incrementing views for bai giang ID: {}", id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                Integer newViewCount = teacherBaiGiangService.incrementBaiGiangViews(teacherId, id);

                return ResponseEntity.ok(Map.of(
                                "baiGiangId", id,
                                "newViewCount", newViewCount,
                                "message", "Đã cập nhật lượt xem"));
        }

        /**
         * GET /api/teacher-baigiang/quick-stats
         * Lấy thống kê nhanh cho dashboard
         */
        @GetMapping("/quick-stats")
        public ResponseEntity<Map<String, Object>> getQuickStats(
                        Authentication authentication) {

                log.info("⚡ Teacher {} requesting quick stats", authentication.getName());

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                Map<String, Object> stats = teacherBaiGiangService.getQuickStats(teacherId);

                return ResponseEntity.ok(stats);
        }

        /**
         * GET /api/teacher-baigiang/{id}/validate-ownership
         * Kiểm tra quyền sở hữu bài giảng
         */
        @GetMapping("/{id}/validate-ownership")
        public ResponseEntity<Map<String, Object>> validateOwnership(
                        Authentication authentication,
                        @PathVariable Long id) {

                log.info("🔒 Teacher {} validating ownership for bai giang ID: {}",
                                authentication.getName(), id);

                Long teacherId = getTeacherIdFromAuthentication(authentication);
                boolean isOwner = teacherBaiGiangService.validateTeacherOwnership(teacherId, id);

                return ResponseEntity.ok(Map.of(
                                "baiGiangId", id,
                                "isOwner", isOwner,
                                "teacherId", teacherId));
        }

        /**
         * Lấy ID giáo viên từ authentication
         * TODO: Implement logic dựa trên hệ thống auth của bạn
         */
        // private Long getTeacherIdFromAuthentication(Authentication authentication) {
        // // PHƯƠNG PHÁP 1: Nếu sử dụng JWT token
        // // JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        // // return Long.valueOf(jwtToken.getToken().getClaimAsString("userId"));

        // // PHƯƠNG PHÁP 2: Nếu sử dụng UserDetails
        // // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // // return userService.getUserByUsername(userDetails.getUsername()).getId();

        // // PHƯƠNG PHÁP 3: Nếu sử dụng custom principal
        // // CustomUserPrincipal principal = (CustomUserPrincipal)
        // // authentication.getPrincipal();
        // // return principal.getUserId();

        // // PHƯƠNG PHÁP 4: Từ SecurityContextHolder
        // // return SecurityContextHolder.getContext().getAuthentication().getName();

        // // PLACEHOLDER - Thay thế bằng logic thực tế
        // log.warn("🚨 Using placeholder teacher ID - replace with actual
        // implementation");
        // return 1L; // Tạm thời trả về ID = 1 để test
        // }
        private Long getTeacherIdFromAuthentication(Authentication authentication) {
                return authenticationHelper.getTeacherIdFromAuthentication(authentication);
        }
}
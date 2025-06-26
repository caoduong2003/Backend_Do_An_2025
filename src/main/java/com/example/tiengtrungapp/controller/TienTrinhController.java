package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.TienTrinhDto;
import com.example.tiengtrungapp.service.TienTrinhService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tien-trinh")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TienTrinhController {

    private final TienTrinhService tienTrinhService;

    /**
     * Tạo tiến trình học tập mới
     */
    @PostMapping
    public ResponseEntity<TienTrinhDto.TienTrinhResponse> createTienTrinh(
            Authentication authentication,
            @Valid @RequestBody TienTrinhDto.CreateTienTrinhRequest request) {
        
        // Lấy ID học viên từ authentication (cần implement logic lấy user ID)
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        log.info("Học viên ID: {} tạo tiến trình học tập cho bài giảng ID: {}", hocVienId, request.getBaiGiangId());
        TienTrinhDto.TienTrinhResponse tienTrinh = tienTrinhService.createTienTrinh(hocVienId, request);
        return ResponseEntity.ok(tienTrinh);
    }

    /**
     * Cập nhật tiến trình học tập
     */
    @PutMapping("/{id}")
    public ResponseEntity<TienTrinhDto.TienTrinhResponse> updateTienTrinh(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody TienTrinhDto.UpdateTienTrinhRequest request) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        log.info("Học viên ID: {} cập nhật tiến trình học tập ID: {}", hocVienId, id);
        TienTrinhDto.TienTrinhResponse tienTrinh = tienTrinhService.updateTienTrinh(hocVienId, id, request);
        return ResponseEntity.ok(tienTrinh);
    }

    /**
     * Lấy tiến trình học tập theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TienTrinhDto.TienTrinhResponse> getTienTrinhById(
            Authentication authentication,
            @PathVariable Long id) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        TienTrinhDto.TienTrinhResponse tienTrinh = tienTrinhService.getTienTrinhById(hocVienId, id);
        return ResponseEntity.ok(tienTrinh);
    }

    /**
     * Lấy danh sách tiến trình học tập của học viên
     */
    @GetMapping
    public ResponseEntity<Page<TienTrinhDto.TienTrinhResponse>> getTienTrinhList(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngayCapNhat") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer capDoHSKId,
            @RequestParam(required = false) Integer chuDeId,
            @RequestParam(required = false) Integer loaiBaiGiangId,
            @RequestParam(required = false) String keyword) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Tạo filter
        TienTrinhDto.TienTrinhFilter filter = TienTrinhDto.TienTrinhFilter.builder()
                .trangThai(trangThai)
                .capDoHSKId(capDoHSKId)
                .chuDeId(chuDeId)
                .loaiBaiGiangId(loaiBaiGiangId)
                .keyword(keyword)
                .build();
        
        Page<TienTrinhDto.TienTrinhResponse> tienTrinhPage = tienTrinhService.getTienTrinhByHocVienId(hocVienId, pageable, filter);
        return ResponseEntity.ok(tienTrinhPage);
    }

    /**
     * Lấy thống kê tiến trình học tập
     */
    @GetMapping("/statistics")
    public ResponseEntity<TienTrinhDto.TienTrinhStatistics> getTienTrinhStatistics(Authentication authentication) {
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        TienTrinhDto.TienTrinhStatistics statistics = tienTrinhService.getTienTrinhStatistics(hocVienId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Lấy tiến trình gần đây nhất
     */
    @GetMapping("/recent")
    public ResponseEntity<List<TienTrinhDto.TienTrinhResponse>> getRecentTienTrinh(
            Authentication authentication,
            @RequestParam(defaultValue = "5") int limit) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        List<TienTrinhDto.TienTrinhResponse> tienTrinhList = tienTrinhService.getRecentTienTrinh(hocVienId, limit);
        return ResponseEntity.ok(tienTrinhList);
    }

    /**
     * Lấy tiến trình có điểm cao nhất
     */
    @GetMapping("/top-scores")
    public ResponseEntity<List<TienTrinhDto.TienTrinhResponse>> getTopScores(
            Authentication authentication,
            @RequestParam(defaultValue = "5") int limit) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        List<TienTrinhDto.TienTrinhResponse> tienTrinhList = tienTrinhService.getTopScores(hocVienId, limit);
        return ResponseEntity.ok(tienTrinhList);
    }

    /**
     * Xóa tiến trình học tập
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTienTrinh(
            Authentication authentication,
            @PathVariable Long id) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        tienTrinhService.deleteTienTrinh(hocVienId, id);
        return ResponseEntity.ok("Đã xóa tiến trình học tập thành công");
    }

    /**
     * Lấy tiến trình theo bài giảng
     */
    @GetMapping("/bai-giang/{baiGiangId}")
    public ResponseEntity<TienTrinhDto.TienTrinhResponse> getTienTrinhByBaiGiang(
            Authentication authentication,
            @PathVariable Long baiGiangId) {
        
        Long hocVienId = getHocVienIdFromAuthentication(authentication);
        
        // TODO: Implement method to get progress by lesson
        // TienTrinhDto.TienTrinhResponse tienTrinh = tienTrinhService.getTienTrinhByBaiGiang(hocVienId, baiGiangId);
        // return ResponseEntity.ok(tienTrinh);
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Helper method để lấy ID học viên từ authentication
     * TODO: Implement logic lấy user ID từ authentication
     */
    private Long getHocVienIdFromAuthentication(Authentication authentication) {
        // Tạm thời return 1, cần implement logic thực tế
        // String username = authentication.getName();
        // NguoiDung user = nguoiDungRepository.findByTenDangNhap(username).orElse(null);
        // return user != null ? user.getId() : null;
        return 1L; // Tạm thời hardcode
    }
} 
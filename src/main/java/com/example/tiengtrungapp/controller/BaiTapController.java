package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.ApiResponse;
import com.example.tiengtrungapp.model.dto.BaiTapDto;
import com.example.tiengtrungapp.model.dto.KetQuaBaiTapDto;
import com.example.tiengtrungapp.service.BaiTapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bai-tap")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*") // Cho phép Android app gọi API
public class BaiTapController {

    private final BaiTapService baiTapService;

    /**
     * Lấy danh sách bài tập
     * GET /api/bai-tap?capDoHSKId=1&chuDeId=2
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BaiTapDto.BaiTapResponse>>> getBaiTapList(
            @RequestParam(required = false) Integer capDoHSKId,
            @RequestParam(required = false) Integer chuDeId,
            Authentication authentication) {

        try {
            log.info("API: Getting bai tap list - capDoHSK: {}, chuDe: {}", capDoHSKId, chuDeId);

            Long hocVienId = getHocVienIdFromAuthentication(authentication);
            List<BaiTapDto.BaiTapResponse> baiTapList = baiTapService.getBaiTapList(hocVienId, capDoHSKId, chuDeId);

            log.info("API: Returned {} bai tap", baiTapList.size());

            return ResponseEntity.ok(
                    ApiResponse.success(baiTapList, "Lấy danh sách bài tập thành công"));

        } catch (Exception e) {
            log.error("API: Error getting bai tap list", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể lấy danh sách bài tập: " + e.getMessage()));
        }
    }

    /**
     * Lấy chi tiết bài tập
     * GET /api/bai-tap/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BaiTapDto.BaiTapDetailResponse>> getBaiTapDetail(
            @PathVariable Long id) {

        try {
            log.info("API: Getting bai tap detail for ID: {}", id);

            Optional<BaiTapDto.BaiTapDetailResponse> baiTap = baiTapService.getBaiTapDetail(id);

            if (baiTap.isPresent()) {
                log.info("API: Found bai tap: {}", baiTap.get().getTieuDe());
                return ResponseEntity.ok(
                        ApiResponse.success(baiTap.get(), "Lấy chi tiết bài tập thành công"));
            } else {
                log.warn("API: Bai tap not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            log.error("API: Error getting bai tap detail for ID: {}", id, e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể lấy chi tiết bài tập: " + e.getMessage()));
        }
    }

    /**
     * Làm bài tập (nộp bài)
     * POST /api/bai-tap/lam-bai
     */
    @PostMapping("/lam-bai")
    public ResponseEntity<ApiResponse<KetQuaBaiTapDto.KetQuaBaiTapResponse>> lamBaiTap(
            @Valid @RequestBody BaiTapDto.LamBaiTapRequest request,
            Authentication authentication) {

        try {
            log.info("API: Processing quiz submission for baiTapId: {}", request.getBaiTapId());

            Long hocVienId = getHocVienIdFromAuthentication(authentication);
            KetQuaBaiTapDto.KetQuaBaiTapResponse ketQua = baiTapService.lamBaiTap(hocVienId, request);

            log.info("API: Quiz submitted successfully, score: {}/{}",
                    ketQua.getDiemSo(), ketQua.getDiemToiDa());

            return ResponseEntity.ok(
                    ApiResponse.success(ketQua, "Nộp bài tập thành công"));

        } catch (Exception e) {
            log.error("API: Error submitting quiz for baiTapId: {}", request.getBaiTapId(), e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể nộp bài tập: " + e.getMessage()));
        }
    }

    /**
     * Lấy kết quả bài tập của học viên
     * GET /api/bai-tap/ket-qua
     */
    @GetMapping("/ket-qua")
    public ResponseEntity<ApiResponse<List<KetQuaBaiTapDto.KetQuaBaiTapResponse>>> getKetQuaBaiTap(
            Authentication authentication) {

        try {
            log.info("API: Getting quiz results");

            Long hocVienId = getHocVienIdFromAuthentication(authentication);
            List<KetQuaBaiTapDto.KetQuaBaiTapResponse> ketQuaList = baiTapService.getKetQuaBaiTap(hocVienId);

            log.info("API: Returned {} quiz results", ketQuaList.size());

            return ResponseEntity.ok(
                    ApiResponse.success(ketQuaList, "Lấy kết quả bài tập thành công"));

        } catch (Exception e) {
            log.error("API: Error getting quiz results", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể lấy kết quả bài tập: " + e.getMessage()));
        }
    }

    /**
     * Lấy thống kê bài tập
     * GET /api/bai-tap/thong-ke
     */
    @GetMapping("/thong-ke")
    public ResponseEntity<ApiResponse<KetQuaBaiTapDto.ThongKeResponse>> getThongKe(
            Authentication authentication) {

        try {
            log.info("API: Getting quiz statistics");

            Long hocVienId = getHocVienIdFromAuthentication(authentication);
            KetQuaBaiTapDto.ThongKeResponse thongKe = baiTapService.getThongKe(hocVienId);

            log.info("API: Statistics - {} attempts, avg score: {}",
                    thongKe.getSoLanLamBai(), thongKe.getDiemTrungBinh());

            return ResponseEntity.ok(
                    ApiResponse.success(thongKe, "Lấy thống kê thành công"));

        } catch (Exception e) {
            log.error("API: Error getting quiz statistics", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể lấy thống kê: " + e.getMessage()));
        }
    }

    /**
     * Lấy kết quả chi tiết của một lần làm bài
     * GET /api/bai-tap/ket-qua/{ketQuaId}/chi-tiet
     */
    @GetMapping("/ket-qua/{ketQuaId}/chi-tiet")
    public ResponseEntity<ApiResponse<KetQuaBaiTapDto.KetQuaDetailResponse>> getKetQuaChiTiet(
            @PathVariable Long ketQuaId,
            Authentication authentication) {

        try {
            log.info("API: Getting quiz result detail for ID: {}", ketQuaId);

            Long hocVienId = getHocVienIdFromAuthentication(authentication);

            // TODO: Implement getKetQuaChiTiet in service
            // KetQuaBaiTapDto.KetQuaDetailResponse chiTiet =
            // baiTapService.getKetQuaChiTiet(hocVienId, ketQuaId);

            return ResponseEntity.ok(
                    ApiResponse.success(null, "Chức năng đang phát triển"));

        } catch (Exception e) {
            log.error("API: Error getting quiz result detail for ID: {}", ketQuaId, e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể lấy chi tiết kết quả: " + e.getMessage()));
        }
    }

    /**
     * Xóa kết quả bài tập
     * DELETE /api/bai-tap/ket-qua/{ketQuaId}
     */
    @DeleteMapping("/ket-qua/{ketQuaId}")
    public ResponseEntity<ApiResponse<String>> deleteKetQua(
            @PathVariable Long ketQuaId,
            Authentication authentication) {

        try {
            log.info("API: Deleting quiz result ID: {}", ketQuaId);

            Long hocVienId = getHocVienIdFromAuthentication(authentication);

            // TODO: Implement deleteKetQua in service
            // baiTapService.deleteKetQua(hocVienId, ketQuaId);

            log.info("API: Quiz result deleted successfully");

            return ResponseEntity.ok(
                    ApiResponse.success("Đã xóa kết quả bài tập thành công", "Xóa thành công"));

        } catch (Exception e) {
            log.error("API: Error deleting quiz result ID: {}", ketQuaId, e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Không thể xóa kết quả: " + e.getMessage()));
        }
    }

    /**
     * Ping test endpoint
     * GET /api/bai-tap/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        log.info("API: Ping test");
        return ResponseEntity.ok(
                ApiResponse.success("pong", "BaiTap API is working"));
    }

    /**
     * Helper method để lấy ID học viên từ authentication
     * TODO: Implement logic thực tế khi có authentication
     */
    private Long getHocVienIdFromAuthentication(Authentication authentication) {
        // Tạm thời return 1 để test
        // Sau này sẽ implement logic thực tế:
        /*
         * if (authentication != null && authentication.isAuthenticated()) {
         * String username = authentication.getName();
         * NguoiDung user = nguoiDungRepository.findByTenDangNhap(username)
         * .orElseThrow(() -> new RuntimeException("User not found"));
         * return user.getId();
         * }
         * throw new RuntimeException("User not authenticated");
         */

        log.debug("Using hardcoded hocVienId = 1 for testing");
        return 1L;
    }
}
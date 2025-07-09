package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.BaiGiang;
import com.example.tiengtrungapp.model.entity.TuVung;
import com.example.tiengtrungapp.model.entity.ChuDe;
import com.example.tiengtrungapp.model.entity.CapDoHSK;
import com.example.tiengtrungapp.model.entity.LoaiBaiGiang;
import com.example.tiengtrungapp.repository.BaiGiangRepository;
import com.example.tiengtrungapp.repository.TuVungRepository;
import com.example.tiengtrungapp.repository.ChuDeRepository;
import com.example.tiengtrungapp.repository.CapDoHSKRepository;
import com.example.tiengtrungapp.repository.LoaiBaiGiangRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/guest")
@CrossOrigin(origins = "*")
@Slf4j
public class GuestController {

    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @Autowired
    private TuVungRepository tuVungRepository;

    @Autowired
    private ChuDeRepository chuDeRepository;

    @Autowired
    private CapDoHSKRepository capDoHSKRepository;

    @Autowired
    private LoaiBaiGiangRepository loaiBaiGiangRepository;

    /**
     * 📊 Lấy thống kê công khai cho guest
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getGuestStats() {
        log.info("🎯 Guest accessing public stats");

        try {
            Map<String, Object> stats = new HashMap<>();

            // Đếm số lượng dữ liệu với error handling
            Long totalBaiGiang = baiGiangRepository.countPublished();
            Long totalChuDe = chuDeRepository.count();
            Long totalCapDoHSK = capDoHSKRepository.count();
            Long totalTuVung = tuVungRepository.countAllVocabulary();

            stats.put("totalBaiGiang", totalBaiGiang != null ? totalBaiGiang.doubleValue() : 0.0);
            stats.put("totalChuDe", totalChuDe != null ? totalChuDe.doubleValue() : 0.0);
            stats.put("totalCapDoHSK", totalCapDoHSK != null ? totalCapDoHSK.doubleValue() : 6.0);
            stats.put("totalVocabulary", totalTuVung != null ? totalTuVung.doubleValue() : 0.0);
            stats.put("isGuestMode", true);
            stats.put("limitation", "Chế độ khách - Giới hạn 10 bài giảng, 5 từ vựng/bài");

            log.info("✅ Guest stats loaded successfully: {}", stats);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            log.error("❌ Error loading guest stats: {}", e.getMessage(), e);

            // Return default stats nếu có lỗi
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalBaiGiang", 0.0);
            defaultStats.put("totalChuDe", 0.0);
            defaultStats.put("totalCapDoHSK", 6.0);
            defaultStats.put("totalVocabulary", 0.0);
            defaultStats.put("isGuestMode", true);
            defaultStats.put("limitation", "Chế độ khách - Giới hạn 10 bài giảng, 5 từ vựng/bài");

            return ResponseEntity.ok(defaultStats);
        }
    }

    /**
     * 📚 Lấy danh sách bài giảng cho guest (giới hạn 10 bài)
     */
    // @GetMapping("/baigiang")
    // public ResponseEntity<List<BaiGiang>> getGuestBaiGiang(
    // @RequestParam(defaultValue = "10") int limit,
    // @RequestParam(required = false) Integer capDoHSK_ID,
    // @RequestParam(required = false) Integer chuDeId) {

    // log.info("🎯 Guest accessing BaiGiang list with limit: {}", limit);

    // try {
    // // SIMPLE VERSION - CHỈ LẤY findAll() ĐƠN GIẢN
    // List<BaiGiang> allBaiGiangs = baiGiangRepository.findAll();

    // log.info("📊 Found {} total BaiGiang records", allBaiGiangs.size());

    // // Manual limit bằng Java Stream (không dùng Pageable)
    // List<BaiGiang> limitedBaiGiangs = allBaiGiangs.stream()
    // .limit(Math.min(limit, 10))
    // .collect(java.util.stream.Collectors.toList());

    // log.info("✅ Returning {} BaiGiang items for guest", limitedBaiGiangs.size());
    // return ResponseEntity.ok(limitedBaiGiangs);

    // } catch (Exception e) {
    // log.error("❌ Error loading guest bai giang list: {}", e.getMessage(), e);
    // e.printStackTrace(); // In full stack trace
    // return ResponseEntity.status(500).body(java.util.List.of());
    // }
    // }
    @GetMapping("/baigiang")
    public ResponseEntity<List<BaiGiang>> getGuestBaiGiang(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Integer capDoHSK_ID,
            @RequestParam(required = false) Integer chuDeId) {

        log.info("🎯 Guest accessing BaiGiang list with limit: {}", limit);

        try {
            // SIMPLE VERSION - CHỈ LẤY findAll() ĐƠN GIẢN
            List<BaiGiang> allBaiGiangs = baiGiangRepository.findAll();

            log.info("📊 Found {} total BaiGiang records", allBaiGiangs.size());

            // Manual limit bằng Java Stream (không dùng Pageable)
            List<BaiGiang> limitedBaiGiangs = allBaiGiangs.stream()
                    .limit(Math.min(limit, 5))
                    .collect(java.util.stream.Collectors.toList());

            log.info("✅ Returning {} BaiGiang items for guest", limitedBaiGiangs.size());
            return ResponseEntity.ok(limitedBaiGiangs);

        } catch (Exception e) {
            log.error("❌ Error loading guest bai giang list: {}", e.getMessage(), e);
            e.printStackTrace(); // In full stack trace
            return ResponseEntity.status(500).body(java.util.List.of());
        }
    }

    /**
     * 📖 Lấy chi tiết bài giảng cho guest
     */
    @GetMapping("/baigiang/{id}")
    public ResponseEntity<BaiGiang> getGuestBaiGiangDetail(@PathVariable Long id) {
        log.info("🎯 Guest accessing BaiGiang detail: {}", id);

        try {
            Optional<BaiGiang> baiGiang = baiGiangRepository.findById(id);
            if (baiGiang.isPresent()) {
                log.info("✅ BaiGiang found for guest: {}", baiGiang.get().getTieuDe());
                return ResponseEntity.ok(baiGiang.get());
            } else {
                log.warn("❌ BaiGiang not found for guest: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("❌ Error loading guest bai giang detail: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 📝 Lấy từ vựng cho guest (giới hạn 5 từ đầu tiên)
     */
    @GetMapping("/tuvung/{baiGiangId}")
    public ResponseEntity<List<TuVung>> getGuestTuVung(
            @PathVariable Long baiGiangId,
            @RequestParam(defaultValue = "5") int limit) {

        log.info("🎯 Guest accessing TuVung for BaiGiang: {}, limit: {}", baiGiangId, limit);

        try {
            // Giới hạn tối đa 5 từ cho guest
            if (limit > 5) {
                limit = 5;
            }

            Pageable pageable = PageRequest.of(0, limit);

            // Sử dụng method có sẵn với pageable parameter
            List<TuVung> tuVungs = tuVungRepository.findByBaiGiangId(baiGiangId, pageable);

            log.info("✅ Returning {} TuVung items for guest", tuVungs.size());
            return ResponseEntity.ok(tuVungs);

        } catch (Exception e) {
            log.error("❌ Error loading guest tu vung list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * 🏷️ Lấy danh sách chủ đề cho guest
     */
    @GetMapping("/chude")
    public ResponseEntity<List<ChuDe>> getGuestChuDe() {
        log.info("🎯 Guest accessing ChuDe list");

        try {
            List<ChuDe> chuDes = chuDeRepository.findAll();
            log.info("✅ Returning {} ChuDe items for guest", chuDes.size());
            return ResponseEntity.ok(chuDes);

        } catch (Exception e) {
            log.error("❌ Error loading guest chu de list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * 📊 Lấy danh sách cấp độ HSK cho guest
     */
    @GetMapping("/capdohsk")
    public ResponseEntity<List<CapDoHSK>> getGuestCapDoHSK() {
        log.info("🎯 Guest accessing CapDoHSK list");

        try {
            List<CapDoHSK> capDoHSKs = capDoHSKRepository.findAll();
            log.info("✅ Returning {} CapDoHSK items for guest", capDoHSKs.size());
            return ResponseEntity.ok(capDoHSKs);

        } catch (Exception e) {
            log.error("❌ Error loading guest cap do hsk list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * 🏷️ Lấy danh sách loại bài giảng cho guest
     */
    @GetMapping("/loaibaigiang")
    public ResponseEntity<List<LoaiBaiGiang>> getGuestLoaiBaiGiang() {
        log.info("🎯 Guest accessing LoaiBaiGiang list");

        try {
            List<LoaiBaiGiang> loaiBaiGiangs = loaiBaiGiangRepository.findAll();
            log.info("✅ Returning {} LoaiBaiGiang items for guest", loaiBaiGiangs.size());
            return ResponseEntity.ok(loaiBaiGiangs);

        } catch (Exception e) {
            log.error("❌ Error loading guest loai bai giang list: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * 🔍 Tìm kiếm bài giảng cho guest
     */
    @GetMapping("/search")
    public ResponseEntity<List<BaiGiang>> searchGuestBaiGiang(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "5") int limit) {

        log.info("🎯 Guest searching BaiGiang with keyword: {}, limit: {}", keyword, limit);

        try {
            // Giới hạn tối đa 5 kết quả cho guest
            if (limit > 5) {
                limit = 5;
            }

            Pageable pageable = PageRequest.of(0, limit);
            List<BaiGiang> baiGiangs = baiGiangRepository.searchForGuest(keyword, pageable);

            log.info("✅ Found {} BaiGiang items for keyword: {}", baiGiangs.size(), keyword);
            return ResponseEntity.ok(baiGiangs);

        } catch (Exception e) {
            log.error("❌ Error searching guest bai giang: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * 💬 Gửi feedback không cần đăng nhập
     */
    @PostMapping("/feedback")
    public ResponseEntity<Map<String, String>> submitGuestFeedback(
            @RequestBody Map<String, Object> feedbackData) {

        log.info("🎯 Guest submitting feedback");

        try {
            // Lưu feedback vào database hoặc log
            String content = (String) feedbackData.get("content");
            Integer rating = (Integer) feedbackData.get("rating");
            String deviceId = (String) feedbackData.get("deviceId");

            log.info("📝 Guest feedback - Rating: {}, Content: {}, DeviceID: {}",
                    rating, content, deviceId);

            // TODO: Implement actual feedback saving logic

            Map<String, String> response = new HashMap<>();
            response.put("message", "Cảm ơn bạn đã phản hồi!");
            response.put("status", "success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error saving guest feedback: {}", e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi khi gửi phản hồi");
            errorResponse.put("status", "error");

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 🔍 Debug endpoint để test repository methods
     */
    @GetMapping("/debug/tuvung/{baiGiangId}")
    public ResponseEntity<Map<String, Object>> debugTuVung(@PathVariable Long baiGiangId) {
        log.info("🔍 Debug TuVung for BaiGiang: {}", baiGiangId);

        Map<String, Object> result = new HashMap<>();

        try {
            // Test basic count
            Long totalCount = tuVungRepository.countByBaiGiangId(baiGiangId);
            result.put("totalCount", totalCount);

            // Test basic find
            List<TuVung> allTuVungs = tuVungRepository.findByBaiGiangId(baiGiangId);
            result.put("allTuVungsSize", allTuVungs.size());

            // Test paginated find
            Pageable pageable = PageRequest.of(0, 5);
            List<TuVung> limitedTuVungs = tuVungRepository.findByBaiGiangId(baiGiangId, pageable);
            result.put("limitedTuVungsSize", limitedTuVungs.size());

            if (!limitedTuVungs.isEmpty()) {
                TuVung first = limitedTuVungs.get(0);
                Map<String, Object> firstTuVung = new HashMap<>();
                firstTuVung.put("id", first.getId());
                firstTuVung.put("tiengTrung", first.getTiengTrung());
                firstTuVung.put("tiengViet", first.getTiengViet());
                result.put("firstTuVung", firstTuVung);
            }

            result.put("status", "success");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("❌ Debug TuVung error: {}", e.getMessage(), e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("errorClass", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 🔍 Test database connection
     */
    @GetMapping("/db-test")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        log.info("🎯 Testing database connection");

        Map<String, Object> result = new HashMap<>();

        try {
            // Test each repository
            Long baiGiangCount = baiGiangRepository.count();
            Long chuDeCount = chuDeRepository.count();
            Long capDoHSKCount = capDoHSKRepository.count();
            Long tuVungCount = tuVungRepository.count();

            result.put("baiGiangCount", baiGiangCount);
            result.put("chuDeCount", chuDeCount);
            result.put("capDoHSKCount", capDoHSKCount);
            result.put("tuVungCount", tuVungCount);
            result.put("databaseStatus", "Connected");
            result.put("timestamp", System.currentTimeMillis());

            log.info("✅ Database test successful: {}", result);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("❌ Database test failed: {}", e.getMessage(), e);

            result.put("databaseStatus", "Error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 🎯 Test endpoint cho guest
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testGuestEndpoint() {
        log.info("🎯 Guest testing endpoint");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Guest API is working!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }
}
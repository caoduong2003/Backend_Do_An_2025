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
     * Lấy danh sách bài giảng cho guest (giới hạn 10 bài)
     */
    @GetMapping("/baigiang")
    public ResponseEntity<List<BaiGiang>> getGuestBaiGiang(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Integer capDoHSK_ID,
            @RequestParam(required = false) Integer chuDeId) {

        log.info("🎯 Guest accessing BaiGiang list with limit: {}", limit);

        // Giới hạn tối đa 10 bài cho guest
        if (limit > 10) {
            limit = 10;
        }

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "ngayTao"));
        List<BaiGiang> baiGiangs;

        if (capDoHSK_ID != null) {
            baiGiangs = baiGiangRepository.findByCapDoHSKId(capDoHSK_ID, pageable);
        } else if (chuDeId != null) {
            baiGiangs = baiGiangRepository.findByChuDeId(chuDeId, pageable);
        } else {
            // Lấy những bài giảng được publish và phổ biến nhất
            baiGiangs = baiGiangRepository.findAllPublished(pageable);
        }

        log.info("✅ Returning {} BaiGiang items for guest", baiGiangs.size());
        return ResponseEntity.ok(baiGiangs);
    }

    /**
     * Lấy chi tiết bài giảng cho guest
     */
    @GetMapping("/baigiang/{id}")
    public ResponseEntity<BaiGiang> getGuestBaiGiangDetail(@PathVariable Long id) {
        log.info("🎯 Guest accessing BaiGiang detail: {}", id);

        Optional<BaiGiang> baiGiang = baiGiangRepository.findById(id);
        if (baiGiang.isPresent()) {
            log.info("✅ BaiGiang found for guest: {}", baiGiang.get().getTieuDe());
            return ResponseEntity.ok(baiGiang.get());
        } else {
            log.warn("❌ BaiGiang not found for guest: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy từ vựng cho guest (giới hạn 5 từ đầu tiên)
     */
    @GetMapping("/tuvung/{baiGiangId}")
    public ResponseEntity<List<TuVung>> getGuestTuVung(
            @PathVariable Long baiGiangId,
            @RequestParam(defaultValue = "5") int limit) {

        log.info("🎯 Guest accessing TuVung for BaiGiang: {}, limit: {}", baiGiangId, limit);

        // Giới hạn tối đa 5 từ cho guest
        if (limit > 5) {
            limit = 5;
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<TuVung> tuVungs = tuVungRepository.findByBaiGiangId(baiGiangId, pageable);

        log.info("✅ Returning {} TuVung items for guest", tuVungs.size());
        return ResponseEntity.ok(tuVungs);
    }

    /**
     * Lấy danh sách chủ đề cho guest
     */
    @GetMapping("/chude")
    public ResponseEntity<List<ChuDe>> getGuestChuDe() {
        log.info("🎯 Guest accessing ChuDe list");

        List<ChuDe> chuDes = chuDeRepository.findAll();
        log.info("✅ Returning {} ChuDe items for guest", chuDes.size());
        return ResponseEntity.ok(chuDes);
    }

    /**
     * Lấy danh sách cấp độ HSK cho guest
     */
    @GetMapping("/capdohsk")
    public ResponseEntity<List<CapDoHSK>> getGuestCapDoHSK() {
        log.info("🎯 Guest accessing CapDoHSK list");

        List<CapDoHSK> capDoHSKs = capDoHSKRepository.findAll();
        log.info("✅ Returning {} CapDoHSK items for guest", capDoHSKs.size());
        return ResponseEntity.ok(capDoHSKs);
    }

    /**
     * Lấy danh sách loại bài giảng cho guest
     */
    @GetMapping("/loaibaigiang")
    public ResponseEntity<List<LoaiBaiGiang>> getGuestLoaiBaiGiang() {
        log.info("🎯 Guest accessing LoaiBaiGiang list");

        List<LoaiBaiGiang> loaiBaiGiangs = loaiBaiGiangRepository.findAll();
        log.info("✅ Returning {} LoaiBaiGiang items for guest", loaiBaiGiangs.size());
        return ResponseEntity.ok(loaiBaiGiangs);
    }

    /**
     * Thống kê nhanh cho guest
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getGuestStats() {
        log.info("🎯 Guest accessing stats");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBaiGiang", Math.min(baiGiangRepository.count(), 10)); // Chỉ hiển thị tối đa 10
        stats.put("totalChuDe", chuDeRepository.count());
        stats.put("totalCapDoHSK", capDoHSKRepository.count());
        stats.put("isGuestMode", true);
        stats.put("limitation", "Chế độ khách - Giới hạn 10 bài giảng, 5 từ vựng/bài");

        log.info("✅ Returning stats for guest");
        return ResponseEntity.ok(stats);
    }
}
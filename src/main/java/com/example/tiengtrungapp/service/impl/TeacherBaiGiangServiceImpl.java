package com.example.tiengtrungapp.service.impl;

import com.example.tiengtrungapp.model.dto.BaiGiangTeacherDto;
import com.example.tiengtrungapp.model.entity.*;
import com.example.tiengtrungapp.repository.*;
import com.example.tiengtrungapp.service.TeacherBaiGiangService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for Teacher BaiGiang operations
 * This is separate from existing services to avoid conflicts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherBaiGiangServiceImpl implements TeacherBaiGiangService {

    private final BaiGiangRepository baiGiangRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final LoaiBaiGiangRepository loaiBaiGiangRepository;
    private final CapDoHSKRepository capDoHSKRepository;
    private final ChuDeRepository chuDeRepository;

    @Override
    public BaiGiangTeacherDto.PageResponse<BaiGiangTeacherDto.TeacherBaiGiangResponse> getTeacherBaiGiangs(
            Long teacherId, String search, Integer capDoHSKId, Integer chuDeId,
            Integer loaiBaiGiangId, Boolean trangThai, Pageable pageable) {

        log.info("🔍 Getting teacher {} lectures with filters", teacherId);

        // Build specification for filtering
        Specification<BaiGiang> spec = Specification.where(
                (root, query, cb) -> cb.equal(root.get("giangVienID"), teacherId));

        // Add search filter
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("tieuDe")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("moTa")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("maBaiGiang")), "%" + search.toLowerCase() + "%")));
        }

        // Add other filters
        if (capDoHSKId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("capDoHSK").get("id"), capDoHSKId));
        }

        if (chuDeId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("chuDe").get("id"), chuDeId));
        }

        if (loaiBaiGiangId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("loaiBaiGiang").get("id"), loaiBaiGiangId));
        }

        if (trangThai != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("trangThai"), trangThai));
        }

        Page<BaiGiang> baiGiangPage = baiGiangRepository.findAll(spec, pageable);
        List<BaiGiangTeacherDto.TeacherBaiGiangResponse> responseList = baiGiangPage.getContent()
                .stream()
                .map(this::convertToTeacherResponse)
                .collect(Collectors.toList());

        return BaiGiangTeacherDto.PageResponse.<BaiGiangTeacherDto.TeacherBaiGiangResponse>builder()
                .content(responseList)
                .page(baiGiangPage.getNumber())
                .size(baiGiangPage.getSize())
                .totalElements(baiGiangPage.getTotalElements())
                .totalPages(baiGiangPage.getTotalPages())
                .first(baiGiangPage.isFirst())
                .last(baiGiangPage.isLast())
                .hasNext(baiGiangPage.hasNext())
                .hasPrevious(baiGiangPage.hasPrevious())
                .build();
    }

    @Override
    public BaiGiangTeacherDto.TeacherBaiGiangDetailResponse getTeacherBaiGiangById(Long teacherId, Long baiGiangId) {
        log.info("📖 Getting lecture {} details for teacher {}", baiGiangId, teacherId);

        BaiGiang baiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền truy cập bài giảng này");
        }

        return convertToTeacherDetailResponse(baiGiang);
    }

    @Override
    @Transactional
    public BaiGiangTeacherDto.TeacherBaiGiangResponse createTeacherBaiGiang(
            Long teacherId, BaiGiangTeacherDto.CreateTeacherBaiGiangRequest request) {

        log.info("➕ Creating new lecture for teacher {}: {}", teacherId, request.getTieuDe());

        // Validate teacher exists and has correct role
        NguoiDung teacher = nguoiDungRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + teacherId));

        if (!teacher.getVaiTro().equals(1)) { // 1 = TEACHER role
            throw new RuntimeException("Người dùng không có quyền giảng viên");
        }

        // Validate related entities
        LoaiBaiGiang loaiBaiGiang = loaiBaiGiangRepository.findById(request.getLoaiBaiGiangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại bài giảng"));

        CapDoHSK capDoHSK = capDoHSKRepository.findById(request.getCapDoHSKId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấp độ HSK"));

        ChuDe chuDe = chuDeRepository.findById(request.getChuDeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ đề"));

        // Create BaiGiang entity
        BaiGiang baiGiang = BaiGiang.builder()
                .maBaiGiang(generateMaBaiGiang())
                .tieuDe(request.getTieuDe())
                .moTa(request.getMoTa())
                .noiDung(request.getNoiDung())
                .giangVienID(teacherId)
                .loaiBaiGiang(loaiBaiGiang)
                .capDoHSK(capDoHSK)
                .chuDe(chuDe)
                .thoiLuong(request.getThoiLuong())
                .hinhAnh(request.getHinhAnh())
                .videoURL(request.getVideoURL())
                .audioURL(request.getAudioURL())
                .trangThai(request.getTrangThai())
                .laBaiGiangGoi(request.getLaBaiGiangGoi())
                .luotXem(0)
                .ngayTao(LocalDateTime.now())
                .build();

        BaiGiang savedBaiGiang = baiGiangRepository.save(baiGiang);
        log.info("✅ Created lecture with ID: {} for teacher: {}", savedBaiGiang.getId(), teacherId);

        return convertToTeacherResponse(savedBaiGiang);
    }

    @Override
    @Transactional
    public BaiGiangTeacherDto.TeacherBaiGiangResponse updateTeacherBaiGiang(
            Long teacherId, Long baiGiangId, BaiGiangTeacherDto.UpdateTeacherBaiGiangRequest request) {

        log.info("✏️ Updating lecture {} for teacher {}", baiGiangId, teacherId);

        BaiGiang baiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa bài giảng này");
        }

        // Validate related entities
        LoaiBaiGiang loaiBaiGiang = loaiBaiGiangRepository.findById(request.getLoaiBaiGiangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại bài giảng"));

        CapDoHSK capDoHSK = capDoHSKRepository.findById(request.getCapDoHSKId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấp độ HSK"));

        ChuDe chuDe = chuDeRepository.findById(request.getChuDeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ đề"));

        // Update fields
        baiGiang.setTieuDe(request.getTieuDe());
        baiGiang.setMoTa(request.getMoTa());
        baiGiang.setNoiDung(request.getNoiDung());
        baiGiang.setLoaiBaiGiang(loaiBaiGiang);
        baiGiang.setCapDoHSK(capDoHSK);
        baiGiang.setChuDe(chuDe);
        baiGiang.setThoiLuong(request.getThoiLuong());
        baiGiang.setHinhAnh(request.getHinhAnh());
        baiGiang.setVideoURL(request.getVideoURL());
        baiGiang.setAudioURL(request.getAudioURL());
        baiGiang.setTrangThai(request.getTrangThai());
        baiGiang.setLaBaiGiangGoi(request.getLaBaiGiangGoi());
        baiGiang.setNgayCapNhat(LocalDateTime.now());

        BaiGiang updatedBaiGiang = baiGiangRepository.save(baiGiang);
        log.info("✅ Updated lecture ID: {} for teacher: {}", baiGiangId, teacherId);

        return convertToTeacherResponse(updatedBaiGiang);
    }

    @Override
    @Transactional
    public void deleteTeacherBaiGiang(Long teacherId, Long baiGiangId) {
        log.info("🗑️ Deleting lecture {} for teacher {}", baiGiangId, teacherId);

        BaiGiang baiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền xóa bài giảng này");
        }

        // Soft delete by setting trangThai to false
        baiGiang.setTrangThai(false);
        baiGiang.setNgayCapNhat(LocalDateTime.now());
        baiGiangRepository.save(baiGiang);

        log.info("✅ Soft deleted lecture ID: {} for teacher: {}", baiGiangId, teacherId);
    }

    @Override
    @Transactional
    public BaiGiangTeacherDto.TeacherBaiGiangResponse toggleBaiGiangStatus(Long teacherId, Long baiGiangId) {
        log.info("🔄 Toggling status for lecture {} by teacher {}", baiGiangId, teacherId);

        BaiGiang baiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền thay đổi trạng thái bài giảng này");
        }

        baiGiang.setTrangThai(!baiGiang.getTrangThai());
        baiGiang.setNgayCapNhat(LocalDateTime.now());
        BaiGiang updatedBaiGiang = baiGiangRepository.save(baiGiang);

        log.info("✅ Toggled status for lecture ID: {} - new status: {}", baiGiangId, updatedBaiGiang.getTrangThai());
        return convertToTeacherResponse(updatedBaiGiang);
    }

    @Override
    @Transactional
    public BaiGiangTeacherDto.TeacherBaiGiangResponse duplicateTeacherBaiGiang(
            Long teacherId, Long baiGiangId, String newTitle) {

        log.info("📋 Duplicating lecture {} for teacher {}", baiGiangId, teacherId);

        BaiGiang originalBaiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền sao chép bài giảng này");
        }

        String duplicatedTitle = newTitle != null ? newTitle : "Bản sao - " + originalBaiGiang.getTieuDe();

        BaiGiang duplicatedBaiGiang = BaiGiang.builder()
                .maBaiGiang(generateMaBaiGiang())
                .tieuDe(duplicatedTitle)
                .moTa(originalBaiGiang.getMoTa())
                .noiDung(originalBaiGiang.getNoiDung())
                .giangVienID(teacherId)
                .loaiBaiGiang(originalBaiGiang.getLoaiBaiGiang())
                .capDoHSK(originalBaiGiang.getCapDoHSK())
                .chuDe(originalBaiGiang.getChuDe())
                .thoiLuong(originalBaiGiang.getThoiLuong())
                .hinhAnh(originalBaiGiang.getHinhAnh())
                .videoURL(originalBaiGiang.getVideoURL())
                .audioURL(originalBaiGiang.getAudioURL())
                .trangThai(false) // Duplicate as draft
                .laBaiGiangGoi(originalBaiGiang.getLaBaiGiangGoi())
                .luotXem(0)
                .ngayTao(LocalDateTime.now())
                .build();

        BaiGiang savedBaiGiang = baiGiangRepository.save(duplicatedBaiGiang);
        log.info("✅ Duplicated lecture: {} -> {} for teacher: {}", baiGiangId, savedBaiGiang.getId(), teacherId);

        return convertToTeacherResponse(savedBaiGiang);
    }

    @Override
    public BaiGiangTeacherDto.TeacherBaiGiangStatsResponse getTeacherBaiGiangStats(Long teacherId) {
        log.info("📊 Getting stats for teacher {}", teacherId);

        List<BaiGiang> allLectures = baiGiangRepository.findByGiangVienID(teacherId);

        long tongSoBaiGiang = allLectures.size();
        long soBaiGiangCongKhai = allLectures.stream().filter(BaiGiang::getTrangThai).count();
        long soBaiGiangBanNhap = tongSoBaiGiang - soBaiGiangCongKhai;

        long tongLuotXem = allLectures.stream()
                .mapToLong(bg -> bg.getLuotXem() != null ? bg.getLuotXem() : 0)
                .sum();

        double thoiLuongTrungBinh = allLectures.stream()
                .filter(bg -> bg.getThoiLuong() != null)
                .mapToInt(BaiGiang::getThoiLuong)
                .average()
                .orElse(0.0);

        BaiGiang baiGiangPhoBien = allLectures.stream()
                .filter(bg -> bg.getLuotXem() != null)
                .max((bg1, bg2) -> Integer.compare(bg1.getLuotXem(), bg2.getLuotXem()))
                .orElse(null);

        LocalDateTime lanCapNhatCuoi = allLectures.stream()
                .map(BaiGiang::getNgayCapNhat)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        // Statistics by HSK level
        long soBaiGiangHSK1 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 1).count();
        long soBaiGiangHSK2 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 2).count();
        long soBaiGiangHSK3 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 3).count();
        long soBaiGiangHSK4 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 4).count();
        long soBaiGiangHSK5 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 5).count();
        long soBaiGiangHSK6 = allLectures.stream().filter(bg -> bg.getCapDoHSK().getId() == 6).count();

        return BaiGiangTeacherDto.TeacherBaiGiangStatsResponse.builder()
                .tongSoBaiGiang(tongSoBaiGiang)
                .soBaiGiangCongKhai(soBaiGiangCongKhai)
                .soBaiGiangBanNhap(soBaiGiangBanNhap)
                .tongLuotXem(tongLuotXem)
                .thoiLuongTrungBinh(thoiLuongTrungBinh)
                .baiGiangPhoBienNhat(baiGiangPhoBien != null ? baiGiangPhoBien.getTieuDe() : null)
                .luotXemCaoNhat(baiGiangPhoBien != null ? baiGiangPhoBien.getLuotXem().longValue() : 0L)
                .lanCapNhatCuoi(lanCapNhatCuoi)
                .soBaiGiangHSK1(soBaiGiangHSK1)
                .soBaiGiangHSK2(soBaiGiangHSK2)
                .soBaiGiangHSK3(soBaiGiangHSK3)
                .soBaiGiangHSK4(soBaiGiangHSK4)
                .soBaiGiangHSK5(soBaiGiangHSK5)
                .soBaiGiangHSK6(soBaiGiangHSK6)
                .build();
    }

    @Override
    public List<BaiGiangTeacherDto.TeacherBaiGiangResponse> searchTeacherBaiGiangs(
            Long teacherId, String keyword, int limit) {

        log.info("🔍 Searching lectures for teacher {} with keyword: {}", teacherId, keyword);

        List<BaiGiang> allLectures = baiGiangRepository.findByGiangVienID(teacherId);

        return allLectures.stream()
                .filter(bg -> bg.getTieuDe().toLowerCase().contains(keyword.toLowerCase()) ||
                        (bg.getMoTa() != null && bg.getMoTa().toLowerCase().contains(keyword.toLowerCase())) ||
                        bg.getMaBaiGiang().toLowerCase().contains(keyword.toLowerCase()))
                .limit(limit)
                .map(this::convertToTeacherResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Integer incrementBaiGiangViews(Long teacherId, Long baiGiangId) {
        log.info("👀 Incrementing views for lecture {} by teacher {}", baiGiangId, teacherId);

        BaiGiang baiGiang = baiGiangRepository.findById(baiGiangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + baiGiangId));

        // Validate ownership
        if (!validateTeacherOwnership(teacherId, baiGiangId)) {
            throw new RuntimeException("Bạn không có quyền cập nhật lượt xem cho bài giảng này");
        }

        int currentViews = baiGiang.getLuotXem() != null ? baiGiang.getLuotXem() : 0;
        baiGiang.setLuotXem(currentViews + 1);
        baiGiangRepository.save(baiGiang);

        return baiGiang.getLuotXem();
    }

    @Override
    public boolean validateTeacherOwnership(Long teacherId, Long baiGiangId) {
        return baiGiangRepository.existsByIdAndGiangVienID(baiGiangId, teacherId);
    }

    @Override
    public Map<String, Object> getQuickStats(Long teacherId) {
        log.info("⚡ Getting quick stats for teacher {}", teacherId);

        Map<String, Object> stats = new HashMap<>();

        List<BaiGiang> allLectures = baiGiangRepository.findByGiangVienID(teacherId);

        stats.put("totalLectures", allLectures.size());
        stats.put("publishedLectures", allLectures.stream().filter(BaiGiang::getTrangThai).count());
        stats.put("draftLectures", allLectures.stream().filter(bg -> !bg.getTrangThai()).count());
        stats.put("totalViews",
                allLectures.stream().mapToInt(bg -> bg.getLuotXem() != null ? bg.getLuotXem() : 0).sum());

        // Recent lectures (last 7 days)
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        long recentLectures = allLectures.stream()
                .filter(bg -> bg.getNgayTao().isAfter(weekAgo))
                .count();
        stats.put("recentLectures", recentLectures);

        return stats;
    }

    // Helper methods
    private BaiGiangTeacherDto.TeacherBaiGiangResponse convertToTeacherResponse(BaiGiang baiGiang) {
        NguoiDung giangVien = nguoiDungRepository.findById(baiGiang.getGiangVienID()).orElse(null);

        return BaiGiangTeacherDto.TeacherBaiGiangResponse.builder()
                .id(baiGiang.getId())
                .maBaiGiang(baiGiang.getMaBaiGiang())
                .tieuDe(baiGiang.getTieuDe())
                .moTa(baiGiang.getMoTa())
                .ngayTao(baiGiang.getNgayTao())
                .ngayCapNhat(baiGiang.getNgayCapNhat())
                .luotXem(baiGiang.getLuotXem())
                .thoiLuong(baiGiang.getThoiLuong())
                .hinhAnh(baiGiang.getHinhAnh())
                .videoURL(baiGiang.getVideoURL())
                .audioURL(baiGiang.getAudioURL())
                .trangThai(baiGiang.getTrangThai())
                .laBaiGiangGoi(baiGiang.getLaBaiGiangGoi())
                .loaiBaiGiang(convertLoaiBaiGiangToInfo(baiGiang.getLoaiBaiGiang()))
                .capDoHSK(convertCapDoHSKToInfo(baiGiang.getCapDoHSK()))
                .chuDe(convertChuDeToInfo(baiGiang.getChuDe()))
                .giangVien(convertGiangVienToInfo(giangVien))
                .build();
    }

    private BaiGiangTeacherDto.TeacherBaiGiangDetailResponse convertToTeacherDetailResponse(BaiGiang baiGiang) {
        NguoiDung giangVien = nguoiDungRepository.findById(baiGiang.getGiangVienID()).orElse(null);

        return BaiGiangTeacherDto.TeacherBaiGiangDetailResponse.builder()
                .id(baiGiang.getId())
                .maBaiGiang(baiGiang.getMaBaiGiang())
                .tieuDe(baiGiang.getTieuDe())
                .moTa(baiGiang.getMoTa())
                .noiDung(baiGiang.getNoiDung())
                .ngayTao(baiGiang.getNgayTao())
                .ngayCapNhat(baiGiang.getNgayCapNhat())
                .luotXem(baiGiang.getLuotXem())
                .thoiLuong(baiGiang.getThoiLuong())
                .hinhAnh(baiGiang.getHinhAnh())
                .videoURL(baiGiang.getVideoURL())
                .audioURL(baiGiang.getAudioURL())
                .trangThai(baiGiang.getTrangThai())
                .laBaiGiangGoi(baiGiang.getLaBaiGiangGoi())
                .loaiBaiGiang(convertLoaiBaiGiangToInfo(baiGiang.getLoaiBaiGiang()))
                .capDoHSK(convertCapDoHSKToInfo(baiGiang.getCapDoHSK()))
                .chuDe(convertChuDeToInfo(baiGiang.getChuDe()))
                .giangVien(convertGiangVienToInfo(giangVien))
                .soBaiTap(0) // TODO: Implement when needed
                .soHocVienDangHoc(0) // TODO: Implement when needed
                .build();
    }

    private BaiGiangTeacherDto.LoaiBaiGiangInfo convertLoaiBaiGiangToInfo(LoaiBaiGiang loaiBaiGiang) {
        if (loaiBaiGiang == null)
            return null;
        return BaiGiangTeacherDto.LoaiBaiGiangInfo.builder()
                .id(loaiBaiGiang.getId())
                .tenLoai(loaiBaiGiang.getTenLoai())
                .moTa(loaiBaiGiang.getMoTa())
                .build();
    }

    private BaiGiangTeacherDto.CapDoHSKInfo convertCapDoHSKToInfo(CapDoHSK capDoHSK) {
        if (capDoHSK == null)
            return null;
        return BaiGiangTeacherDto.CapDoHSKInfo.builder()
                .id(capDoHSK.getId())
                .tenCapDo(capDoHSK.getTenCapDo())
                .moTa(capDoHSK.getMoTa())
                .capDo(capDoHSK.getCapDo())
                .build();
    }

    private BaiGiangTeacherDto.ChuDeInfo convertChuDeToInfo(ChuDe chuDe) {
        if (chuDe == null)
            return null;
        return BaiGiangTeacherDto.ChuDeInfo.builder()
                .id(chuDe.getId())
                .tenChuDe(chuDe.getTenChuDe())
                .moTa(chuDe.getMoTa())
                .hinhAnh(chuDe.getHinhAnh())
                .build();
    }

    private BaiGiangTeacherDto.GiangVienInfo convertGiangVienToInfo(NguoiDung giangVien) {
        if (giangVien == null)
            return null;
        return BaiGiangTeacherDto.GiangVienInfo.builder()
                .id(giangVien.getId())
                .hoTen(giangVien.getHoTen())
                .email(giangVien.getEmail())
                .hinhDaiDien(giangVien.getHinhDaiDien())
                .build();
    }

    private String generateMaBaiGiang() {
        String prefix = "BG";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return prefix + timestamp.substring(timestamp.length() - 6) + randomPart;
    }
}
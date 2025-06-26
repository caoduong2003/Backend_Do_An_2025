package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.TienTrinhDto;
import com.example.tiengtrungapp.model.entity.TienTrinh;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.model.entity.BaiGiang;
import com.example.tiengtrungapp.repository.TienTrinhRepository;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import com.example.tiengtrungapp.repository.BaiGiangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TienTrinhService {

    private final TienTrinhRepository tienTrinhRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final BaiGiangRepository baiGiangRepository;

    /**
     * Tạo tiến trình học tập mới
     */
    @Transactional
    public TienTrinhDto.TienTrinhResponse createTienTrinh(Long hocVienId, TienTrinhDto.CreateTienTrinhRequest request) {
        // Kiểm tra học viên tồn tại
        NguoiDung hocVien = nguoiDungRepository.findById(hocVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên với ID: " + hocVienId));

        // Kiểm tra bài giảng tồn tại
        BaiGiang baiGiang = baiGiangRepository.findById(request.getBaiGiangId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài giảng với ID: " + request.getBaiGiangId()));

        // Kiểm tra tiến trình đã tồn tại
        Optional<TienTrinh> existingTienTrinh = tienTrinhRepository.findByHocVienIdAndBaiGiangId(hocVienId, request.getBaiGiangId());
        if (existingTienTrinh.isPresent()) {
            throw new RuntimeException("Tiến trình học tập đã tồn tại cho bài giảng này");
        }

        // Tạo tiến trình mới
        TienTrinh tienTrinh = TienTrinh.builder()
                .hocVien(hocVien)
                .baiGiang(baiGiang)
                .trangThai(request.getTrangThai())
                .tienDo(request.getTienDo() != null ? request.getTienDo() : 0)
                .thoiGianHoc(request.getThoiGianHoc() != null ? request.getThoiGianHoc() : 0)
                .diemSo(request.getDiemSo())
                .lanHoc(1)
                .ngayBatDau(LocalDateTime.now())
                .ghiChu(request.getGhiChu())
                .build();

        // Nếu hoàn thành, set ngày hoàn thành
        if (request.getTrangThai() == 2) {
            tienTrinh.setNgayHoanThanh(LocalDateTime.now());
        }

        TienTrinh savedTienTrinh = tienTrinhRepository.save(tienTrinh);
        log.info("Đã tạo tiến trình học tập cho học viên ID: {} và bài giảng ID: {}", hocVienId, request.getBaiGiangId());

        return convertToTienTrinhResponse(savedTienTrinh);
    }

    /**
     * Cập nhật tiến trình học tập
     */
    @Transactional
    public TienTrinhDto.TienTrinhResponse updateTienTrinh(Long hocVienId, Long tienTrinhId, TienTrinhDto.UpdateTienTrinhRequest request) {
        TienTrinh tienTrinh = tienTrinhRepository.findById(tienTrinhId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiến trình với ID: " + tienTrinhId));

        // Kiểm tra quyền sở hữu
        if (!tienTrinh.getHocVien().getId().equals(hocVienId)) {
            throw new RuntimeException("Không có quyền cập nhật tiến trình này");
        }

        // Cập nhật thông tin
        if (request.getTrangThai() != null) {
            tienTrinh.setTrangThai(request.getTrangThai());
            
            // Nếu chuyển sang trạng thái hoàn thành
            if (request.getTrangThai() == 2 && tienTrinh.getNgayHoanThanh() == null) {
                tienTrinh.setNgayHoanThanh(LocalDateTime.now());
            }
        }

        if (request.getTienDo() != null) {
            tienTrinh.setTienDo(request.getTienDo());
        }

        if (request.getThoiGianHoc() != null) {
            tienTrinh.setThoiGianHoc(tienTrinh.getThoiGianHoc() + request.getThoiGianHoc());
        }

        if (request.getDiemSo() != null) {
            tienTrinh.setDiemSo(request.getDiemSo());
        }

        if (request.getGhiChu() != null) {
            tienTrinh.setGhiChu(request.getGhiChu());
        }

        // Tăng số lần học
        tienTrinh.setLanHoc(tienTrinh.getLanHoc() + 1);

        TienTrinh updatedTienTrinh = tienTrinhRepository.save(tienTrinh);
        log.info("Đã cập nhật tiến trình học tập ID: {}", tienTrinhId);

        return convertToTienTrinhResponse(updatedTienTrinh);
    }

    /**
     * Lấy tiến trình học tập theo ID
     */
    public TienTrinhDto.TienTrinhResponse getTienTrinhById(Long hocVienId, Long tienTrinhId) {
        TienTrinh tienTrinh = tienTrinhRepository.findById(tienTrinhId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiến trình với ID: " + tienTrinhId));

        // Kiểm tra quyền sở hữu
        if (!tienTrinh.getHocVien().getId().equals(hocVienId)) {
            throw new RuntimeException("Không có quyền xem tiến trình này");
        }

        return convertToTienTrinhResponse(tienTrinh);
    }

    /**
     * Lấy danh sách tiến trình học tập của học viên
     */
    public Page<TienTrinhDto.TienTrinhResponse> getTienTrinhByHocVienId(Long hocVienId, Pageable pageable, TienTrinhDto.TienTrinhFilter filter) {
        Specification<TienTrinh> spec = Specification.where((root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("hocVien").get("id"), hocVienId));

        // Áp dụng các filter
        if (filter != null) {
            if (filter.getTrangThai() != null) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.equal(root.get("trangThai"), filter.getTrangThai()));
            }

            if (filter.getCapDoHSKId() != null) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.equal(root.get("baiGiang").get("capDoHSK").get("id"), filter.getCapDoHSKId()));
            }

            if (filter.getChuDeId() != null) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.equal(root.get("baiGiang").get("chuDe").get("id"), filter.getChuDeId()));
            }

            if (filter.getLoaiBaiGiangId() != null) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.equal(root.get("baiGiang").get("loaiBaiGiang").get("id"), filter.getLoaiBaiGiangId()));
            }

            if (filter.getTuNgay() != null && filter.getDenNgay() != null) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.between(root.get("ngayBatDau"), filter.getTuNgay(), filter.getDenNgay()));
            }

            if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> 
                    criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("baiGiang").get("tieuDe")), 
                            "%" + filter.getKeyword().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("baiGiang").get("maBaiGiang")), 
                            "%" + filter.getKeyword().toLowerCase() + "%")
                    ));
            }
        }

        Page<TienTrinh> tienTrinhPage = tienTrinhRepository.findAll(spec, pageable);
        return tienTrinhPage.map(this::convertToTienTrinhResponse);
    }

    /**
     * Lấy thống kê tiến trình học tập của học viên
     */
    public TienTrinhDto.TienTrinhStatistics getTienTrinhStatistics(Long hocVienId) {
        // Tổng số bài giảng
        long tongSoBaiGiang = baiGiangRepository.countByTrangThai(true);
        
        // Số bài giảng đã học (có tiến trình)
        long soBaiGiangDaHoc = tienTrinhRepository.countByHocVienId(hocVienId);
        
        // Số bài giảng đang học
        long soBaiGiangDangHoc = tienTrinhRepository.countByHocVienIdAndTrangThai(hocVienId, 1);
        
        // Số bài giảng hoàn thành
        long soBaiGiangHoanThanh = tienTrinhRepository.countByHocVienIdAndTrangThai(hocVienId, 2);
        
        // Tiến độ tổng thể
        int tienDoTongThe = tongSoBaiGiang > 0 ? (int) ((soBaiGiangHoanThanh * 100) / tongSoBaiGiang) : 0;
        
        // Tổng thời gian học
        Integer tongThoiGianHoc = tienTrinhRepository.getTotalStudyTimeByHocVienId(hocVienId);
        
        // Điểm trung bình
        Double diemTrungBinh = tienTrinhRepository.getAverageScoreByHocVienId(hocVienId);
        
        // Thống kê theo thời gian
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime monthStart = LocalDate.now().minusDays(30).atStartOfDay();
        
        int soBaiGiangHomNay = tienTrinhRepository.findByHocVienIdAndNgayBatDauBetween(hocVienId, todayStart, LocalDateTime.now()).size();
        int soBaiGiangTuanNay = tienTrinhRepository.findByHocVienIdAndNgayBatDauBetween(hocVienId, weekStart, LocalDateTime.now()).size();
        int soBaiGiangThangNay = tienTrinhRepository.findByHocVienIdAndNgayBatDauBetween(hocVienId, monthStart, LocalDateTime.now()).size();
        
        // TODO: Tính số ngày học liên tiếp (cần logic phức tạp hơn)
        int soNgayHocLienTiep = 0;

        return TienTrinhDto.TienTrinhStatistics.builder()
                .tongSoBaiGiang(tongSoBaiGiang)
                .soBaiGiangDaHoc(soBaiGiangDaHoc)
                .soBaiGiangDangHoc(soBaiGiangDangHoc)
                .soBaiGiangHoanThanh(soBaiGiangHoanThanh)
                .tienDoTongThe(tienDoTongThe)
                .tongThoiGianHoc(tongThoiGianHoc != null ? tongThoiGianHoc : 0)
                .diemTrungBinh(diemTrungBinh != null ? diemTrungBinh : 0.0)
                .soNgayHocLienTiep(soNgayHocLienTiep)
                .soBaiGiangHomNay(soBaiGiangHomNay)
                .soBaiGiangTuanNay(soBaiGiangTuanNay)
                .soBaiGiangThangNay(soBaiGiangThangNay)
                .build();
    }

    /**
     * Lấy tiến trình gần đây nhất
     */
    public List<TienTrinhDto.TienTrinhResponse> getRecentTienTrinh(Long hocVienId, int limit) {
        List<TienTrinh> tienTrinhList = tienTrinhRepository.findRecentProgressByHocVienId(hocVienId);
        return tienTrinhList.stream()
                .limit(limit)
                .map(this::convertToTienTrinhResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tiến trình có điểm cao nhất
     */
    public List<TienTrinhDto.TienTrinhResponse> getTopScores(Long hocVienId, int limit) {
        List<TienTrinh> tienTrinhList = tienTrinhRepository.findTopScoresByHocVienId(hocVienId);
        return tienTrinhList.stream()
                .limit(limit)
                .map(this::convertToTienTrinhResponse)
                .collect(Collectors.toList());
    }

    /**
     * Xóa tiến trình học tập
     */
    @Transactional
    public void deleteTienTrinh(Long hocVienId, Long tienTrinhId) {
        TienTrinh tienTrinh = tienTrinhRepository.findById(tienTrinhId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiến trình với ID: " + tienTrinhId));

        // Kiểm tra quyền sở hữu
        if (!tienTrinh.getHocVien().getId().equals(hocVienId)) {
            throw new RuntimeException("Không có quyền xóa tiến trình này");
        }

        tienTrinhRepository.delete(tienTrinh);
        log.info("Đã xóa tiến trình học tập ID: {}", tienTrinhId);
    }

    /**
     * Chuyển đổi TienTrinh entity sang TienTrinhResponse DTO
     */
    private TienTrinhDto.TienTrinhResponse convertToTienTrinhResponse(TienTrinh tienTrinh) {
        return TienTrinhDto.TienTrinhResponse.builder()
                .id(tienTrinh.getId())
                .hocVienId(tienTrinh.getHocVien().getId())
                .hocVienTen(tienTrinh.getHocVien().getHoTen())
                .baiGiangId(tienTrinh.getBaiGiang().getId())
                .baiGiangTieuDe(tienTrinh.getBaiGiang().getTieuDe())
                .baiGiangMaBaiGiang(tienTrinh.getBaiGiang().getMaBaiGiang())
                .trangThai(tienTrinh.getTrangThai())
                .trangThaiText(TienTrinhDto.getTrangThaiText(tienTrinh.getTrangThai()))
                .tienDo(tienTrinh.getTienDo())
                .thoiGianHoc(tienTrinh.getThoiGianHoc())
                .diemSo(tienTrinh.getDiemSo())
                .lanHoc(tienTrinh.getLanHoc())
                .ngayBatDau(tienTrinh.getNgayBatDau())
                .ngayHoanThanh(tienTrinh.getNgayHoanThanh())
                .ngayCapNhat(tienTrinh.getNgayCapNhat())
                .ghiChu(tienTrinh.getGhiChu())
                .baiGiangHinhAnh(tienTrinh.getBaiGiang().getHinhAnh())
                .baiGiangVideoURL(tienTrinh.getBaiGiang().getVideoURL())
                .baiGiangThoiLuong(tienTrinh.getBaiGiang().getThoiLuong())
                .chuDeTen(tienTrinh.getBaiGiang().getChuDe().getTenChuDe())
                .capDoHSKTen(tienTrinh.getBaiGiang().getCapDoHSK().getTenCapDo())
                .loaiBaiGiangTen(tienTrinh.getBaiGiang().getLoaiBaiGiang().getTenLoai())
                .build();
    }
} 
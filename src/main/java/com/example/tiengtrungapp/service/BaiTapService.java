package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.BaiTapDto;
import com.example.tiengtrungapp.model.dto.CauHoiDto;
import com.example.tiengtrungapp.model.dto.DapAnDto;
import com.example.tiengtrungapp.model.dto.KetQuaBaiTapDto;
import com.example.tiengtrungapp.model.entity.*;
import com.example.tiengtrungapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaiTapService {

    private final BaiTapRepository baiTapRepository;
    private final CauHoiRepository cauHoiRepository;
    private final KetQuaBaiTapRepository ketQuaBaiTapRepository;
    private final ChiTietKetQuaRepository chiTietKetQuaRepository;
    private final NguoiDungRepository nguoiDungRepository;

    /**
     * Lấy danh sách bài tập với filter
     */
    public List<BaiTapDto.BaiTapResponse> getBaiTapList(Long hocVienId, Integer capDoHSKId, Integer chuDeId) {
        log.info("Getting bai tap list for hocVienId: {}, capDoHSK: {}, chuDe: {}", hocVienId, capDoHSKId, chuDeId);

        List<BaiTap> baiTapList;

        if (capDoHSKId != null && chuDeId != null) {
            baiTapList = baiTapRepository.findByCapDoHSKIdAndChuDeIdAndTrangThaiTrue(capDoHSKId, chuDeId);
        } else if (capDoHSKId != null) {
            baiTapList = baiTapRepository.findByCapDoHSKIdAndTrangThaiTrue(capDoHSKId);
        } else if (chuDeId != null) {
            baiTapList = baiTapRepository.findByChuDeIdAndTrangThaiTrue(chuDeId);
        } else {
            baiTapList = baiTapRepository.findAllWithDetails();
        }

        log.info("Found {} bai tap", baiTapList.size());

        return baiTapList.stream()
                .map(baiTap -> convertToBaiTapResponse(baiTap, hocVienId))
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết bài tập để làm bài
     */
    public Optional<BaiTapDto.BaiTapDetailResponse> getBaiTapDetail(Long baiTapId) {
        log.info("Getting bai tap detail for ID: {}", baiTapId);

        Optional<BaiTap> baiTapOpt = baiTapRepository.findByIdWithDetails(baiTapId);

        if (baiTapOpt.isPresent()) {
            BaiTap baiTap = baiTapOpt.get();
            List<CauHoi> cauHoiList = cauHoiRepository.findByBaiTapIdWithDapAn(baiTapId);

            log.info("Found bai tap: {} with {} questions", baiTap.getTieuDe(), cauHoiList.size());

            BaiTapDto.BaiTapDetailResponse response = BaiTapDto.BaiTapDetailResponse.builder()
                    .id(baiTap.getId())
                    .tieuDe(baiTap.getTieuDe())
                    .moTa(baiTap.getMoTa())
                    .thoiGianLam(baiTap.getThoiGianLam())
                    .soCauHoi(baiTap.getSoCauHoi())
                    .diemToiDa(baiTap.getDiemToiDa())
                    .cauHoiList(cauHoiList.stream()
                            .map(this::convertToCauHoiResponse)
                            .collect(Collectors.toList()))
                    .build();

            return Optional.of(response);
        }

        log.warn("Bai tap not found with ID: {}", baiTapId);
        return Optional.empty();
    }

    /**
     * Xử lý làm bài tập
     */
    @Transactional
    public KetQuaBaiTapDto.KetQuaBaiTapResponse lamBaiTap(Long hocVienId, BaiTapDto.LamBaiTapRequest request) {
        log.info("Processing quiz submission for hocVienId: {}, baiTapId: {}", hocVienId, request.getBaiTapId());

        // Kiểm tra bài tập tồn tại
        BaiTap baiTap = baiTapRepository.findById(request.getBaiTapId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập với ID: " + request.getBaiTapId()));

        // Kiểm tra học viên tồn tại
        NguoiDung hocVien = nguoiDungRepository.findById(hocVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên với ID: " + hocVienId));

        // Lấy danh sách câu hỏi
        List<CauHoi> cauHoiList = cauHoiRepository.findByBaiTapIdWithDapAn(request.getBaiTapId());

        // Tính điểm
        int soCauDung = 0;
        float tongDiem = 0;

        for (BaiTapDto.TraLoiRequest traLoi : request.getDanhSachTraLoi()) {
            CauHoi cauHoi = cauHoiList.stream()
                    .filter(c -> c.getId().equals(traLoi.getCauHoiId()))
                    .findFirst()
                    .orElse(null);

            if (cauHoi != null) {
                DapAn dapAnDung = cauHoi.getDapAnList().stream()
                        .filter(DapAn::getLaDapAnDung)
                        .findFirst()
                        .orElse(null);

                if (dapAnDung != null && dapAnDung.getId().equals(traLoi.getDapAnId())) {
                    soCauDung++;
                    tongDiem += cauHoi.getDiemSo();
                }
            }
        }

        log.info("Quiz results: {}/{} correct answers, score: {}", soCauDung, cauHoiList.size(), tongDiem);

        // Lưu kết quả
        KetQuaBaiTap ketQua = KetQuaBaiTap.builder()
                .baiTap(baiTap)
                .hocVien(hocVien)
                .diemSo(tongDiem)
                .thoiGianLam(request.getThoiGianLam())
                .soCauDung(soCauDung)
                .tongSoCau(cauHoiList.size())
                .trangThai(1) // Hoàn thành
                .build();

        ketQua = ketQuaBaiTapRepository.save(ketQua);

        // Lưu chi tiết kết quả
        for (BaiTapDto.TraLoiRequest traLoi : request.getDanhSachTraLoi()) {
            CauHoi cauHoi = cauHoiList.stream()
                    .filter(c -> c.getId().equals(traLoi.getCauHoiId()))
                    .findFirst()
                    .orElse(null);

            if (cauHoi != null) {
                DapAn dapAnDung = cauHoi.getDapAnList().stream()
                        .filter(DapAn::getLaDapAnDung)
                        .findFirst()
                        .orElse(null);

                DapAn dapAnDaChon = cauHoi.getDapAnList().stream()
                        .filter(da -> da.getId().equals(traLoi.getDapAnId()))
                        .findFirst()
                        .orElse(null);

                boolean laDung = dapAnDung != null && dapAnDung.getId().equals(traLoi.getDapAnId());

                ChiTietKetQua chiTiet = ChiTietKetQua.builder()
                        .ketQuaBaiTap(ketQua)
                        .cauHoi(cauHoi)
                        .dapAnDaChon(dapAnDaChon)
                        .laDung(laDung)
                        .build();

                chiTietKetQuaRepository.save(chiTiet);
            }
        }

        log.info("Saved quiz result with ID: {}", ketQua.getId());

        return convertToKetQuaBaiTapResponse(ketQua);
    }

    /**
     * Lấy kết quả bài tập của học viên
     */
    public List<KetQuaBaiTapDto.KetQuaBaiTapResponse> getKetQuaBaiTap(Long hocVienId) {
        log.info("Getting quiz results for hocVienId: {}", hocVienId);

        List<KetQuaBaiTap> ketQuaList = ketQuaBaiTapRepository.findByHocVienIdWithDetails(hocVienId);

        log.info("Found {} quiz results", ketQuaList.size());

        return ketQuaList.stream()
                .map(this::convertToKetQuaBaiTapResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thống kê bài tập của học viên
     */
    public KetQuaBaiTapDto.ThongKeResponse getThongKe(Long hocVienId) {
        log.info("Getting quiz statistics for hocVienId: {}", hocVienId);

        List<KetQuaBaiTap> ketQuaList = ketQuaBaiTapRepository.findByHocVienIdOrderByNgayLamBaiDesc(hocVienId);

        long tongSoBaiTap = baiTapRepository.findByTrangThaiTrue().size();
        long soLanLamBai = ketQuaList.size();

        Double diemTrungBinh = ketQuaBaiTapRepository.getAverageScoreByHocVienId(hocVienId);
        Float diemCaoNhat = ketQuaBaiTapRepository.getMaxScoreByHocVienId(hocVienId);

        Long soCauDungTong = ketQuaList.stream()
                .mapToLong(KetQuaBaiTap::getSoCauDung)
                .sum();

        Long tongSoCauHoi = ketQuaList.stream()
                .mapToLong(KetQuaBaiTap::getTongSoCau)
                .sum();

        Float tiLeDungTong = tongSoCauHoi > 0 ? (soCauDungTong.floatValue() / tongSoCauHoi.floatValue()) * 100 : 0f;

        log.info("Statistics: {} total exercises, {} attempts, avg score: {}", tongSoBaiTap, soLanLamBai,
                diemTrungBinh);

        return KetQuaBaiTapDto.ThongKeResponse.builder()
                .tongSoBaiTap(tongSoBaiTap)
                .soLanLamBai(soLanLamBai)
                .diemTrungBinh(diemTrungBinh != null ? diemTrungBinh.floatValue() : 0f)
                .diemCaoNhat(diemCaoNhat != null ? diemCaoNhat : 0f)
                .soCauDungTong(soCauDungTong)
                .tongSoCauHoi(tongSoCauHoi)
                .tiLeDungTong(tiLeDungTong)
                .build();
    }

    // Helper methods để convert Entity sang DTO
    private BaiTapDto.BaiTapResponse convertToBaiTapResponse(BaiTap baiTap, Long hocVienId) {
        // Lấy điểm cao nhất và số lần làm của học viên
        Optional<KetQuaBaiTap> ketQuaTotNhat = ketQuaBaiTapRepository
                .findTopByHocVienIdAndBaiTapIdOrderByDiemSoDesc(hocVienId, baiTap.getId());

        List<KetQuaBaiTap> danhSachKetQua = ketQuaBaiTapRepository
                .findByHocVienIdAndBaiTapIdOrderByNgayLamBaiDesc(hocVienId, baiTap.getId());

        return BaiTapDto.BaiTapResponse.builder()
                .id(baiTap.getId())
                .tieuDe(baiTap.getTieuDe())
                .moTa(baiTap.getMoTa())
                .baiGiangId(baiTap.getBaiGiang() != null ? baiTap.getBaiGiang().getId() : null)
                .baiGiangTieuDe(baiTap.getBaiGiang() != null ? baiTap.getBaiGiang().getTieuDe() : null)
                .capDoHSKId(baiTap.getCapDoHSK() != null ? baiTap.getCapDoHSK().getId() : null)
                .capDoHSKTen(baiTap.getCapDoHSK() != null ? baiTap.getCapDoHSK().getTenCapDo() : null)
                .chuDeId(baiTap.getChuDe() != null ? baiTap.getChuDe().getId() : null)
                .chuDeTen(baiTap.getChuDe() != null ? baiTap.getChuDe().getTenChuDe() : null)
                .thoiGianLam(baiTap.getThoiGianLam())
                .soCauHoi(baiTap.getSoCauHoi())
                .diemToiDa(baiTap.getDiemToiDa())
                .trangThai(baiTap.getTrangThai())
                .ngayTao(baiTap.getNgayTao())
                .ngayCapNhat(baiTap.getNgayCapNhat())
                .diemCaoNhat(ketQuaTotNhat.map(KetQuaBaiTap::getDiemSo).orElse(null))
                .soLanLam(danhSachKetQua.size())
                .build();
    }

    private CauHoiDto.CauHoiResponse convertToCauHoiResponse(CauHoi cauHoi) {
        return CauHoiDto.CauHoiResponse.builder()
                .id(cauHoi.getId())
                .noiDung(cauHoi.getNoiDung())
                .loaiCauHoi(cauHoi.getLoaiCauHoi())
                .hinhAnh(cauHoi.getHinhAnh())
                .audioURL(cauHoi.getAudioURL())
                .thuTu(cauHoi.getThuTu())
                .diemSo(cauHoi.getDiemSo())
                .dapAnList(cauHoi.getDapAnList().stream()
                        .map(this::convertToDapAnResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private DapAnDto.DapAnResponse convertToDapAnResponse(DapAn dapAn) {
        return DapAnDto.DapAnResponse.builder()
                .id(dapAn.getId())
                .noiDung(dapAn.getNoiDung())
                .thuTu(dapAn.getThuTu())
                // Không trả về laDapAnDung để tránh gian lận
                .build();
    }

    private KetQuaBaiTapDto.KetQuaBaiTapResponse convertToKetQuaBaiTapResponse(KetQuaBaiTap ketQua) {
        float tiLeDung = ketQua.getTongSoCau() > 0
                ? (ketQua.getSoCauDung().floatValue() / ketQua.getTongSoCau().floatValue()) * 100
                : 0f;

        String xepLoai = getXepLoai(tiLeDung);

        return KetQuaBaiTapDto.KetQuaBaiTapResponse.builder()
                .id(ketQua.getId())
                .baiTapId(ketQua.getBaiTap().getId())
                .baiTapTieuDe(ketQua.getBaiTap().getTieuDe())
                .diemSo(ketQua.getDiemSo())
                .diemToiDa(ketQua.getBaiTap().getDiemToiDa())
                .thoiGianLam(ketQua.getThoiGianLam())
                .soCauDung(ketQua.getSoCauDung())
                .tongSoCau(ketQua.getTongSoCau())
                .ngayLamBai(ketQua.getNgayLamBai())
                .trangThai(ketQua.getTrangThai())
                .tiLeDung(tiLeDung)
                .xepLoai(xepLoai)
                .build();
    }

    private String getXepLoai(float tiLeDung) {
        if (tiLeDung >= 90)
            return "Xuất sắc";
        if (tiLeDung >= 80)
            return "Giỏi";
        if (tiLeDung >= 70)
            return "Khá";
        if (tiLeDung >= 60)
            return "Trung bình";
        return "Yếu";
    }

}

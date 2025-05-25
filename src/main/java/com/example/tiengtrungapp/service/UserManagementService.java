package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.UserDto;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import com.example.tiengtrungapp.repository.BaiGiangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

    private final NguoiDungRepository nguoiDungRepository;
    private final BaiGiangRepository baiGiangRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lấy danh sách tất cả người dùng với phân trang và lọc
     */
    public Page<UserDto.UserResponse> getAllUsers(Pageable pageable, Integer vaiTro, String keyword) {
        Specification<NguoiDung> spec = Specification.where(null);

        // Lọc theo vai trò
        if (vaiTro != null) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("vaiTro"), vaiTro));
        }

        // Tìm kiếm theo keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> 
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tenDangNhap")), 
                        "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("hoTen")), 
                        "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), 
                        "%" + keyword.toLowerCase() + "%")
                ));
        }

        Page<NguoiDung> users = nguoiDungRepository.findAll(spec, pageable);
        return users.map(this::convertToUserResponse);
    }

    /**
     * Lấy danh sách giáo viên
     */
    public List<UserDto.UserResponse> getAllTeachers() {
        List<NguoiDung> teachers = nguoiDungRepository.findByVaiTroAndTrangThai(1, true);
        return teachers.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách học viên
     */
    public List<UserDto.UserResponse> getAllStudents() {
        List<NguoiDung> students = nguoiDungRepository.findByVaiTroAndTrangThai(2, true);
        return students.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết của một người dùng
     */
    public UserDto.UserResponse getUserById(Long id) {
        NguoiDung user = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
        return convertToUserResponse(user);
    }

    /**
     * Tạo tài khoản mới
     */
    @Transactional
    public UserDto.UserResponse createUser(UserDto.CreateUserRequest request) {
        // Kiểm tra tên đăng nhập đã tồn tại
        if (nguoiDungRepository.existsByTenDangNhap(request.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Kiểm tra email đã tồn tại
        if (request.getEmail() != null && nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Tạo người dùng mới
        NguoiDung user = NguoiDung.builder()
                .tenDangNhap(request.getTenDangNhap())
                .email(request.getEmail())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .hoTen(request.getHoTen())
                .soDienThoai(request.getSoDienThoai())
                .vaiTro(request.getVaiTro())
                .trinhDoHSK(request.getTrinhDoHSK())
                .hinhDaiDien(request.getHinhDaiDien())
                .ngayTao(LocalDateTime.now())
                .ngayCapNhat(LocalDateTime.now())
                .trangThai(request.getTrangThai())
                .build();

        NguoiDung savedUser = nguoiDungRepository.save(user);
        log.info("Đã tạo tài khoản mới: {}", savedUser.getTenDangNhap());
        
        return convertToUserResponse(savedUser);
    }

    /**
     * Cập nhật thông tin người dùng
     */
    @Transactional
    public UserDto.UserResponse updateUser(Long id, UserDto.UpdateUserRequest request) {
        NguoiDung user = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        // Kiểm tra email đã tồn tại (nếu thay đổi)
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (nguoiDungRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }
        }

        // Cập nhật thông tin
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getHoTen() != null) {
            user.setHoTen(request.getHoTen());
        }
        if (request.getSoDienThoai() != null) {
            user.setSoDienThoai(request.getSoDienThoai());
        }
        if (request.getVaiTro() != null) {
            user.setVaiTro(request.getVaiTro());
        }
        if (request.getTrinhDoHSK() != null) {
            user.setTrinhDoHSK(request.getTrinhDoHSK());
        }
        if (request.getHinhDaiDien() != null) {
            user.setHinhDaiDien(request.getHinhDaiDien());
        }
        if (request.getTrangThai() != null) {
            user.setTrangThai(request.getTrangThai());
        }

        user.setNgayCapNhat(LocalDateTime.now());
        NguoiDung updatedUser = nguoiDungRepository.save(user);
        
        log.info("Đã cập nhật thông tin người dùng ID: {}", id);
        return convertToUserResponse(updatedUser);
    }

    /**
     * Thay đổi trạng thái người dùng
     */
    @Transactional
    public UserDto.UserResponse changeUserStatus(Long id, Boolean trangThai) {
        NguoiDung user = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        user.setTrangThai(trangThai);
        user.setNgayCapNhat(LocalDateTime.now());
        NguoiDung updatedUser = nguoiDungRepository.save(user);
        
        log.info("Đã thay đổi trạng thái người dùng ID: {} thành: {}", id, trangThai);
        return convertToUserResponse(updatedUser);
    }

    /**
     * Đặt lại mật khẩu cho người dùng
     */
    @Transactional
    public String resetUserPassword(Long id) {
        NguoiDung user = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        // Tạo mật khẩu ngẫu nhiên
        String newPassword = generateRandomPassword();
        user.setMatKhau(passwordEncoder.encode(newPassword));
        user.setNgayCapNhat(LocalDateTime.now());
        
        nguoiDungRepository.save(user);
        log.info("Đã đặt lại mật khẩu cho người dùng ID: {}", id);
        
        return newPassword;
    }

    /**
     * Xóa người dùng (soft delete)
     */
    @Transactional
    public void deleteUser(Long id) {
        NguoiDung user = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        // Soft delete - chỉ thay đổi trạng thái
        user.setTrangThai(false);
        user.setNgayCapNhat(LocalDateTime.now());
        nguoiDungRepository.save(user);
        
        log.info("Đã xóa (vô hiệu hóa) người dùng ID: {}", id);
    }

    /**
     * Lấy thống kê người dùng
     */
    public UserDto.UserStatistics getUserStatistics() {
        long tongSoNguoiDung = nguoiDungRepository.count();
        long soAdmin = nguoiDungRepository.countByVaiTro(0);
        long soGiangVien = nguoiDungRepository.countByVaiTro(1);
        long soHocVien = nguoiDungRepository.countByVaiTro(2);
        long soNguoiDungHoatDong = nguoiDungRepository.countByTrangThai(true);
        long soNguoiDungBiKhoa = nguoiDungRepository.countByTrangThai(false);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();

        long soNguoiDungMoi7Ngay = nguoiDungRepository.countByNgayTaoAfter(sevenDaysAgo);
        long soNguoiDungMoi30Ngay = nguoiDungRepository.countByNgayTaoAfter(thirtyDaysAgo);
        long soNguoiDungDangNhapHomNay = nguoiDungRepository.countByLanDangNhapCuoiAfter(todayStart);
        long soNguoiDungDangNhap7Ngay = nguoiDungRepository.countByLanDangNhapCuoiAfter(sevenDaysAgo);

        return UserDto.UserStatistics.builder()
                .tongSoNguoiDung(tongSoNguoiDung)
                .soAdmin(soAdmin)
                .soGiangVien(soGiangVien)
                .soHocVien(soHocVien)
                .soNguoiDungHoatDong(soNguoiDungHoatDong)
                .soNguoiDungBiKhoa(soNguoiDungBiKhoa)
                .soNguoiDungMoi7Ngay(soNguoiDungMoi7Ngay)
                .soNguoiDungMoi30Ngay(soNguoiDungMoi30Ngay)
                .soNguoiDungDangNhapHomNay(soNguoiDungDangNhapHomNay)
                .soNguoiDungDangNhap7Ngay(soNguoiDungDangNhap7Ngay)
                .build();
    }

    /**
     * Tìm kiếm người dùng
     */
    public List<UserDto.UserResponse> searchUsers(String keyword, Integer vaiTro) {
        List<NguoiDung> users;
        
        if (vaiTro != null) {
            users = nguoiDungRepository.findByVaiTroAndKeyword(vaiTro, keyword.toLowerCase());
        } else {
            users = nguoiDungRepository.findByKeyword(keyword.toLowerCase());
        }
        
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Xuất danh sách người dùng ra CSV
     */
    public String exportUsersToCSV(Integer vaiTro) {
        List<NguoiDung> users;
        
        if (vaiTro != null) {
            users = nguoiDungRepository.findByVaiTro(vaiTro);
        } else {
            users = nguoiDungRepository.findAll();
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Tên đăng nhập,Email,Họ tên,Số điện thoại,Vai trò,Trình độ HSK,Ngày tạo,Trạng thái\n");
        
        for (NguoiDung user : users) {
            csvBuilder.append(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                user.getId(),
                user.getTenDangNhap(),
                user.getEmail() != null ? user.getEmail() : "",
                user.getHoTen() != null ? user.getHoTen() : "",
                user.getSoDienThoai() != null ? user.getSoDienThoai() : "",
                UserDto.getVaiTroText(user.getVaiTro()),
                user.getTrinhDoHSK() != null ? user.getTrinhDoHSK().toString() : "",
                user.getNgayTao() != null ? user.getNgayTao().toString() : "",
                UserDto.getTrangThaiText(user.getTrangThai())
            ));
        }
        
        return csvBuilder.toString();
    }

    /**
     * Chuyển đổi NguoiDung entity sang UserResponse DTO
     */
    private UserDto.UserResponse convertToUserResponse(NguoiDung user) {
        UserDto.UserResponse.UserResponseBuilder builder = UserDto.UserResponse.builder()
                .id(user.getId())
                .tenDangNhap(user.getTenDangNhap())
                .email(user.getEmail())
                .hoTen(user.getHoTen())
                .soDienThoai(user.getSoDienThoai())
                .vaiTro(user.getVaiTro())
                .vaiTroText(UserDto.getVaiTroText(user.getVaiTro()))
                .trinhDoHSK(user.getTrinhDoHSK())
                .hinhDaiDien(user.getHinhDaiDien())
                .ngayTao(user.getNgayTao())
                .ngayCapNhat(user.getNgayCapNhat())
                .lanDangNhapCuoi(user.getLanDangNhapCuoi())
                .trangThai(user.getTrangThai())
                .trangThaiText(UserDto.getTrangThaiText(user.getTrangThai()));

        // Thêm thông tin thống kê cho giáo viên
        if (user.getVaiTro() == 1) {
            int soBaiGiangDaTao = baiGiangRepository.countByGiangVienID(user.getId());
            builder.soBaiGiangDaTao(soBaiGiangDaTao);
        }

        // Thêm thông tin thống kê cho học viên
        if (user.getVaiTro() == 2) {
            // TODO: Implement logic để tính số bài giảng đã học và tiến độ
            // Cần có repository cho TienTrinh để tính toán
            builder.soBaiGiangDaHoc(0)
                   .tienDoHocTap(0);
        }

        return builder.build();
    }

    /**
     * Tạo mật khẩu ngẫu nhiên
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}
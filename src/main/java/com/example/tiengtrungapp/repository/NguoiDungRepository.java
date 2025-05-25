// Thêm các method sau vào NguoiDungRepository.java

package com.example.tiengtrungapp.repository;

import com.example.tiengtrungapp.model.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long>, JpaSpecificationExecutor<NguoiDung> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    Optional<NguoiDung> findByEmail(String email);

    Boolean existsByTenDangNhap(String tenDangNhap);

    Boolean existsByEmail(String email);

    // Các method mới cho user management
    List<NguoiDung> findByVaiTro(Integer vaiTro);
    
    List<NguoiDung> findByVaiTroAndTrangThai(Integer vaiTro, Boolean trangThai);
    
    List<NguoiDung> findByTrangThai(Boolean trangThai);
    
    // Thống kê
    long countByVaiTro(Integer vaiTro);
    
    long countByTrangThai(Boolean trangThai);
    
    long countByNgayTaoAfter(LocalDateTime ngayTao);
    
    long countByLanDangNhapCuoiAfter(LocalDateTime lanDangNhapCuoi);
    
    // Tìm kiếm
    @Query("SELECT n FROM NguoiDung n WHERE " +
           "LOWER(n.tenDangNhap) LIKE %:keyword% OR " +
           "LOWER(n.hoTen) LIKE %:keyword% OR " +
           "LOWER(n.email) LIKE %:keyword%")
    List<NguoiDung> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT n FROM NguoiDung n WHERE n.vaiTro = :vaiTro AND (" +
           "LOWER(n.tenDangNhap) LIKE %:keyword% OR " +
           "LOWER(n.hoTen) LIKE %:keyword% OR " +
           "LOWER(n.email) LIKE %:keyword%)")
    List<NguoiDung> findByVaiTroAndKeyword(@Param("vaiTro") Integer vaiTro, @Param("keyword") String keyword);
    
    // Tìm người dùng theo khoảng thời gian
    List<NguoiDung> findByNgayTaoBetween(LocalDateTime start, LocalDateTime end);
    
    List<NguoiDung> findByLanDangNhapCuoiBetween(LocalDateTime start, LocalDateTime end);
    
    // Tìm người dùng theo trình độ HSK
    List<NguoiDung> findByTrinhDoHSK(Integer trinhDoHSK);
    
    // Tìm người dùng chưa từng đăng nhập
    List<NguoiDung> findByLanDangNhapCuoiIsNull();
    
    // Tìm người dùng không hoạt động trong X ngày
    @Query("SELECT n FROM NguoiDung n WHERE n.lanDangNhapCuoi < :ngayGioiHan OR n.lanDangNhapCuoi IS NULL")
    List<NguoiDung> findInactiveUsers(@Param("ngayGioiHan") LocalDateTime ngayGioiHan);
}
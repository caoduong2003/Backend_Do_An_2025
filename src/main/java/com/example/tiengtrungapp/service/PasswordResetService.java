package com.example.tiengtrungapp.service;

import com.example.tiengtrungapp.model.dto.ForgotPasswordRequest;
import com.example.tiengtrungapp.model.dto.ResetPasswordRequest;
import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.model.entity.PasswordResetToken;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import com.example.tiengtrungapp.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // Kiểm tra email có tồn tại không
        if (!nguoiDungRepository.existsByEmail(request.getEmail())) {
            // Không thông báo lỗi để tránh email enumeration attack
            return;
        }

        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(request.getEmail()).get();

        // Xóa token cũ nếu có
        tokenRepository.deleteByNguoiDungId(nguoiDung.getId());

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .nguoiDung(nguoiDung)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();
        tokenRepository.save(resetToken);

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(nguoiDung.getEmail());
        message.setSubject("Yêu cầu đặt lại mật khẩu");
        message.setText("Mã xác nhận đặt lại mật khẩu của bạn là: " + token +
                "\n\nVui lòng nhập mã này vào ứng dụng để đặt lại mật khẩu." +
                "\nMã này sẽ hết hạn sau 24 giờ.");
        mailSender.send(message);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));

        if (token.isUsed()) {
            throw new RuntimeException("Token đã được sử dụng");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn");
        }

        NguoiDung nguoiDung = token.getNguoiDung();
        nguoiDung.setMatKhau(passwordEncoder.encode(request.getNewPassword()));
        nguoiDungRepository.save(nguoiDung);

        token.setUsed(true);
        tokenRepository.save(token);
    }
}
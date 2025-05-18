package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.dto.ForgotPasswordRequest;
import com.example.tiengtrungapp.model.dto.ResetPasswordRequest;
import com.example.tiengtrungapp.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request);
        return ResponseEntity.ok().body("Email đặt lại mật khẩu đã được gửi");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok().body("Mật khẩu đã được đặt lại thành công");
    }
}
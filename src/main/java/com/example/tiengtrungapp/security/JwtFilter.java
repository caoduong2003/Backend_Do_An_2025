package com.example.tiengtrungapp.security;

import com.example.tiengtrungapp.model.entity.NguoiDung;
import com.example.tiengtrungapp.repository.NguoiDungRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);

                // Lấy thông tin người dùng từ database
                NguoiDung user = nguoiDungRepository.findByTenDangNhap(username).orElse(null);
                
                if (user != null && user.getTrangThai()) { // Kiểm tra trạng thái hoạt động
                    // Tạo authorities dựa trên vai trò
                    List<SimpleGrantedAuthority> authorities = getAuthorities(user.getVaiTro());

                    // Tạo authentication object
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Không thể xác thực: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private List<SimpleGrantedAuthority> getAuthorities(Integer vaiTro) {
        if (vaiTro == null) {
            return Collections.emptyList();
        }

        switch (vaiTro) {
            case 0: // Admin
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case 1: // Giáo viên
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER"));
            case 2: // Học viên
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"));
            default:
                return Collections.emptyList();
        }
    }
}
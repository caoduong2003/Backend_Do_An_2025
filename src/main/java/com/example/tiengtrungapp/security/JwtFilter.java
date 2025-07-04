// JwtFilter.java - SỬA ĐỂ BYPASS PUBLIC ENDPOINTS

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

    // QUAN TRỌNG: Danh sách các endpoint public cần bypass
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/",
            "/api/files/",
            "/api/baigiang/",
            "/api/tuvung/",
            "/api/translation/",
            "/api/chude/",
            "/api/capdohsk/",
            "/api/loaibaigiang/",
            "/api/tien-trinh/",
            "/api/media/",
            "/api/profile/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // QUAN TRỌNG: Bypass JWT filter cho public endpoints
        if (isPublicEndpoint(requestURI)) {
            logger.info("✅ BYPASSING JWT filter for public endpoint: " + method + " " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("🔒 Processing JWT for endpoint: " + method + " " + requestURI);

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                logger.info("✅ Valid JWT found for user: " + username);

                // Lấy thông tin người dùng từ database
                NguoiDung user = nguoiDungRepository.findByTenDangNhap(username).orElse(null);

                if (user != null && user.getTrangThai()) { // Kiểm tra trạng thái hoạt động
                    // Tạo authorities dựa trên vai trò
                    List<SimpleGrantedAuthority> authorities = getAuthorities(user.getVaiTro());

                    // Tạo authentication object
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("✅ Authentication set for user: " + username + " with authorities: " + authorities);
                } else {
                    logger.warn("❌ User not found or inactive: " + username);
                }
            } else {
                logger.warn("❌ No valid JWT found for endpoint: " + method + " " + requestURI);
            }
        } catch (Exception e) {
            logger.error("❌ Error during JWT authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem endpoint có phải là public không
     */
    private boolean isPublicEndpoint(String requestURI) {
        boolean isPublic = PUBLIC_ENDPOINTS.stream()
                .anyMatch(endpoint -> requestURI.startsWith(endpoint));

        if (!isPublic) {
            logger.warn("🔍 Endpoint NOT in public list: " + requestURI);
            logger.warn("🔍 Available public endpoints: " + PUBLIC_ENDPOINTS);
        }

        return isPublic;
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
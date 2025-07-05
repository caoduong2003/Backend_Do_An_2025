package com.example.tiengtrungapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.tiengtrungapp.security.JwtFilter;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép tất cả origins
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // Thêm cụ thể các origins có thể gây vấn đề
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:8080",
                "http://localhost:8081",
                "http://192.168.1.*:*", // Local network
                "http://10.0.2.2:8080", // Android emulator
                "capacitor://localhost",
                "ionic://localhost",
                "http://127.0.0.1:*",
                "https://localhost:*"));

        configuration.setAllowedMethods(Arrays.asList("*")); // Cho phép tất cả methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả headers
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - hoàn toàn public
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Quan trọng cho CORS
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/files/**").permitAll()
                        .requestMatchers("/api/baigiang/**").permitAll()
                        .requestMatchers("/api/tuvung/**").permitAll()
                        .requestMatchers("/api/translation/**").permitAll()
                        .requestMatchers("/api/chude/**").permitAll()
                        .requestMatchers("/api/capdohsk/**").permitAll()
                        .requestMatchers("/api/loaibaigiang/**").permitAll()
                        .requestMatchers("/api/tien-trinh/**").permitAll()
                        .requestMatchers("/api/media/**").permitAll()
                        .requestMatchers("/api/profile/**").permitAll()
                        .requestMatchers("/videos/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/maucau/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/bai-tap/**").permitAll()
                        .requestMatchers("/api/thong-ke/**").permitAll()
                        .requestMatchers("/api/ket-qua-bai-tap/**").permitAll()
                        .requestMatchers("/api/guest/**").permitAll()
                        .requestMatchers("/api/guest/baigiang/**").permitAll()
                        .requestMatchers("/api/guest/tuvung/**").permitAll()
                        .requestMatchers("/api/guest/chude/**").permitAll()
                        .requestMatchers("/api/guest/capdohsk/**").permitAll()
                        .requestMatchers("/api/guest/loaibaigiang/**").permitAll()
                        .requestMatchers("/api/guest/stats/**").permitAll()

                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Các endpoint khác cần authentication
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
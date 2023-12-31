package com.example.twogether.common.config;

import com.example.twogether.common.jwt.JwtUtil;
import com.example.twogether.common.redis.RedisRefreshToken;
import com.example.twogether.common.security.JwtAuthenticationFilter;
import com.example.twogether.common.security.JwtAuthorizationFilter;
import com.example.twogether.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisRefreshToken redisRefreshToken;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, redisRefreshToken);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, redisRefreshToken, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);

        // Session 방식 -> JWT 방식 설정 변경
        http.sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorizeHttpRequests ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/views/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/social/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/boards/**").permitAll()
                .requestMatchers("/api/decks/**").permitAll()
                .requestMatchers("api/cards/**").permitAll()
                .requestMatchers("/api/labels/**").permitAll()
                .requestMatchers("/api/alarms/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                .anyRequest().authenticated()
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

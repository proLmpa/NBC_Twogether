package com.example.twogether.common.config;

import com.example.twogether.common.jwt.JwtUtil;
import com.example.twogether.common.security.JwtAuthenticationFilter;
import com.example.twogether.common.security.JwtAuthorizationFilter;
import com.example.twogether.common.security.UserDetailsServiceImpl;
import com.example.twogether.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
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
                .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                .requestMatchers("/api/boards/**").permitAll()
                .requestMatchers("/api/decks/**").permitAll()
                .requestMatchers("api/cards/**").permitAll()
                .requestMatchers("/api/labels/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // static 아래 정적파일 사용
                .anyRequest().permitAll()//.authenticated()//.permitAll() //.authenticated()// 현재 html 사용할 때 permitAll로 풀어야 함
        );
        //http.formLogin(AbstractHttpConfigurer::disable);
        //http.oauth2Login(Customizer.withDefaults()).userInfoEndpoint().userService(customOAuth2UserService);
        //http.oauth2Login(Customizer.withDefaults()).userInfoEndpoint().userService(customOAuth2UserService);
//        http.oauth2Login(Customizer.withDefaults()).userInfoEndpoint(Customizer.withDefaults());
        //http.userInfoEndpoint(Customizer.withDefaults());
        http.oauth2Login(Customizer.withDefaults());
        //http.userDetailsService((UserDetailsService) customOAuth2UserService);

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

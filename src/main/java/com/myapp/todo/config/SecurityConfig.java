package com.myapp.todo.config;

import com.myapp.todo.oauth.CustomOAuth2UserService;
import com.myapp.todo.oauth.OAuth2LoginFailureHandler;
import com.myapp.todo.oauth.OAuth2LoginSuccessHandler;
import com.myapp.todo.oauth.OAuth2LogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF(Cross-Site Request Forgery) 보호를 비활성화
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/profile", "/todo/**").permitAll() // 해당 URL 패턴들은 모든 사용자가 접근 가능
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 가능
                )// 요청 URL에 따른 권한을 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/todo/user/logout"))
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(oAuth2LogoutHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService) // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                        )
                );
        return http.build();
    }
}
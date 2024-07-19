package com.myapp.todo.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class OAuth2LogoutHandler implements LogoutHandler {

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    private final String logoutURL = "https://kapi.kakao.com/v1/user/logout";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 로직 시작");
        HttpSession session = request.getSession();


        // 액세스 토큰 처리 (로그아웃 요청)
        String accessToken = (String) session.getAttribute("access_token");
        log.info(accessToken);
        if (accessToken != null && !accessToken.isEmpty()) {
            try {
                URL url = new URL(logoutURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty(AUTHORIZATION, BEARER_PREFIX + accessToken);

                conn.disconnect();
                log.info("카카오 로그아웃");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            session.removeAttribute("access_token");
            session.removeAttribute("user");
        } else {
            log.info("액세스 토큰 없음");
        }
        session.invalidate();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.info("로그아웃 완료");
    }

}
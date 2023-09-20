package com.collectpop.config;

import com.collectpop.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserSuccessHandler implements AuthenticationSuccessHandler {



    //카카오 로그인 처리 시큐리티에 추가하기 위한 기능
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 로그인 성공 후 원하는 로직을 여기에 작성합니다.

        // 예시: 사용자 이름을 로그로 출력
        String username = authentication.getName();
        log.info("success user:  {} ", username);


        // 로그인 후 원하는 페이지로 리다이렉트
        response.sendRedirect("/collectpop/");

    }
}
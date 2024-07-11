package com.twentythree.peech.auth.interceptor;

import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            final String BEARER = "Bearer ";

            String token = request.getHeader("Authorization");

            if (token.isEmpty()) {
                throw new UserAlreadyExistException("로그인을 다시 해주세요");
            }

            String credential;

            if (token.startsWith(BEARER)) {
                credential = token.substring(BEARER.length());
            } else {
                throw new IllegalArgumentException("token의 type이 올바르지 않습니다.");
            }

            if (credential.equals("x")) {
                throw new UserAlreadyExistException("token이 없습니다.");
            }

            Long userId = Long.parseLong(jwtUtils.parseJWT(credential).getPayload().get("userId").toString());

            if (userId == null) {
                throw new IllegalArgumentException("가입이 안된 유저 입니다.");
            }

            request.setAttribute("userId", userId);
            return true;
        } catch (JwtException e) {
            throw e;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

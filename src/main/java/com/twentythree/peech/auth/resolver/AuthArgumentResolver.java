package com.twentythree.peech.auth.resolver;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserIdDTO.class) && parameter.hasParameterAnnotation(LoginUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Cookie[] cookies = request.getCookies();
        log.info("1");
        if (cookies == null) {
            throw new IllegalArgumentException("로그인을 다시 해주세요");
        }
        log.info("{}", cookies);

        Long userId = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userId")) {
                userId = Long.parseLong(cookie.getValue());
            }
        }

        if (userId == null) {throw new IllegalArgumentException("cookie의 userId가 잘 못 되었습니다");}

        return new UserIdDTO(userId);
    }
}

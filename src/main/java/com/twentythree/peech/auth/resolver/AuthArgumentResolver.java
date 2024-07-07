package com.twentythree.peech.auth.resolver;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 시작");
        return UserIdDTO.class.isAssignableFrom(parameter.getParameterType()) && parameter.hasParameterAnnotation(LoginUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

            String token = request.getHeader("Authorization");

            if (token.isEmpty()) {
                throw new UserAlreadyExistException("로그인을 다시 해주세요");
            }

            Long userId = Long.parseLong(jwtUtils.parseJWT(token).getPayload().get("userId").toString());

            if (userId == null) {
                throw new IllegalArgumentException("cookie의 userId가 잘 못 되었습니다");
            }

            return new UserIdDTO(userId);
        } catch (JwtException e) {
            throw e;
        }
    }
}

package com.twentythree.peech.security.jwt;


import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.value.SignUpFinished;
import com.twentythree.peech.user.value.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
@RequiredArgsConstructor
public class JWTUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public JWTUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long userId = Long.parseLong(username);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. userId: " + userId));

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        // http 메서드와 uri 뽑아오기
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();

        if (userEntity.getUserStatus() == UserStatus.DELETE) {
            throw new JWTAuthenticationException(LoginExceptionCode.NOT_EXIST_USER);
        }

        // User가 Pending이면 에러 발생 단, 요청 request가 PATCH /api/v1/users/{userId} 일 경우에는 에러 발생하지 않음
        if (userEntity.getSignUpFinished() == SignUpFinished.PENDING
                && !httpMethod.equals("PATCH") && !uri.contains("/api/v1/users/" + userId)) {
            throw new JWTAuthenticationException(LoginExceptionCode.SIGNUP_FINISHED_NOT_YET);
        }
        return JWTUserDetails.create(userEntity);
    }
}

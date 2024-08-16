package com.twentythree.peech.security.jwt;


import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.value.SignUpFinished;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public JWTUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long userId = Long.parseLong(username);

        UserEntity userEntity = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. userId: " + userId));
        // User가 Pending이면 에러 발생
        if (userEntity.getSignUpFinished() == SignUpFinished.PENDING) {
            throw new JWTAuthenticationException(LoginExceptionCode.SIGNUP_FINISHED_NOT_YET);
        }
        return JWTUserDetails.create(userEntity);
    }
}

package com.twentythree.peech.user.validator;

import com.twentythree.peech.user.dto.KakaoAccount;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.value.SignUpFinished;
import com.twentythree.peech.user.value.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class UserValidatorImpl implements UserValidator {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean NickNameIsNotDuplicated(String nickName) {

        Optional<UserEntity> foundNickName = userRepository.findByNickName(nickName);

        return foundNickName.isEmpty();
    }

    @Override
    public boolean existUserByEmail(String email) {

        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean notExistUserByEmail(String email) {
        return !existUserByEmail(email);
    }

    @Override
    public boolean kakaoEmailValid(KakaoAccount kakaoAccount) {
        String email = kakaoAccount.getEmail();
        boolean emailVerified = kakaoAccount.isEmailVerified();
        boolean emailValid = kakaoAccount.isEmailValid();

        if (emailVerified && emailValid) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean singedUpFinishedUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("알 수 없는 에러가 발생했습니다."));

        if (userEntity.getSignUpFinished() == SignUpFinished.FINISHED) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean userIsNotDeleted(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("알 수 없는 에러가 발생했습니다."));

        return userEntity.getUserStatus() == UserStatus.ACTIVE;
    }
}

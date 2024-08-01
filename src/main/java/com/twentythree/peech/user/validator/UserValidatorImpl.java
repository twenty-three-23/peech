package com.twentythree.peech.user.validator;

import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
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

}

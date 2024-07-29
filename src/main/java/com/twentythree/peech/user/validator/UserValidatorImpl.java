package com.twentythree.peech.user.validator;

import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean NickNameIsNotDuplicated(String nickName) {

        Optional<UserEntity> foundNickName = userRepository.findByNickName(nickName);

        return foundNickName.isEmpty();
    }
}

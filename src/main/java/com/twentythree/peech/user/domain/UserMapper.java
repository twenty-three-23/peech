package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.value.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserMapper {

    private final UserRepository userRepository;
    private final UsageTimeRepository usageTimeRepository;

    @Transactional
    public Long saveUserDomain(UserDomain userDomain) {
        UserEntity userEntity = UserEntity.of(userDomain.getUserId(), null, userDomain.getAuthorizationServer(), userDomain.getFirstName(), userDomain.getLastName(), userDomain.getBirth(), userDomain.getGender(), userDomain.getEmail(), userDomain.getNickName(), userDomain.getSignUpFinished());

        UsageTimeEntity usageTimeEntity = UsageTimeEntity.of(userDomain.getUsageTime(), userDomain.getRemainingTime(), userEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        usageTimeRepository.save(usageTimeEntity);

        return savedUser.getId();
    }

    @Transactional
    public UserEntity deleteUserDomain(UserDomain userDomain) {
        UserEntity userEntity = userRepository.findById(userDomain.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.changeUserStatusToDelete();
        return userEntity;
    }
}

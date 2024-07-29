package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserMapper {

    private final UserRepository userRepository;
    private final UsageTimeRepository usageTimeRepository;

    @Transactional
    public Long saveUserDomain(UserDomain userDomain) {
        UserEntity userEntity = UserEntity.of(userDomain.getUserId(), null, userDomain.getAuthorizationIdentifier(), userDomain.getFirstName(), userDomain.getLastName(), userDomain.getBirth(), userDomain.getGender(), userDomain.getEmail(), userDomain.getNickName());

        UsageTimeEntity usageTimeEntity = UsageTimeEntity.of(userDomain.getUsageTime(), userDomain.getRemainingTime(), userEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        usageTimeRepository.save(usageTimeEntity);

        return savedUser.getId();
    }
}

package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFetcher {

    private final UserRepository userRepository;
    private final UsageTimeRepository usageTimeRepository;

    public UserDomain fetchUser(Long userId){

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘 못된 유저 정보입니다."));
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("잘 못된 유저 정보 입니다."));

        return UserDomain.of(user.getId(), user.getAuthorizationIdentifier(), user.getFirstName(), user.getLastName(), user.getBirth(), user.getGender(), user.getEmail(), user.getNickName(), user.getRole(), user.getUserStatus(), usageTime.getUsageTimeId(), usageTime.getRemainingTime(), user.getDeleteAt());
    }
}

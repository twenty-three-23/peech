package com.twentythree.peech.user.domain;

import com.twentythree.peech.common.exception.Unauthorized;
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

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Unauthorized("잘 못된 유저 정보입니다."));
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).orElseThrow(() -> new Unauthorized("잘 못된 유저 정보 입니다."));

        return UserDomain.of(user.getId(), user.getAuthorizationServer(), user.getFirstName(), user.getLastName(), user.getBirth(), user.getGender(), user.getEmail(), user.getNickName(), user.getRole(), user.getUserStatus(), usageTime.getUsageTimeId(), usageTime.getRemainingTime(), user.getDeleteAt(), user.getSignUpFinished());
    }

    // Q 2. 이게 뭐지...
//    public UserDomain fetchUserNickNameByUserId(Long userId){
//        String nickName = userRepository.findNickNameById(userId).orElseThrow(() -> new Unauthorized("잘 못된 유저 id 입니다.: " + userId));
//        UserDomain userDomain = UserDomain.of(userId, null, null, null, null, null, null, nickName, null, null, null, null, null, null);
//        return userDomain;
//    }
    // Q 2
}

package com.twentythree.peech.user.service;

import com.twentythree.peech.user.domain.UserEntity;
import com.twentythree.peech.user.dto.request.CreateUserRequestDTO;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createUser(String deviceId) {

        if (validateDeviceId(deviceId)) {
            UserEntity userEntity = UserEntity.ofNoLogin(deviceId);

            return userRepository.save(userEntity).getId();
        } else {
            throw new IllegalArgumentException("이미 가입된 유저 입니다");
        }
    }

    // DeviceId가 존재 하면 false
    private boolean validateDeviceId(String deviceId) {
        UserEntity user = userRepository.findByDeviceId(deviceId);
        if (user != null) {
            return false;
        } else {
            return true;
        }
    }
}

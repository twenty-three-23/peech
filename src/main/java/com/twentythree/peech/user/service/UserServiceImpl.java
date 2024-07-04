package com.twentythree.peech.user.service;

import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.user.domain.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    @Override
    @Transactional
    public String createUser(String deviceId) {

        if (validateDeviceId(deviceId)) {
            UserEntity user = UserEntity.ofNoLogin(deviceId);

            Long userId = userRepository.save(user).getId();
            String jwt = jwtUtils.createJWT(userId);
            return jwt;
        } else {
            throw new UserAlreadyExistException("이미 가입된 유저 입니다");
        }
    }

    @Override
    public String reIssuanceUserToken(String deviceId) {
        if (!validateDeviceId(deviceId)) {
            UserEntity user = userRepository.findByDeviceId(deviceId);
            Long userId = user.getId();

            String jwt = jwtUtils.createJWT(userId);
            return jwt;
        } else {
            throw new IllegalArgumentException("가입된적 없는 유저입니다.");
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

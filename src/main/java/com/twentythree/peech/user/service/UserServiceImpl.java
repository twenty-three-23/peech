package com.twentythree.peech.user.service;

import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.AuthorizationIdentifier;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.UserRole;
import com.twentythree.peech.user.domain.UserCreator;
import com.twentythree.peech.user.domain.UserDomain;
import com.twentythree.peech.user.domain.UserMapper;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsageTimeRepository usageTimeRepository;
    private final UserMapper userMapper;
    private final UserCreator userCreator;
    private final JWTUtils jwtUtils;

    @Override
    @Transactional
    public String createUserByDeviceId(String deviceId) {

        if (validateDeviceId(deviceId)) {
            UserEntity user = UserEntity.ofNoLogin(deviceId);

            Long userId = userRepository.save(user).getId();
            String jwt = jwtUtils.createJWT(userId);

            UsageTimeEntity usageTime = new UsageTimeEntity(user);
            usageTimeRepository.save(usageTime);

            return jwt;
        } else {
            throw new UserAlreadyExistException("이미 가입된 유저 입니다");
        }
    }

    @Override
    @Transactional
    public AccessAndRefreshToken createUserBySocial( String socialId, AuthorizationServer authorizationServer, String firstName, String lastName, LocalDate birth, String email, UserGender gender, String nickName) {

        AuthorizationIdentifier authorizationIdentifier = AuthorizationIdentifier.of(socialId, authorizationServer);

        UserDomain userDomain = userCreator.createUser(authorizationIdentifier, firstName, lastName, birth, email, gender, nickName);
        Long userId = userMapper.saveUserDomain(userDomain);
        UserRole userRole = userDomain.getRole();

        String accessToken = jwtUtils.createAccessToken(userId, userRole);
        String refreshToken = jwtUtils.createRefreshToken(userId, userRole);

        return new AccessAndRefreshToken(accessToken, refreshToken);
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

package com.twentythree.peech.user.service;

import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.AuthorizationIdentifier;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.SignUpFinished;
import com.twentythree.peech.user.UserRole;
import com.twentythree.peech.user.client.KakaoLoginClient;
import com.twentythree.peech.user.domain.*;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import com.twentythree.peech.user.dto.response.KakaoGetUserEmailResponseDTO;
import com.twentythree.peech.user.dto.response.KakaoTokenDecodeResponseDTO;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.validator.UserValidator;
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
    private final UserFetcher userFetcher;
    private final UserDeleter userDeleter;
    private final KakaoLoginClient kakaoLoginClient;

    private final UserValidator userValidator;
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
    public AccessAndRefreshToken loginBySocial( String socialToken, AuthorizationServer authorizationServer) {

        String socialId = "";
        String userEmail = "";

        String accessToken = "";
        String refreshToken = "";

        // Q: 도메인 규칙이라고 볼 수 없는 이런 코드는 위치를 어디로 해야하는가?
        if (authorizationServer == AuthorizationServer.KAKAO) {
            KakaoTokenDecodeResponseDTO kakaoTokenDecodeResponseDTO = kakaoLoginClient.decodeToken(socialToken);
            socialId = kakaoTokenDecodeResponseDTO.getId().toString();

            KakaoGetUserEmailResponseDTO response = kakaoLoginClient.getUserEmail(socialToken);
            userEmail = response.getEmail();

        } else if (authorizationServer == AuthorizationServer.APPLE) {
            // TODO apple 로그인 구현시 여기서 토큰을 직접 decode
        } else {
            throw new IllegalArgumentException(String.format("잘못된 인증 서버입니다: %s", authorizationServer));
        }
        // Q

        AuthorizationIdentifier authorizationIdentifier = AuthorizationIdentifier.of(socialId, authorizationServer);

        if (userValidator.notExistUser(authorizationIdentifier)) {

            UserDomain userDomain = userCreator.createUserByEmail(authorizationIdentifier, userEmail, SignUpFinished.PENDING);
            Long userId = userMapper.saveUserDomain(userDomain);
            UserRole userRole = userDomain.getRole();

            accessToken = jwtUtils.createAccessToken(userId, userRole);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole);

        } else if (userValidator.existUser(authorizationIdentifier)) {
            UserEntity user = userRepository.findByAuthorizationIdentifier(authorizationIdentifier).orElseThrow(() -> new IllegalArgumentException("소셜 로그인이 잘 못되었습니다."));
            Long userId = user.getId();
            UserRole userRole = user.getRole();

            accessToken = jwtUtils.createAccessToken(userId, userRole);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole);
        } else {
            throw new RuntimeException("유저 생성에서 예상치 못한 문제가 생겼습니다.");
        }

        return new AccessAndRefreshToken(accessToken, refreshToken);
    }

    @Override
    public UserDomain deleteUser(Long userId) {
        UserDomain userDomain = userFetcher.fetchUser(userId);
        LocalDate deleteAt = userDeleter.deleteUser(userDomain);
        return userDomain;
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

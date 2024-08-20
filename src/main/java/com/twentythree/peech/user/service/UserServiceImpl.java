package com.twentythree.peech.user.service;

import com.twentythree.peech.common.exception.Unauthorized;
import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.client.AppleLoginClient;
import com.twentythree.peech.user.client.KakaoLoginClient;
import com.twentythree.peech.user.domain.*;
import com.twentythree.peech.user.dto.LoginBySocial;
import com.twentythree.peech.user.dto.IdentityToken;
import com.twentythree.peech.user.dto.KakaoAccount;
import com.twentythree.peech.user.dto.response.ApplePublicKeyResponseDTO;
import com.twentythree.peech.user.dto.response.GetUserInformationResponseDTO;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import com.twentythree.peech.user.validator.UserValidator;
import com.twentythree.peech.user.value.*;
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
    private final AppleLoginClient appleLoginClient;

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
    public LoginBySocial loginBySocial(String socialToken, AuthorizationServer authorizationServer) {

        String userEmail = null;

        String accessToken = null;
        String refreshToken = null;

        String bearerSocialToken  = "Bearer " + socialToken;

        // Q: 도메인 규칙이라고 볼 수 없는 이런 코드는 위치를 어디로 해야하는가?
        if (authorizationServer == AuthorizationServer.KAKAO) {

            KakaoAccount kakaoAccount = kakaoLoginClient.getUserEmail(bearerSocialToken).getKakaoAccount();

            if (userValidator.kakaoEmailValid(kakaoAccount)) {
                userEmail = kakaoAccount.getEmail();
            } else {
                throw new Unauthorized("이메일이 검증되지 않았습니다.");
            }
        } else if (authorizationServer == AuthorizationServer.APPLE) {

            IdentityToken identityToken = jwtUtils.decodeIdentityToken(socialToken);
            String alg = identityToken.getIdentityTokenHeader().getAlg();
            String kid = identityToken.getIdentityTokenHeader().getKid();

            ApplePublicKeyResponseDTO publicKeys = appleLoginClient.getPublicKeys();

            if (identityToken.isVerify(publicKeys.getApplePublicKeys())) {
                userEmail = identityToken.getIdentityTokenPayload().getEmail();
            } else {
                throw new Unauthorized("애플로그인에서 토큰이 유효하지 않습니다.");
            }

        } else {
            throw new Unauthorized(String.format("잘못된 인증 서버입니다: %s", authorizationServer));
        }

        Integer responseCode = 411;

        if (userValidator.notExistUserByEmail(userEmail)) {

            UserDomain userDomain = userCreator.createUserByEmail(authorizationServer, userEmail, SignUpFinished.PENDING);
            Long userId = userMapper.saveUserDomain(userDomain);
            UserRole userRole = userDomain.getRole();

            accessToken = jwtUtils.createAccessToken(userId, userRole);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole);

        } else if (userValidator.existUserByEmail(userEmail)) {
            UserDomain userDomain = userFetcher.fetchUserByEmail(userEmail);

            Long userId = userDomain.getUserId();
            UserRole userRole = userDomain.getRole();

            if (userValidator.singedUpFinishedUser(userId)) {
                responseCode = 200;
            }

            accessToken = jwtUtils.createAccessToken(userId, userRole);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole);
        } else {
            throw new RuntimeException("유저 생성에서 예상치 못한 문제가 생겼습니다.");
        }

        return new LoginBySocial(accessToken, refreshToken, responseCode);
    }

    @Override
    @Transactional
    public UserDomain deleteUser(Long userId) {
        UserDomain userDomain = userFetcher.fetchUser(userId);
        LocalDate deleteAt = userDeleter.deleteUser(userDomain);
        userMapper.saveUserDomain(userDomain);
        return userDomain;
    }

    @Override
    @Transactional
    public LoginBySocial completeProfile(Long userId, String firstName, String lastName, String nickName, LocalDate birth, UserGender gender) {

        Integer responseCode = 200;

        UserDomain userDomain = userFetcher.fetchUser(userId);
        UserDomain completedUserDomain = userCreator.completeUser(userDomain, userDomain.getAuthorizationServer(), firstName, lastName, birth, gender, userDomain.getEmail(), nickName, userDomain.getRole(), userDomain.getUserStatus(), SignUpFinished.FINISHED, userDomain.getDeleteAt());
        userId = userMapper.saveUserDomain(completedUserDomain);
        UserRole userRole = userDomain.getRole();


        String accessToken = jwtUtils.createAccessToken(userId, userRole);
        String refreshToken = jwtUtils.createRefreshToken(userId, userRole);
        return new LoginBySocial(accessToken, refreshToken, responseCode);
    }

    @Override
    public UserEntity existUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }

    public GetUserInformationResponseDTO getUserInformation() {

        // TODO context holder에서 id 가져옴
        Long userId = 1L;

        UserDomain userDomain = userFetcher.fetchUser(userId);
        String nickName = userDomain.getNickName();

        GetUserInformationResponseDTO getUserInformationResponse = new GetUserInformationResponseDTO(nickName);

        return getUserInformationResponse;
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

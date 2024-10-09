package com.twentythree.peech.user.service;

import com.twentythree.peech.common.exception.Unauthorized;
import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.common.utils.UserRoleConvertUtils;
import com.twentythree.peech.script.service.ThemeService;
import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import com.twentythree.peech.security.jwt.JWTAuthenticationToken;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import com.twentythree.peech.user.client.AppleLoginClient;
import com.twentythree.peech.user.client.KakaoLoginClient;
import com.twentythree.peech.user.domain.*;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;


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
    private final ThemeService themeService;

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
    public LoginBySocial loginBySocial(String socialToken, AuthorizationServer authorizationServer, String funnel) {

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

            if (identityToken.isVerify(socialToken, publicKeys.getApplePublicKeyKey(identityToken.getIdentityTokenHeader()))) {
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

            accessToken = jwtUtils.createAccessToken(userId, userRole, funnel);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole, funnel);

        } else if (userValidator.existUserByEmail(userEmail)) {
            UserDomain userDomain = userFetcher.fetchUserByEmail(userEmail);

            Long userId = userDomain.getUserId();
            UserRole userRole = userDomain.getRole();

            if (userValidator.singedUpFinishedUser(userId)) {
                responseCode = 200;
            }

            accessToken = jwtUtils.createAccessToken(userId, userRole, funnel);
            refreshToken = jwtUtils.createRefreshToken(userId, userRole, funnel);
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
    public LoginBySocial completeProfile(Long userId, String firstName, String lastName, String nickName, LocalDate birth, UserGender gender, String funnel) {

        Integer responseCode = 200;

        UserDomain userDomain = userFetcher.fetchUser(userId);
        UserDomain completedUserDomain = userCreator.completeUser(userDomain, userDomain.getAuthorizationServer(), firstName, lastName, birth, gender, userDomain.getEmail(), nickName, userDomain.getRole(), userDomain.getUserStatus(), SignUpFinished.FINISHED, userDomain.getDeleteAt());
        userId = userMapper.saveUserDomain(completedUserDomain);
        UserRole userRole = userDomain.getRole();


        String accessToken = jwtUtils.createAccessToken(userId, userRole, funnel);
        String refreshToken = jwtUtils.createRefreshToken(userId, userRole, funnel);

        // 회원 생성이 완료되면 해당 회원의 기본 폴더를 생성
        themeService.createDefaultFolder(userId);

        return new LoginBySocial(accessToken, refreshToken, responseCode);
    }

    @Override
    public UserEntity existUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }

    public GetUserInformationResponseDTO getUserInformation(Long userId) {

        UserDomain userDomain = userFetcher.fetchUser(userId);
        String nickName = userDomain.getNickName();

        GetUserInformationResponseDTO getUserInformationResponse = new GetUserInformationResponseDTO(nickName);

        return getUserInformationResponse;
    }

    @Override
    public AccessAndRefreshToken createNewToken(String refreshToken, Long userId, String funnel) {
        try {
            JWTAuthenticationToken authentication = (JWTAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            GrantedAuthority Authority = authentication.getAuthorities()
                    .stream().findFirst().orElseThrow(() -> new NoSuchElementException("유저의 권한이 부여되지 않았습니다"));

            String newAccessToken = jwtUtils.createAccessToken(userId, UserRoleConvertUtils
                    .convertStringToUserRole(Authority.getAuthority()), funnel);
            String newRefreshToken = jwtUtils.createRefreshToken(userId, UserRoleConvertUtils
                    .convertStringToUserRole(Authority.getAuthority()), funnel);

            return new AccessAndRefreshToken(newAccessToken, newRefreshToken);
        }catch (Exception e){
            throw new JWTAuthenticationException(LoginExceptionCode.LOGIN_EXCEPTION_CODE);
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

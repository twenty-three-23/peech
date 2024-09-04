package com.twentythree.peech.user.controller;

import com.twentythree.peech.common.dto.response.WrappedResponseBody;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import com.twentythree.peech.user.domain.UserFetcher;
import com.twentythree.peech.user.domain.UserMapper;
import com.twentythree.peech.user.dto.response.GetUserInformationResponseDTO;
import com.twentythree.peech.user.value.AuthorizationServer;
import com.twentythree.peech.user.domain.UserDomain;
import com.twentythree.peech.user.dto.LoginBySocial;
import com.twentythree.peech.user.dto.request.CompleteProfileRequestDTO;
import com.twentythree.peech.user.dto.request.CreateUserRequestDTO;
import com.twentythree.peech.user.dto.request.LoginBySocialRequestDTO;
import com.twentythree.peech.user.dto.response.UserDeleteResponseDTO;
import com.twentythree.peech.user.dto.response.UserIdTokenResponseDTO;
import com.twentythree.peech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController implements SwaggerUserController{

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserFetcher userFetcher;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "유저 가입",
            description = "deviceId를 RequestBody에 담아 요청하면 새로운 유저를 생성하고 생성된 UserId를 응답한다.")
    @Override
    @PostMapping("api/v1/user")
    public UserIdTokenResponseDTO createUserByDeviceId(@RequestBody CreateUserRequestDTO request) {
        String token = userService.createUserByDeviceId(request.getDeviceId());
        return new UserIdTokenResponseDTO(token, token);
    }

    @Operation(summary = "소셜로 회원 가입",
            description = "소셜 계정으로 회원 가입")
    @PostMapping("api/v1.1/user")
    public ResponseEntity<WrappedResponseBody<UserIdTokenResponseDTO>> loginBySocial(@RequestBody LoginBySocialRequestDTO request, @RequestParam String funnel) {

        String token = request.getSocialToken();
        AuthorizationServer authorizationServer = request.getAuthorizationServer();

        LoginBySocial loginBySocial = userService.loginBySocial(token, authorizationServer, funnel);
        UserIdTokenResponseDTO userIdTokenResponseDTO = new UserIdTokenResponseDTO(loginBySocial.getAccessToken(), loginBySocial.getRefreshToken());
        log.info("{}", userIdTokenResponseDTO.getAccessToken());
        return ResponseEntity.status(201).body(new WrappedResponseBody<UserIdTokenResponseDTO>(loginBySocial.getResponseCode(), userIdTokenResponseDTO));
    }

    @Operation(summary = "로그인에 필요한 추가 정보를 입력 받는다",
            description = "gender는 꼭 입력 받지 않아도 된다.")
    @PatchMapping("api/v1.1/user")
    public ResponseEntity<UserIdTokenResponseDTO> completeProfile(@RequestBody CompleteProfileRequestDTO request, @RequestParam String funnel) {
        if (request.getFirstName() == null || request.getLastName() == null || request.getBirth() == null || request.getNickName() == null ) {
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        Long userId = jwtAuthentication.getUserId();
        log.info("userId : {}", userId);

        LoginBySocial accessAndRefreshToken = userService.completeProfile(userId,request.getFirstName(), request.getLastName(), request.getNickName(), request.getBirth(), request.getGender(), funnel);

        return ResponseEntity.status(200).body(new UserIdTokenResponseDTO(accessAndRefreshToken.getAccessToken(), accessAndRefreshToken.getRefreshToken()));
    }

    @Operation(summary = "토큰으로 유저 정보 조회")
    @GetMapping("api/v1.1/user")
    public GetUserInformationResponseDTO getUserInformation() {
        return userService.getUserInformation();
    }

    @Operation(summary = "유저 토큰 재발급",
            description = "deviceId를 전송하면 이미 가입된 유저라면 유저 토큰 재발급")
    @Override
    @GetMapping("api/v1/user")
    public UserIdTokenResponseDTO reIssuanceUserToken(@RequestBody CreateUserRequestDTO request) {
        String token = userService.reIssuanceUserToken(request.getDeviceId());
        return new UserIdTokenResponseDTO(token, token);
    }

    @Operation(summary = "유저 삭제")
    @DeleteMapping("api/v1.1/user")
    public UserDeleteResponseDTO deleteUser() {
        // TODO 유저 토큰에서 userId 가져오는 코드
        Long userId = 123L; // 임시 유저

        UserDomain userDomain = userService.deleteUser(userId);
        return new UserDeleteResponseDTO(userDomain.getDeleteAt());
    }
}

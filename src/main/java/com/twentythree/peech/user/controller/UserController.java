package com.twentythree.peech.user.controller;

import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import com.twentythree.peech.user.dto.request.CreateUserRequestDTO;
import com.twentythree.peech.user.dto.request.LoginBySocialRequestDTO;
import com.twentythree.peech.user.dto.response.UserIdTokenResponseDTO;
import com.twentythree.peech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements SwaggerUserController{

    private final UserService userService;

    @Operation(summary = "유저 가입",
            description = "deviceId를 RequestBody에 담아 요청하면 새로운 유저를 생성하고 생성된 UserId를 응답한다.")
    @Override
    @PostMapping("api/v1/user")
    public UserIdTokenResponseDTO createUserByDeviceId(@RequestBody CreateUserRequestDTO request) {
        String token = userService.createUserByDeviceId(request.getDeviceId());
        return new UserIdTokenResponseDTO(token, token);
    }

    @Operation(summary = "카카오톡으로 회원 가입",
            description = "카카오 톡으로 회원 가입")
    @PostMapping("api/v1.1/user")
    public ResponseEntity<UserIdTokenResponseDTO> loginBySocial(@RequestBody LoginBySocialRequestDTO request) {

        String token = request.getSocialToken();
        AuthorizationServer authorizationServer = request.getAuthorizationServer();

        AccessAndRefreshToken accessAndRefreshToken = userService.loginBySocial(token, authorizationServer);
        
        return ResponseEntity.status(411).body(new UserIdTokenResponseDTO(accessAndRefreshToken.getAccessToken(), accessAndRefreshToken.getRefreshToken()));
    }

    @Operation(summary = "유저 토큰 재발급",
            description = "deviceId를 전송하면 이미 가입된 유저라면 유저 토큰 재발급")
    @Override
    @GetMapping("api/v1/user")
    public UserIdTokenResponseDTO reIssuanceUserToken(@RequestBody CreateUserRequestDTO request) {
        String token = userService.reIssuanceUserToken(request.getDeviceId());
        return new UserIdTokenResponseDTO(token, token);
    }
}

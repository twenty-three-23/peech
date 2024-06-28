package com.twentythree.peech.user.controller;

import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.user.dto.response.CreateUserResponseDTO;
import com.twentythree.peech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements SwaggerUserController{

    private final UserService userService;
    private final JWTUtils jwtUtils;

    @Operation(summary = "유저 가입",
            description = "deviceId를 CreateUserRequestDTO에 담아 요청하면 생성된 UserId를 CreteUserResponseDTO에 담아 응답한다.")
    @Override
    @PostMapping("api/v1/user")
    public CreateUserResponseDTO createUser(@RequestParam String deviceId) {
        Long userId = userService.createUser(deviceId);
        String token = jwtUtils.createJWT(userId);
        return new CreateUserResponseDTO(token);
    }


}

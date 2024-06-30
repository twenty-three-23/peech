package com.twentythree.peech.user.controller;

import com.twentythree.peech.user.dto.request.CreateUserRequestDTO;
import com.twentythree.peech.user.dto.response.CreateUserResponseDTO;
import com.twentythree.peech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CreateUserResponseDTO createUser(@RequestBody CreateUserRequestDTO request) {
        Long userId = userService.createUser(request);
        return new CreateUserResponseDTO(userId);
    }
}

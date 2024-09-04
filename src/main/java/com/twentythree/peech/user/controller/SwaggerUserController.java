package com.twentythree.peech.user.controller;

import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import com.twentythree.peech.user.dto.request.CreateUserRequestDTO;
import com.twentythree.peech.user.dto.response.UserIdTokenResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerUserController {
    @ApiResponse(responseCode = "201", description = "성공", content = {@Content(schema = @Schema(implementation = UserIdTokenResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    UserIdTokenResponseDTO createUserByDeviceId(@RequestBody CreateUserRequestDTO request);

    @ApiResponse(responseCode = "201", description = "성공", content = {@Content(schema = @Schema(implementation = UserIdTokenResponseDTO.class), mediaType = "application/json")})
    UserIdTokenResponseDTO reIssuanceUserToken(@RequestBody CreateUserRequestDTO createUserRequestDTO);

    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = AccessAndRefreshToken.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    AccessAndRefreshToken reissueToken(@RequestBody String refreshToken);
}

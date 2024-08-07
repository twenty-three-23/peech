package com.twentythree.peech.auth.controller;

import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerAuthController {
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = AccessAndRefreshToken.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    AccessAndRefreshToken reissueAccessToken(@RequestBody String refreshToken);
}

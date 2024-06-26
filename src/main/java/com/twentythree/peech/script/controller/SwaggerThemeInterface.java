package com.twentythree.peech.script.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.script.dto.request.ThemeTitleRequestDTO;
import com.twentythree.peech.script.dto.response.ProcessedScriptResponseDTO;
import com.twentythree.peech.script.dto.response.ThemeIdResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerThemeInterface {

    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ThemeIdResponseDTO.class), mediaType = "application/json")})
    ThemeIdResponseDTO saveTheme(@RequestBody ThemeTitleRequestDTO request, @LoginUserId UserIdDTO userIdDTO);
}

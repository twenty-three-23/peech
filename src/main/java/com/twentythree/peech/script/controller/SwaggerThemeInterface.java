package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ThemeTitleRequestDTO;
import com.twentythree.peech.script.dto.response.ThemeIdResponseDTO;
import com.twentythree.peech.script.dto.response.ThemesResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerThemeInterface {

    @ApiResponse(responseCode = "201", description = "성공", content = {@Content(schema = @Schema(implementation = ThemeIdResponseDTO.class), mediaType = "application/json")})
    ThemeIdResponseDTO saveTheme(@RequestBody ThemeTitleRequestDTO request);

    @ApiResponse(responseCode = "200", description = "success")
    ThemesResponseDTO getThemes();
}

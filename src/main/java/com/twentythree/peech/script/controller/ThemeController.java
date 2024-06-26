package com.twentythree.peech.script.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.script.dto.request.ThemeTitleRequestDTO;
import com.twentythree.peech.script.dto.response.ThemeIdResponseDTO;
import com.twentythree.peech.script.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ThemeController implements SwaggerThemeInterface {

    private final ThemeService themeService;

    @Operation(summary = "주제 저장", description = "사용자가 주제를 생성할 때 호출")
    @Override
    @PostMapping("/api/v1/theme")
    public ThemeIdResponseDTO saveTheme(@RequestBody ThemeTitleRequestDTO request, @LoginUserId UserIdDTO userIdDTO) {
        Long userId = userIdDTO.userId();

        Long themeId = themeService.saveTheme(userId, request.themeTitle());

        return new ThemeIdResponseDTO(themeId);
    }
}

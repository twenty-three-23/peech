package com.twentythree.peech.script.controller;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.script.dto.request.ThemeTitleRequestDTO;
import com.twentythree.peech.script.dto.response.ThemeIdResponseDTO;
import com.twentythree.peech.script.dto.response.ThemesResponseDTO;
import com.twentythree.peech.script.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ThemeIdResponseDTO saveTheme(@RequestBody ThemeTitleRequestDTO request) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        Long themeId = themeService.saveTheme(userId, request.themeTitle());

        return new ThemeIdResponseDTO(themeId);
    }

    @Override
    @GetMapping("/api/v1/themes")
    public ThemesResponseDTO getThemes() {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return themeService.getThemesByUserId(userId);
    }
}

package com.twentythree.peech.auth.controller;

import com.twentythree.peech.auth.service.ReissueAccessAndRefreshTokenService;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.1/auth")
public class AuthController {

    private final ReissueAccessAndRefreshTokenService reissueAccessAndRefreshTokenService;

    @PatchMapping("/reissue")
    public AccessAndRefreshToken reissueAccessToken(String refreshToken) {
        return reissueAccessAndRefreshTokenService.createNewToken(refreshToken);
    }
}

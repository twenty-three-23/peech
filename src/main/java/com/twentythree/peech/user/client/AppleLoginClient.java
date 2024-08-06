package com.twentythree.peech.user.client;

import com.twentythree.peech.user.dto.response.ApplePublicKeyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleLoginClient", url = "https://appleid.apple.com/auth/oauth2/")
public interface AppleLoginClient {

    @GetMapping("v2/keys")
    ApplePublicKeyResponseDTO getPublicKeys();
}

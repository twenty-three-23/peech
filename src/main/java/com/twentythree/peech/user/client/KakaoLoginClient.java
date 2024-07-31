package com.twentythree.peech.user.client;

import com.twentythree.peech.user.dto.response.KakaoGetUserEmailResponseDTO;
import com.twentythree.peech.user.dto.response.KakaoTokenDecodeResponseDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoLoginClient", url = "https://kapi.kakao.com/")
public interface KakaoLoginClient {
    final String xfrom = "application/x-www-form-urlencoded;charset=utf-8";

    @GetMapping("v1/user/access_token_info")
    KakaoTokenDecodeResponseDTO decodeToken(@RequestHeader("Authorization") String token);

    @GetMapping("v2/user/me?properties_keys=[\"kakao_acount.email\"]")
    @Headers("application/x-www-form-urlencoded;charset=utf-8")
    KakaoGetUserEmailResponseDTO getUserEmail(@RequestHeader("Authorization") String token);
}

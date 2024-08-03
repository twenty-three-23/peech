package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twentythree.peech.user.dto.KakaoAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoGetUserEmailResponseDTO {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}

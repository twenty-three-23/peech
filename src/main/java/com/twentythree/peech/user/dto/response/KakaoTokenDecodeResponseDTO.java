package com.twentythree.peech.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoTokenDecodeResponseDTO {
    private Long id;
    private Integer expires_in;
    private Integer app_id;
}
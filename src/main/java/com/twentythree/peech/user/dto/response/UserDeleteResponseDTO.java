package com.twentythree.peech.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserDeleteResponseDTO {
    private LocalDateTime deleteAt;

}

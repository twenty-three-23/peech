package com.twentythree.peech.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserDeleteResponseDTO {
    private LocalDate deleteAt;

}

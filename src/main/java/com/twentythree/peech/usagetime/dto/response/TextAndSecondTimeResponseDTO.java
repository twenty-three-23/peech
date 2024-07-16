package com.twentythree.peech.usagetime.dto.response;

import com.twentythree.peech.usagetime.dto.RemainingTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class TextAndSecondTimeResponseDTO {
    private RemainingTimeDTO remainingTime;
}

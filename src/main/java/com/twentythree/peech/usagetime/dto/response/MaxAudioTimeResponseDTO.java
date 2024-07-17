package com.twentythree.peech.usagetime.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.twentythree.peech.usagetime.dto.TextAndSecondDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class MaxAudioTimeResponseDTO {

    private TextAndSecondDTO maxAudioTime;
}


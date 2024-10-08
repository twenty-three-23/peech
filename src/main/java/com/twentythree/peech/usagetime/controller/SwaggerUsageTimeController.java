package com.twentythree.peech.usagetime.controller;

import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SwaggerUsageTimeController {

    @GetMapping("api/v1/usage-time")
    CheckRemainingTimeResponseDTO checkRemainingTime(@RequestParam(name = "audio-time") Long audioTime);

    @GetMapping("api/v1/usage-time")
    TextAndSecondResponseDTO getUsageTime();

    @GetMapping("api/v1/max-audio-time")
    TextAndSecondResponseDTO getMaxAudioTime();

}

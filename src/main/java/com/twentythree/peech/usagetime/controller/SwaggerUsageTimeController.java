package com.twentythree.peech.usagetime.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondTimeResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SwaggerUsageTimeController {

    @GetMapping("api/v1/usage-time")
    CheckRemainingTimeResponseDTO checkRemainingTime(@LoginUserId UserIdDTO userId, @RequestParam(name = "audio-time") Long audioTime);

    @GetMapping("api/v1/usage-time")
    TextAndSecondTimeResponseDTO getUsageTime(@LoginUserId UserIdDTO userId);

    TextAndSecondTimeResponseDTO getMaxAudioTime();

}

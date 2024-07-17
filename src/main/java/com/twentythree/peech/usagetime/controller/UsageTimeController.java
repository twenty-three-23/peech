package com.twentythree.peech.usagetime.controller;

import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.MaxAudioTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondTimeResponseDTO;
import com.twentythree.peech.usagetime.service.UsageTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UsageTimeController implements SwaggerUsageTimeController{

    private final UsageTimeService usageTimeService;

    @Override
    @GetMapping("api/v1/usage-time")
    public CheckRemainingTimeResponseDTO checkRemainingTime(UserIdDTO userId, Long audioTime) {
        return usageTimeService.checkRemainingTime(userId.userId(), audioTime);
    }

    @Override
    @GetMapping("api/v1/remaining-time")
    public TextAndSecondTimeResponseDTO getUsageTime(UserIdDTO userId) {
        return usageTimeService.getUsageTime(userId.userId());
    }

    @Override
    @GetMapping("api/v1/max-audio-time")
    public MaxAudioTimeResponseDTO getMaxAudioTime() {
        return usageTimeService.getMaxAudioTime();
    }
}

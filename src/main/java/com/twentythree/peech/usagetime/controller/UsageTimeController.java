package com.twentythree.peech.usagetime.controller;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondResponseDTO;
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
    public CheckRemainingTimeResponseDTO checkRemainingTime(Long audioTime) {
        Long userId = SecurityContextHolder.getUserId();

        return usageTimeService.checkRemainingTime(userId, audioTime);
    }

    @Override
    @GetMapping("api/v1/remaining-time")
    public TextAndSecondResponseDTO getUsageTime() {
        Long userId = SecurityContextHolder.getUserId();

        return usageTimeService.getUsageTime(userId);
    }

    @Override
    @GetMapping("api/v1/max-audio-time")
    public TextAndSecondResponseDTO getMaxAudioTime() {
        return usageTimeService.getMaxAudioTime();
    }
}

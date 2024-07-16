package com.twentythree.peech.usagetime.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.usagetime.dto.AudioFileRequestDTO;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.RemainingTimeResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SwaggerUsageTimeController {

    @GetMapping("api/v1/usage-time")
    CheckRemainingTimeResponseDTO checkRemainingTime(@LoginUserId UserIdDTO userId, @RequestParam(name = "audio-time") Long audioTime);

    @GetMapping("api/v1/usage-time")
    RemainingTimeResponseDTO getUsageTime(@LoginUserId UserIdDTO userId);

}

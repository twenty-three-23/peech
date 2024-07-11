package com.twentythree.peech.script.stt.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.STTResultResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface SwaggerSTTController {
    @ApiResponse(responseCode = "201" , description = "성공", content = {@Content(schema = @Schema(implementation = STTResultResponseDto.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400" , description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    Mono<STTResultResponseDto>  responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId, @PathVariable("scriptId") Long scriptId,@LoginUserId UserIdDTO userId);

    @ApiResponse(responseCode = "201" , description = "성공", content = {@Content(schema = @Schema(implementation = STTResultResponseDto.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400" , description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    Mono<STTResultResponseDto>  responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId, @LoginUserId UserIdDTO userId);
}

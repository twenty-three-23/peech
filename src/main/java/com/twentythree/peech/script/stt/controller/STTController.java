package com.twentythree.peech.script.stt.controller;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.STTScriptResponseDTO;
import com.twentythree.peech.script.stt.service.ProcessSTTService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class STTController implements SwaggerSTTController{

    private final ProcessSTTService processSTTService;


    @Operation(summary = "stt 결과 및 소요시간 반환",
        description = "대본 입력없이 바로 음성녹음을 STTRequestDTO에 담아 요청하면 Processing과정을 거쳐 STTResultResponseDTO에 담아 응답한다.")
    @PostMapping(value ="/api/v1/themes/{themeId}/scripts/speech/script", consumes = "multipart/form-data")
    @Override
    public Mono<STTScriptResponseDTO> responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId){
        Long userId = SecurityContextHolder.getUserId();

        return processSTTService.createSTTResult(request, themeId, userId);
    }


    @Operation(summary = "stt 결과 및 소요시간 반환",
            description = "대본이 입력된 상태에서 음성녹음을 STTRequestDTO에 담아 요청하면 Processing과정을 거쳐 STTResultResponseDTO에 담아 응답한다.")
    @PostMapping(value ="/api/v1/themes/{themeId}/scripts/{scriptId}/speech/script", consumes = "multipart/form-data")
    @Override
    public Mono<STTScriptResponseDTO> responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId, @PathVariable("scriptId") Long scriptId){
        Long userId = SecurityContextHolder.getUserId();

        return processSTTService.createSTTResult(request, themeId, scriptId, userId);
    }
}

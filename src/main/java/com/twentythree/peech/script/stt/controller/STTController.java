package com.twentythree.peech.script.stt.controller;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.common.utils.FileUtils;
import com.twentythree.peech.meta.conversionapi.annotation.MetaEventTrigger;
import com.twentythree.peech.meta.conversionapi.eventhandler.event.FeatureType;
import com.twentythree.peech.script.stt.dto.request.STTRequest2DTO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.STTScriptResponseDTO;
import com.twentythree.peech.script.stt.exception.STTException;
import com.twentythree.peech.script.stt.service.ProcessSTTService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class STTController implements SwaggerSTTController {

    private final ProcessSTTService processSTTService;

    @MetaEventTrigger(name = FeatureType.STT)
    @Operation(summary = "stt 결과 및 소요시간 반환",
            description = "대본 입력없이 바로 음성녹음을 STTRequestDTO에 담아 요청하면 Processing과정을 거쳐 STTResultResponseDTO에 담아 응답한다.")
    @PostMapping(value = "/api/v1/themes/{themeId}/scripts/speech/script", consumes = "multipart/form-data")
    @Override
    public STTScriptResponseDTO responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return processSTTService.createSTTResult(request, themeId, userId).block();
    }

    @MetaEventTrigger(name = FeatureType.STT)
    @Operation(summary = "stt 결과 및 소요시간 반환",
            description = "대본이 입력된 상태에서 음성녹음을 STTRequestDTO에 담아 요청하면 Processing과정을 거쳐 STTResultResponseDTO에 담아 응답한다.")
    @PostMapping(value = "/api/v1/themes/{themeId}/scripts/{scriptId}/speech/script", consumes = "multipart/form-data")
    @Override
    public STTScriptResponseDTO responseSTTResult(@ModelAttribute STTRequestDto request, @PathVariable("themeId") Long themeId, @PathVariable("scriptId") Long scriptId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return processSTTService.createSTTResult(request, themeId, scriptId, userId).block();
    }

    @MetaEventTrigger(name = FeatureType.STT)
    @PostMapping(value = "/web/api/v1/themes/{themeId}/scripts/{scriptId}/speech/script")
    public STTScriptResponseDTO responseSTTResult(@RequestBody STTRequest2DTO requestDto,
                                                  @PathVariable("themeId") Long themeId,
                                                  @PathVariable("scriptId") Long scriptId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        try {
            String base64 = requestDto.getFile();
            File file = FileUtils.createAudioFileFromBase64EncodedString(base64);
            STTScriptResponseDTO block = processSTTService.createSTTResult(file, themeId, scriptId, userId).block();
            file.delete();
            return block;
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof STTException) {
                throw (STTException) e;
            } else {
                throw e;
            }
        }
    }

    @MetaEventTrigger(name = FeatureType.STT)
    @Operation(summary = "stt 결과 및 소요시간 반환",
            description = "대본 입력없이 바로 음성녹음을 STTRequestDTO에 담아 요청하면 Processing과정을 거쳐 STTResultResponseDTO에 담아 응답한다.")
    @PostMapping(value = "/web/api/v1/themes/{themeId}/scripts/speech/script")
    public STTScriptResponseDTO responseSTTResult(@RequestBody STTRequest2DTO request, @PathVariable("themeId") Long themeId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();
        String base64 = request.getFile();
        File file = FileUtils.createAudioFileFromBase64EncodedString(base64);

        STTScriptResponseDTO block = processSTTService.createSTTResult(file, themeId, userId).block();
        file.delete();

        return block;
    }

}

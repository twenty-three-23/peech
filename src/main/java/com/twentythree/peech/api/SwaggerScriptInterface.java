package com.twentythree.peech.api;

import com.twentythree.peech.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalTime;

public interface SwaggerScriptInterface {


    @Operation(summary = "대본 입력", description = "대본 전송시 문단으로 나눠서 응답")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ParagraphResponseDTO.class), mediaType = "application/json")})
    ParagraphResponseDTO postParaInfo(@RequestBody ScriptRequestDto script);

    @Operation(summary = "문단 전송", description = "문단 전송시 예상 발표 시간 응답")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DefaultTimeResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    DefaultTimeResponseDTO getDefaultTimePerParagraphResponseDTO(@RequestBody ParagraphRequestDTO paragraph);


    @Operation(summary = "대본 입력", description = "대본 전송시 전체 예상 소요시간 출력")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = AllDefaultTimeResponseDTO.class), mediaType = "application/json")})
    AllDefaultTimeResponseDTO getAllDefaultTimeResponseDTO(@RequestBody ScriptRequestDto script);

}

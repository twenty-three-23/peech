package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ScriptRequestDTO;
import com.twentythree.peech.script.dto.response.ExpectedAllTimeResponseDTO;
import com.twentythree.peech.script.dto.response.ParagraphsResponseDTO;
import com.twentythree.peech.script.dto.response.ProcessedScriptResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerScriptInterface {


    @Operation(summary = "대본 입력", description = "대본 전송시 문단으로 나눠서 응답")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ProcessedScriptResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    ProcessedScriptResponseDTO processScript(@RequestBody ScriptRequestDTO paragraph);
}

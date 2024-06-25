package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ParagraphsRequestDTO;
import com.twentythree.peech.script.dto.request.ScriptIdRequestDTO;
import com.twentythree.peech.script.dto.response.ExpectedTimeResponseDTO;
import com.twentythree.peech.script.dto.response.SaveScriptAndSentenceResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerScriptInterface {

    @ApiResponse(responseCode = "201", description = "성공", content = {@Content(schema = @Schema(implementation = SaveScriptAndSentenceResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    SaveScriptAndSentenceResponseDTO saveScriptAndSentence(@RequestBody ParagraphsRequestDTO request);

    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ExpectedTimeResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    ExpectedTimeResponseDTO getScriptAndSentenceExpectedTime(ScriptIdRequestDTO scriptIdRequestDTO);
}

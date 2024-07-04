package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ModifiedScriptRequestDTO;
import com.twentythree.peech.script.dto.request.ParagraphsRequestDTO;
import com.twentythree.peech.script.dto.response.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface SwaggerScriptInterface {

    @ApiResponse(responseCode = "201", description = "성공", content = {@Content(schema = @Schema(implementation = SaveScriptAndSentencesResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    SaveScriptAndSentencesResponseDTO saveScriptAndSentences(@PathVariable("themeId") Long themeId,
                                                             @RequestBody ParagraphsRequestDTO request);

    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = ExpectedTimeResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "실패", content = {@Content(schema = @Schema(implementation = Error.class), mediaType = "application/json")})
    ExpectedTimeResponseDTO getScriptAndParagraphsExpectedTime(@PathVariable("themeId") Long themeId,
                                                             @PathVariable("scriptId") Long scriptId);

    @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = MajorScriptsResponseDTO.class), mediaType = "application/json")})
    MajorScriptsResponseDTO getMajorScripts(@PathVariable Long themeId);

    @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = MajorScriptsResponseDTO.class), mediaType = "application/json")})
    MinorScriptsResponseDTO getMinorScripts(@PathVariable Long majorVersion, @PathVariable Long themeId);

    @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = ModifiedScriptResponseDTO.class), mediaType = "application/json")})
    ModifiedScriptResponseDTO modifyScript(@PathVariable Long themeId, @PathVariable Long scriptId, @RequestBody ModifiedScriptRequestDTO request);
}

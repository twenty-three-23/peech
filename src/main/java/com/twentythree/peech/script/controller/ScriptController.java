package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ParagraphsRequestDTO;
import com.twentythree.peech.script.dto.response.ExpectedTimeResponseDTO;
import com.twentythree.peech.script.dto.response.MajorScriptsResponseDTO;
import com.twentythree.peech.script.dto.response.SaveScriptAndSentenceResponseDTO;
import com.twentythree.peech.script.service.ScriptSentenceFacade;
import com.twentythree.peech.script.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ScriptController implements SwaggerScriptInterface{

    private final ScriptSentenceFacade scriptSentenceFacade;
    private final ScriptService scriptService;

    @Operation(summary = "문단들 입력", description = "버전 생성 및 저장, 대본 저장, 문장 저장.")
    @PostMapping("/api/v1/script")
    public SaveScriptAndSentenceResponseDTO saveScriptAndSentence(@RequestBody ParagraphsRequestDTO request) {
        Long scriptId = scriptSentenceFacade.createScriptAndSentence(request.themeId(), request.paragraphs());

        return new SaveScriptAndSentenceResponseDTO(scriptId);
    }

    @GetMapping("/api/v1/script")
    public ExpectedTimeResponseDTO getScriptAndSentenceExpectedTime(@RequestParam("scriptId") Long scriptId) {
        return scriptSentenceFacade.getScriptAndSentence(scriptId);
    }

    @Override
    @GetMapping("/api/v1/script/{themeId}")
    public MajorScriptsResponseDTO getMajorScript(@PathVariable("themeId") Long themeId) {
        MajorScriptsResponseDTO majorScript = scriptService.getMajorScript(themeId);
        return majorScript;
    }
}

package com.twentythree.peech.script.controller;

import com.twentythree.peech.script.dto.request.ParagraphsRequestDTO;
import com.twentythree.peech.script.dto.response.SaveScriptAndSentenceResponseDTO;
import com.twentythree.peech.script.service.ScriptSentenceFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ScriptController implements SwaggerScriptInterface{

    private final ScriptSentenceFacade scriptSentenceFacade;

    @Operation(summary = "문단들 입력", description = "버전 생성 및 저장, 대본 저장, 문장 저장.")
    @PostMapping("/api/v1/script")
    public SaveScriptAndSentenceResponseDTO saveScriptAndSentence(@RequestBody ParagraphsRequestDTO request) {

        Long scriptId = scriptSentenceFacade.createScriptAndSentence(request.packageId(), request.paragraphs());

        return new SaveScriptAndSentenceResponseDTO(scriptId);
    }

}

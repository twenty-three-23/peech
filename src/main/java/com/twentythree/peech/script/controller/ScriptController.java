package com.twentythree.peech.script.controller;

import com.twentythree.peech.auth.dto.LoginUserId;
import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.script.dto.MinorScriptDTO;
import com.twentythree.peech.script.dto.request.ModifiedScriptRequestDTO;
import com.twentythree.peech.script.dto.request.ParagraphsRequestDTO;
import com.twentythree.peech.script.dto.response.*;
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

    @Operation(summary = "새로운 대본 생성",
            description = "특정 주제를 themeId로 path에 넣고, 스크립트를 문단들로 나누어 RequestBody에 입력하면 새로운 버전, 대본, 문장 생성 및 저장.")
    @Override
    @PostMapping("/api/v1/themes/{themeId}/script")
    public SaveScriptAndSentencesResponseDTO saveScriptAndSentences(@PathVariable("themeId") Long themeId,
                                                                    @RequestBody ParagraphsRequestDTO request) {
        Long scriptId = scriptSentenceFacade.createScriptAndSentences(themeId, request.paragraphs());

        return new SaveScriptAndSentencesResponseDTO(scriptId);
    }

    @Operation(summary = "입력한 스크립트에 대한 예상 시간 가져오기",
            description = "특정 주제의 스크립트를 themeId, scriptId로 path에 입력하면 전체 예상시간, 문단별 예상시간들을 응답")
    @Override
    @GetMapping("/api/v1/themes/{themeId}/scripts/{scriptId}/time")
    public ExpectedTimeResponseDTO getScriptAndParagraphsExpectedTime(@PathVariable("themeId") Long themeId,
                                                                    @PathVariable("scriptId") Long scriptId) {
        return scriptSentenceFacade.getScriptAndSentence(scriptId);
    }

    @Operation(summary = "입력(메이저) 대본 가져오기",
            description = "특정 주제의 themeId를 path에 넣으면 특정 주제에 대해 입력한(메이저) 스크립트들을 응답한다.")
    @Override
    @GetMapping("/api/v1/themes/{themeId}/scripts/majors")
    public MajorScriptsResponseDTO getMajorScripts(@PathVariable("themeId") Long themeId) {
        MajorScriptsResponseDTO majorScripts = scriptService.getMajorScripts(themeId);
        return majorScripts;
    }

    @Operation(summary = "측정(마이너) 대본 가져오기",
            description = "특정 주제의 themeId와 메이저 대본의 버전(majorVersion)을 path에 입력하면 특정주제의 입력대본을 가지고 연습하여 나온 음성 스크립트들을 응답한다.")
    @Override
    @GetMapping("/api/v1/themes/{themeId}/scripts/{majorVersion}/minors")
    public MinorScriptsResponseDTO getMinorScripts(@PathVariable("themeId") Long themeId, @PathVariable("majorVersion") Long majorVersion) {
        return scriptService.getMinorScripts(themeId, majorVersion);
    }

    @Operation(summary = "측정(마이너) 대본 상세 가져오기",
            description = "특정 주제의 themeId와 메이저 버전 major_version, 마이너 버전 minor version 을 모두 보내주면 해당 버전의 음성 스크립트를 응답한다.")
    @Override
    @GetMapping("/api/v1/themes/{themeId}/scripts/{majorVersion}/{minorVersion}")
    public MinorScriptDTO getMinorScriptDetail(@PathVariable("themeId") Long themeId, @PathVariable("majorVersion") Long majorVersion, @PathVariable("minorVersion") Long minorVersion) {
        return scriptService.getMinorScriptDetail(themeId, majorVersion, minorVersion);
    }

    @Operation(summary = "연습해서 나온 측정 대본 수정하기",
            description = "수정한 측정대본을 전달하면 수정된 부분에 대해서는 예상 시간을 통해서 결과를 응답한다.")
    @Override
    @PostMapping("/api/v1/themes/{themeId}/scripts/{scriptId}")
    public ModifyScriptResponseDTO modifyScript(@PathVariable Long themeId, @PathVariable Long scriptId, @RequestBody ModifiedScriptRequestDTO request, @LoginUserId UserIdDTO userId) {
        return scriptService.modifyScriptService(request.getParagraphs(), scriptId, userId.userId());
    }
}

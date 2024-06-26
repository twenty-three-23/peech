package com.twentythree.peech.script.service;

import com.twentythree.peech.script.dto.SaveScriptDTO;
import com.twentythree.peech.script.dto.paragraphIdToExpectedTime;
import com.twentythree.peech.script.dto.response.ExpectedTimeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScriptSentenceFacade {
    private final ScriptService scriptService;
    private final SentenceService sentenceService;

    @Transactional
    public Long createScriptAndSentence(Long packageId, String[] paragraphs) {
        SaveScriptDTO saveScripDTO = scriptService.saveInputScript(packageId, paragraphs);
        List<Long> sentenceIds = sentenceService.saveInputSentencesByParagraphs(saveScripDTO.scriptEntity(), paragraphs);

        return saveScripDTO.scriptEntity().getScriptId();
    }

    @Transactional
    public ExpectedTimeResponseDTO getScriptAndSentence(Long scriptId) {
        LocalTime inputExpectedScriptTime = scriptService.getInputExpectedScriptTime(scriptId);
        List<paragraphIdToExpectedTime> paragraphExpectedTime = sentenceService.getParagraphExpectedTime(scriptId);

        return new ExpectedTimeResponseDTO(inputExpectedScriptTime, paragraphExpectedTime);
    }
}

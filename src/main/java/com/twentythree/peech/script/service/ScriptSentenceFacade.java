package com.twentythree.peech.script.service;

import com.twentythree.peech.script.dto.SaveScripDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScriptSentenceFacade {
    private final ScriptService scriptService;
    private final SentenceService sentenceService;

    @Transactional
    public Long createScriptAndSentence(Long packageId, String[] paragraphs) {
        SaveScripDTO saveScripDTO = scriptService.saveInputScript(packageId, paragraphs);
        List<Long> sentenceIds = sentenceService.saveInputSentencesByParagraphs(saveScripDTO.scriptEntity(), paragraphs);

        return saveScripDTO.scriptEntity().getScriptId();
    }

}

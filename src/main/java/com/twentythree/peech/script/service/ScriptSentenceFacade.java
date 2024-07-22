package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.dto.SaveScriptDTO;
import com.twentythree.peech.script.dto.paragraphIdToExpectedTime;
import com.twentythree.peech.script.dto.response.ExpectedTimeResponseDTO;
import com.twentythree.peech.script.dto.response.MinorDetailScriptDTO;
import com.twentythree.peech.script.dto.response.ParagraphDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScriptSentenceFacade {
    private final ScriptService scriptService;
    private final SentenceService sentenceService;

    @Transactional
    public Long createScriptAndSentences(Long themeId, String[] paragraphs) {
        SaveScriptDTO saveScripDTO = scriptService.saveInputScript(themeId, paragraphs);
        List<String> sentenceIds = sentenceService.saveInputSentencesByParagraphs(saveScripDTO.scriptEntity(), paragraphs);

        return saveScripDTO.scriptEntity().getScriptId();
    }

    @Transactional
    public ExpectedTimeResponseDTO getScriptAndSentence(Long scriptId) {
        LocalTime inputExpectedScriptTime = scriptService.getInputExpectedScriptTime(scriptId);
        List<paragraphIdToExpectedTime> paragraphExpectedTime = sentenceService.getParagraphExpectedTime(scriptId);

        return new ExpectedTimeResponseDTO(inputExpectedScriptTime, paragraphExpectedTime);
    }

    public MinorDetailScriptDTO getMinorScriptAndSentence(Long themeId, Long majorVersion, Long minorVersion) {

        List<ParagraphDetail> paragraphDetails = new ArrayList<>();
        // 해당 스크립트 정보 가져오기
        ScriptEntity minorDetailScript = scriptService.getMinorScriptDetail(themeId, majorVersion, minorVersion);
        // 문잘별 정보 가져오기
        List<SentenceEntity> minorDetailSentenceList = sentenceService.getMinorScriptSentences(minorDetailScript.getScriptId());

        // 문단별로 그룹핑
        Map<Long, List<SentenceEntity>> groupedSentences = minorDetailSentenceList.stream().collect(Collectors.groupingBy(SentenceEntity::getParagraphId));

        for(Map.Entry<Long, List<SentenceEntity>> sentenceEntry : groupedSentences.entrySet()) {

            List<SentenceEntity> sentenceList = sentenceEntry.getValue();
            List<String> sentenceContentList = sentenceEntry.getValue().stream().map(SentenceEntity::getSentenceContent).toList();

            // 문단에 해당하는 시간 합산
            LocalTime realTimePerParagraph = sentenceList.stream()
                    .map(SentenceEntity::getSentenceRealTime)
                    .reduce(LocalTime.of(0,0,0),
                            ((localTime, localTime2) -> localTime.plusHours(localTime2.getHour()).plusMinutes(localTime2.getMinute()).plusSeconds(localTime2.getSecond())));

            ParagraphDetail paragraphDetail = new ParagraphDetail(realTimePerParagraph, sentenceContentList);
            paragraphDetails.add(paragraphDetail);
        }
        return new MinorDetailScriptDTO(minorDetailScript.getTotalRealTime(), paragraphDetails);
    }
}

package com.twentythree.peech.script.service;


import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.dto.paragraphIdToExpectedTime;
import com.twentythree.peech.script.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    @Transactional
    public List<Long> saveInputSentencesByParagraphs(ScriptEntity scriptEntity, String[] paragraphs) {

        List<Long> sentenceIds = new ArrayList<>();


        for (int paragraphId = 0; paragraphId < paragraphs.length; paragraphId++) {
            String paragraph = paragraphs[paragraphId];

            String[] sentences = paragraph.split("\\.\\s*");
            for (int sentenceOrder = 0; sentenceOrder < sentences.length; sentenceOrder++) {

                String sentence = sentences[sentenceOrder]+".";
                LocalTime expectedTime = ScriptUtils.calculateExpectedTime(sentence);
                SentenceEntity sentenceEntity = sentenceRepository.save(SentenceEntity.ofCreateInputSentence(scriptEntity, (long) paragraphId, sentence, (long) sentenceOrder, expectedTime));
                sentenceIds.add(sentenceEntity.getSentenceId());
            }
        }
        return sentenceIds;
    }

    public List<paragraphIdToExpectedTime> getParagraphExpectedTime(Long scriptId) {

        List<paragraphIdToExpectedTime> results = new ArrayList<>();

        Map<Long, LocalTime> timeMap = new HashMap<>();

        List<SentenceEntity> sentences = sentenceRepository.findBySentencesToScriptId(scriptId);

        for (SentenceEntity sentence : sentences) {
            Long paragraphId = sentence.getParagraphId();
            LocalTime expectedTime = timeMap.get(paragraphId);

            if (expectedTime == null) {
                timeMap.put(paragraphId, sentence.getSentenceExpectTime());
            } else {
                LocalTime sumTime = ScriptUtils.sumLocalTime(expectedTime, sentence.getSentenceExpectTime());
                timeMap.put(paragraphId, sumTime);
            }
        }

        Long paragraphId = 0L;
        for (Map.Entry<Long, LocalTime> entry : timeMap.entrySet()) {
            results.add(new paragraphIdToExpectedTime(entry.getKey(), entry.getValue()));
        }

        return results;
    }
}

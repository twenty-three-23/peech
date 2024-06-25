package com.twentythree.peech.script.service;


import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

            String[] sentences = paragraph.split(".");

            for (int sentenceOrder = 0; sentenceOrder < sentences.length; sentenceOrder++) {
                String sentence = sentences[sentenceOrder];
                LocalTime expectedTime = ScriptUtils.calculateExpectedTime(sentence);
                SentenceEntity sentenceEntity = sentenceRepository.save(SentenceEntity.ofCreateInputSentence(scriptEntity, (long) paragraphId, sentence, (long) sentenceOrder, expectedTime));
                sentenceIds.add(sentenceEntity.getSentenceId());
            }
        }
        return sentenceIds;
    }

}

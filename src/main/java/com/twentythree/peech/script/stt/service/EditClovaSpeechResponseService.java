package com.twentythree.peech.script.stt.service;


import com.twentythree.peech.script.stt.dto.EditClovaSpeechSentenceVO;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.utils.RealTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EditClovaSpeechResponseService {

    public List<EditClovaSpeechSentenceVO> editClovaSpeechResponseSentences(ClovaResponseDto clovaResponseDto) {

        List<EditClovaSpeechSentenceVO> clovaSpeechSentenceDtoList = new ArrayList<>();

        StringBuilder currentSentence = new StringBuilder();
        int calculatedTime = 0;
        int startTimestamp = 0;
        int endTimestamp = 0;
        Long sentenceOrder = 1L;

        // Get the sentences that STT arbitrarily divided
        for(ClovaResponseDto.Segment segment : clovaResponseDto.getSentences()) {
            List<List<Object>> words = segment.getWords();

            // Start checking word by word
            for (List<Object> word : words){
                String text = (String) word.get(2);

                if(currentSentence.isEmpty()) {
                    startTimestamp = (int) word.get(0);
                }

                if (!currentSentence.isEmpty()) {
                    // No space needed if it's the first word
                    currentSentence.append(" ");
                }
                currentSentence.append(text);

                if(text.contains(".")) {
                    endTimestamp = (int) word.get(1);
                    calculatedTime = endTimestamp - startTimestamp;
                    clovaSpeechSentenceDtoList.add(new EditClovaSpeechSentenceVO(sentenceOrder++, currentSentence.toString(), RealTimeUtils.convertMsToTimeFormat(calculatedTime)));
                    currentSentence.setLength(0);
                    calculatedTime = 0;
                }
            }
        }
        return clovaSpeechSentenceDtoList;
    }
}

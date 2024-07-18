
package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.dto.NowStatus;
import com.twentythree.peech.script.stt.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// STT 결과를 클라이언트에게 전달할 VO 생성
@Service
@RequiredArgsConstructor
public class CreateSTTResultService {

    private final RedisTemplateImpl redisTemplateImpl;

    public STTScriptResponseDTO createSTTResultResponseDto(ClovaResponseDto clovaResponseDto, List<SentenceEntity> sentenceEntityList) {


        // 문단별로 그룹핑
        Map<Long, List<SentenceEntity>> sentenceListGroupByParagraph = sentenceEntityList.stream().collect(Collectors.groupingBy(SentenceEntity::getParagraphId));

        List<STTParagraphDTO> paragraphList = new ArrayList<>();

        for(Map.Entry<Long, List<SentenceEntity>> sentenceEntry : sentenceListGroupByParagraph.entrySet()) {

            List<SentenceDTO> sentenceDTOList = new ArrayList<>();

            for(SentenceEntity sentenceEntity : sentenceEntry.getValue()) {
                SentenceDTO sentenceDTO = new SentenceDTO(sentenceEntity.getSentenceId(), sentenceEntity.getSentenceOrder(), sentenceEntity.getSentenceContent());
                sentenceDTOList.add(sentenceDTO);
            }

            List<SentenceEntity> sentenceList = sentenceEntry.getValue();
            List<String> sentenceContentList = sentenceEntry.getValue().stream().map(SentenceEntity::getSentenceContent).toList();

            // 문단에 해당하는 시간 합산
            LocalTime realTimePerParagraph = sentenceList.stream()
                    .map(SentenceEntity::getSentenceRealTime)
                    .reduce(LocalTime.of(0,0,0),
                            ((localTime, localTime2) -> localTime.plusHours(localTime2.getHour()).plusMinutes(localTime2.getMinute()).plusSeconds(localTime2.getSecond())));

            STTParagraphDTO sttParagraphDTO = new STTParagraphDTO(sentenceEntry.getKey(), sentenceEntry.getKey(), realTimePerParagraph, NowStatus.REALTIME, sentenceDTOList);
            paragraphList.add(sttParagraphDTO);
        }

        return new STTScriptResponseDTO(paragraphList);//        return new STTScriptResponseDTO(paragraphList);
    }

}


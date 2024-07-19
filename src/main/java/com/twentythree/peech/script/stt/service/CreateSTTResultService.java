
package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.dto.NowStatus;
import com.twentythree.peech.script.stt.dto.AddSentenceInformationVO;
import com.twentythree.peech.script.stt.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// STT 결과를 클라이언트에게 전달할 VO 생성
@Service
@RequiredArgsConstructor
public class CreateSTTResultService {

    public STTScriptResponseDTO createSTTResultResponseDto(ClovaResponseDto clovaResponseDto, List<SentenceEntity> sentenceEntityList, List<AddSentenceInformationVO> sentenceAndRealTimeList, Long scriptId) {

        // 문단별로 그룹핑
        Map<Long, List<SentenceEntity>> sentenceListGroupByParagraph = sentenceEntityList.stream().collect(Collectors.groupingBy(SentenceEntity::getParagraphId));

        List<STTParagraphDTO> paragraphList = new ArrayList<>();

        // 문단 단위로 가져오기
        for(Map.Entry<Long, List<SentenceEntity>> sentenceEntry : sentenceListGroupByParagraph.entrySet()) {

            List<SentenceDTO> sentenceDTOList = new ArrayList<>();

            // 문단의 첫 문장과 마지막 문장 가져오기
            String firstSentence = sentenceEntry.getValue().get(0).getSentenceContent();
            String lastSentence = sentenceEntry.getValue().get(sentenceEntry.getValue().size() - 1).getSentenceContent();

            LocalTime startTime = sentenceAndRealTimeList.stream().filter(sentence -> sentence.sentenceContent().equals(firstSentence))
                    .findFirst().map(AddSentenceInformationVO::startTime).orElseThrow(() -> new NoSuchElementException("해당하는 문장이 없습니다."));
            LocalTime endTime = sentenceAndRealTimeList.stream().filter(sentence -> sentence.sentenceContent().equals(lastSentence))
                    .findFirst().map(AddSentenceInformationVO::endTime).orElseThrow(() -> new NoSuchElementException("해당하는 문장이 없습니다."));


            for(SentenceEntity sentenceEntity : sentenceEntry.getValue()) {
                SentenceDTO sentenceDTO = new SentenceDTO(sentenceEntity.getSentenceId(), sentenceEntity.getSentenceOrder(), sentenceEntity.getSentenceContent());
                sentenceDTOList.add(sentenceDTO);
            }

            List<SentenceEntity> sentenceList = sentenceEntry.getValue();
            List<String> sentenceContentList = sentenceEntry.getValue().stream().map(SentenceEntity::getSentenceContent).toList();

            // 문단에 해당하는 시간 합산
            LocalTime realTimePerParagraph = sentenceList.stream()
                    .map(SentenceEntity::getSentenceRealTime)
                    .reduce(LocalTime.of(0,0,0, 0),
                            ((localTime, localTime2) -> localTime.plusHours(localTime2.getHour()).plusMinutes(localTime2.getMinute()).plusSeconds(localTime2.getSecond()).plusNanos(localTime2.getNano())));

            STTParagraphDTO sttParagraphDTO = new STTParagraphDTO(sentenceEntry.getKey(), sentenceEntry.getKey(), realTimePerParagraph, startTime, endTime, NowStatus.REALTIME, sentenceDTOList);
            paragraphList.add(sttParagraphDTO);
        }

        return new STTScriptResponseDTO(scriptId, clovaResponseDto.getTotalRealTime(), paragraphList);
    }
}


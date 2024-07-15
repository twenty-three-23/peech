
package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.dto.RedisSentenceDTO;
import com.twentythree.peech.script.stt.dto.response.NowStatus;
import com.twentythree.peech.script.stt.dto.EditClovaSpeechSentenceVO;
import com.twentythree.peech.script.stt.dto.STTResultSentenceDto;
import com.twentythree.peech.script.stt.dto.SentenceVO;
import com.twentythree.peech.script.stt.dto.response.*;
import com.twentythree.peech.script.stt.utils.RealTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// STT 결과를 클라이언트에게 전달할 VO 생성
@Service
@RequiredArgsConstructor
public class CreateSTTResultService {

    private final RedisTemplateImpl redisTemplateImpl;

    public STTScriptResponseDTO createSTTResultResponseDto(ClovaResponseDto clovaResponseDto, List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList, List<SentenceVO> sentenceVOList, ParagraphDivideResponseDto paragraphDivideResponseDto) {

        int timestamp;
        Long paragraphId = 0L;
        Long paragraphOrder = 0L;

        List<STTResultSentenceDto> sttResultSentenceDtoList = new ArrayList<>();

        // 전체 대본 소요 시간
        LocalTime totalRealTime = clovaResponseDto.getTotalRealTime();

        // sttSentenceDto 리스트에 있는 타임 스탬프들을 가져옴
        List<Integer> sentencesRealTimeList = sentenceAndRealTimeList
                .stream().map(EditClovaSpeechSentenceVO::sentenceDuration)
                .toList();

        for(int i = 0; i < sentencesRealTimeList.size(); i++){
            SentenceVO sentenceVO = sentenceVOList.get(i);

            Long id = sentenceVO.sentenceEntity().getSentenceId();
            Long order = sentenceVO.sentenceEntity().getSentenceOrder();
            String content = sentenceVO.sentenceEntity().getSentenceContent();

            STTResultSentenceDto sttResultSentenceDto = new STTResultSentenceDto(id, order, content, sentencesRealTimeList.get(i));
            sttResultSentenceDtoList.add(sttResultSentenceDto);
        }

        // paragraphDevideResponseDto에서 문단별로 나눈 index를 가져옴
        List<List<Integer>> paragraphNumber = paragraphDivideResponseDto.getResult().getSpan();

        // 문단별 측정된 시간 저장
        List<LocalTime> paragraphRealTime = new ArrayList<>();

        List<STTParagraphDTO> paragraphList = new ArrayList<>();

        for (List<Integer> paragraph : paragraphNumber) {
            List<SentenceDTO> sentenceList = new ArrayList<>();
            timestamp = 0;
            for (Integer index : paragraph) {

                Long sentenceId = sttResultSentenceDtoList.get(index).getSentenceId();
                Long sentenceOrder = sttResultSentenceDtoList.get(index).getSentenceOrder();
                String sentenceContent = sttResultSentenceDtoList.get(index).getContent();

                timestamp += sttResultSentenceDtoList.get(index).getRealTime();
                sentenceList.add(new SentenceDTO(sentenceId, sentenceOrder, sentenceContent));
                redisTemplateImpl.saveSentenceInformation(sentenceId, new RedisSentenceDTO(paragraphId, paragraphOrder, Long.valueOf(index), sentenceContent, RealTimeUtils.convertMsToTimeFormat(timestamp), com.twentythree.peech.script.dto.NowStatus.RealTime));
            }
            paragraphList.add(new STTParagraphDTO(paragraphId++, paragraphOrder++, RealTimeUtils.convertMsToTimeFormat(timestamp), NowStatus.RealTime, sentenceList));
        }

        return new STTScriptResponseDTO(paragraphList);
    }

}


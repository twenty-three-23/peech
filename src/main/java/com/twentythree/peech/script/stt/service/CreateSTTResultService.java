
package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.stt.dto.EditClovaSpeechSentenceVO;
import com.twentythree.peech.script.stt.dto.STTResultSentenceDto;
import com.twentythree.peech.script.stt.dto.SentenceVO;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTResultResponseDto;
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

    public STTResultResponseDto createSTTResultResponseDto(ClovaResponseDto clovaResponseDto, List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList, List<SentenceVO> sentenceVOList, ParagraphDivideResponseDto paragraphDivideResponseDto) {

        int timestamp;
        long paragraphId = 1L;

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
            String content = sentenceVO.sentenceEntity().getSentenceContent();

            STTResultSentenceDto sttResultSentenceDto = new STTResultSentenceDto(id, content, sentencesRealTimeList.get(i));
            sttResultSentenceDtoList.add(sttResultSentenceDto);
        }

        // paragraphDevideResponseDto에서 문단별로 나눈 index를 가져옴
        List<List<Integer>> paragraphNumber = paragraphDivideResponseDto.getResult().getSpan();

        // 문단별 측정된 시간 저장
        List<LocalTime> paragraphRealTime = new ArrayList<>();

        List<STTResultResponseDto.Paragraph> paragraphList = new ArrayList<>();

        for (List<Integer> paragraph : paragraphNumber) {
            List<STTResultResponseDto.Paragraph.Sentence> sentenceList = new ArrayList<>();
            timestamp = 0;
            for (Integer index : paragraph) {

                Long sentenceId = sttResultSentenceDtoList.get(index).getSentenceId();
                String sentenceContent = sttResultSentenceDtoList.get(index).getContent();
                timestamp += sttResultSentenceDtoList.get(index).getRealTime();
                sentenceList.add(new STTResultResponseDto.Paragraph.Sentence(sentenceId, sentenceContent));
            }
            paragraphList.add(new STTResultResponseDto.Paragraph(paragraphId++, sentenceList, RealTimeUtils.convertMsToTimeFormat(timestamp)));
        }

        return new STTResultResponseDto(totalRealTime, paragraphList);
    }

}


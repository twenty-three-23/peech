package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.stt.dto.AddSentenceInformationVO;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.utils.MergeWordListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddRealTimeToSentenceService {
    public List<AddSentenceInformationVO> addRealTimeToSentence(ClovaResponseDto clovaResponseDto, ParagraphDivideResponseDto paragraphDivideResponseDto) {

        // AddSentenceInformationVO 객체 리스트를 만들어야 함
        List<AddSentenceInformationVO> AddSentenceInformationVOList = new ArrayList<>();

        // 문단 나누기 결과 가져오기
        List<String> sentenceList = paragraphDivideResponseDto.getResult().getParagraphList().stream().flatMap(List::stream).toList();

        // clovaResponseDto에서 wordList 가져오기
        List<List<Object>> wordList = MergeWordListUtils.mergeWordList(clovaResponseDto);

        int startIndex = 0;
        int endIndex = 0;
        int startStamp = 0;
        int endStamp = 0;
        long sentenceOrder = 0L;

        // 문장의 단어 개수를 기반으로 문장의 시작 시간과 종료 시간을 계산
        for(String sentence : sentenceList){
            startStamp = (int) wordList.get(startIndex).get(0);
            // 해당 문장의 단어 개수를 알아야함
            int wordCount = sentence.split(" ").length;
            endIndex = startIndex + wordCount - 1;
            endStamp = (int) wordList.get(endIndex).get(1);
            int duration = endStamp - startStamp;

            AddSentenceInformationVO AddSentenceInformationVO = new AddSentenceInformationVO(++sentenceOrder, sentence, duration);
            AddSentenceInformationVOList.add(AddSentenceInformationVO);
            startIndex = endIndex + 1;
        }

        return AddSentenceInformationVOList;
    }
}

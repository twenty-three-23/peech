package com.twentythree.peech.script.stt.utils;

import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;

import java.util.ArrayList;
import java.util.List;

public class MergeWordListUtils {

    public static List<List<Object>> mergeWordList(ClovaResponseDto clovaResponseDto) {
        List<List<Object>> mergedWordList = new ArrayList<>();

        for(ClovaResponseDto.Segment segment : clovaResponseDto.getSentences()) {
            List<List<Object>> words = segment.getWords();
            mergedWordList.addAll(words);
        }
        return mergedWordList;
    }

}

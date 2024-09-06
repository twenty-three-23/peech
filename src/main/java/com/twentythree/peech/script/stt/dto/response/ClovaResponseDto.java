package com.twentythree.peech.script.stt.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.twentythree.peech.script.stt.exception.STTException;
import com.twentythree.peech.script.stt.exception.STTExceptionCode;
import com.twentythree.peech.script.stt.utils.RealTimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // response에서 필요하지 않은 것은 무시
@NoArgsConstructor
public class ClovaResponseDto {

    @JsonProperty("sentences")
    @JsonAlias("segments")
    private List<Segment> sentences;

    @JsonProperty("fulltext")
    @JsonAlias("text")
    private String fullText;

    @JsonProperty("totalRealTime")
    public LocalTime getTotalRealTime() {
        if(sentences == null || sentences.isEmpty()){
            throw new STTException(STTExceptionCode.NOT_EXIST_VOICE_CONTENT);
        }
        int last = sentences.size()-1;

        // 마지막 문장 가져오기
        Segment lastSegment = getSentences().get(last);
        // 마지막 문장의 마지막 단어 객체가져오기
        List<Object> lastWord = lastSegment.words.get(lastSegment.words.size()-1);

        return RealTimeUtils.convertTimeStampToTimeFormat((int) lastWord.get(1));
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {

        @JsonProperty("words")
        private List<List<Object>> words;


    }

    public ClovaResponseDto(List<Segment> sentences, String fullText) {
        this.sentences = sentences;
        this.fullText = fullText;
    }
}

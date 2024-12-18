package com.twentythree.peech.script.stt.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParagraphDivideResponseDto {

    @JsonProperty("result")
    private Result result;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("paragraphList")
        @JsonAlias("topicSeg")
        private List<List<String>> paragraphList;

        @JsonProperty("span")
        private List<List<Integer>> span;

    }
}

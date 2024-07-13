package com.twentythree.peech.script.stt.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParagraphDivideResponseDto {

    @JsonProperty("result")
    private Result result;

    @Getter
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("paragraphs")
        @JsonAlias("topicSeg")
        private List<List<String>> paragraphs;

        @JsonProperty("span")
        private List<List<Integer>> span;

    }
}

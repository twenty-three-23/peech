package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalTime;


@Data
@AllArgsConstructor
public class RedisSentenceDTO {

    private Long paragraphId;
    private Long paragraphOrder;
    private Long sentenceOrder;
    private String sentenceContent;
    private LocalTime time;
    private boolean isChanged;

    public String toStringIsChanged() {
        return isChanged ? "true" : "false";
    }

    public RedisSentenceDTO(String paragraphId, String paragraphOrder, String sentenceOrder, String sentenceContent, String time, String isChanged) {
        this.paragraphId = Long.parseLong(paragraphId);
        this.paragraphOrder = Long.parseLong(paragraphOrder);
        this.sentenceOrder = Long.parseLong(sentenceOrder);
        this.sentenceContent = sentenceContent;
        this.time = LocalTime.parse(time);
        this.isChanged = Boolean.parseBoolean(isChanged);
    }

}


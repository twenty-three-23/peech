package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisSentenceDTO {

    private Long paragraphId;
    private Long paragraphOrder;
    private Long sentenceOrder;
    private String sentenceContent;
    private LocalTime time;
    private NowStatus nowStatus;

    public RedisSentenceDTO(String paragraphId, String paragraphOrder, String sentenceOrder, String sentenceContent, String time, String nowStatus) {
        this.paragraphId = Long.parseLong(paragraphId);
        this.paragraphOrder = Long.parseLong(paragraphOrder);
        this.sentenceOrder = Long.parseLong(sentenceOrder);
        this.sentenceContent = sentenceContent;
        this.time = LocalTime.parse(time);
        this.nowStatus = NowStatus.parse(nowStatus);
    }

}


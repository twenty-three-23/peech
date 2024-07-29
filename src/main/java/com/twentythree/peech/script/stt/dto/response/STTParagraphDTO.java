package com.twentythree.peech.script.stt.dto.response;

import com.twentythree.peech.script.dto.NowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class STTParagraphDTO {

    private Long paragraphId;
    private Long paragraphOrder;
    private String measurementResult;
    private LocalTime time;
    private LocalTime startTime;
    private LocalTime endTime;
    private NowStatus nowStatus;
    private List<SentenceDTO> sentences;
}

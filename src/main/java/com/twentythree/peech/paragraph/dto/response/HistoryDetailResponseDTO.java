package com.twentythree.peech.paragraph.dto.response;

import com.twentythree.peech.paragraph.dto.HistoryParagraphDTO;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
public class HistoryDetailResponseDTO {
    private LocalTime totalRealTime;
    private List<HistoryParagraphDTO> script;
}

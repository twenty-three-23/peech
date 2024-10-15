package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.paragraph.dto.HistoryParagraphDTO;
import com.twentythree.peech.script.dto.ParagraphExpectedTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScriptExpectedTimeDTO {
    private LocalTime totalExpectedTime;
    private List<ParagraphExpectedTimeDTO> script;
}

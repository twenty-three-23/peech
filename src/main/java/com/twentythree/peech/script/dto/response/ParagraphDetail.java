package com.twentythree.peech.script.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphDetail {
    LocalTime realTimePerParagraph;
    List<String> sentences;
}

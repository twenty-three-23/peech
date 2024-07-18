package com.twentythree.peech.script.stt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class STTScriptResponseDTO {

    LocalTime totalRealTime;
    List<STTParagraphDTO> script;
}

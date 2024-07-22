package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.ModifiedParagraphDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ModifyScriptResponseDTO {
    private Long scriptId;
    private LocalTime totalTime;
    private List<ModifiedParagraphDTO> script;
}

package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.ParagraphAndExpectedTimeDTO;
import lombok.Data;

import java.util.List;

@Data
public class ModifiedScriptResponseDTO {

    private List<ParagraphAndExpectedTimeDTO> paragraphs;

}

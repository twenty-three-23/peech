package com.twentythree.peech.script.dto.request;

import com.twentythree.peech.script.dto.ParagraphDTO;
import lombok.Data;

import java.util.List;

@Data
public class ModifiedScriptRequestDTO {

    List<ParagraphDTO> paragraphs;
}

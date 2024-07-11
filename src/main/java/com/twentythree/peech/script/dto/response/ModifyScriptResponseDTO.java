package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.ModifiedParagraphDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ModifyScriptResponseDTO {
    List<ModifiedParagraphDTO> script;
}

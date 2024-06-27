package com.twentythree.peech.script.dto.response;


import com.twentythree.peech.script.dto.MajorScriptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MajorScriptsResponseDTO {
    List<MajorScriptDTO> majorScripts;
}

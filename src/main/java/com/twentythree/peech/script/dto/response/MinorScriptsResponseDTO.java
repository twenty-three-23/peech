package com.twentythree.peech.script.dto.response;


import com.twentythree.peech.script.dto.MinorScriptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MinorScriptsResponseDTO {
    List<MinorScriptDTO> minorScripts;
}

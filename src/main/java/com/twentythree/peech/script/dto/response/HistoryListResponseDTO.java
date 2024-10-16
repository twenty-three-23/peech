package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.ScriptDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HistoryListResponseDTO {
    private List<ScriptDTO> scriptHistoryList;
}

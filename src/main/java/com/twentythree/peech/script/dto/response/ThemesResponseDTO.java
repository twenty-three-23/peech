package com.twentythree.peech.script.dto.response;


import com.twentythree.peech.script.dto.ThemeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ThemesResponseDTO {

    private List<ThemeDTO> themes;


}

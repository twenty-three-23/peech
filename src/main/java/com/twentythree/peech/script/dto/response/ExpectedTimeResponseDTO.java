package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.paragraphIdToExpectedTime;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ExpectedTimeResponseDTO {

    private LocalTime expectedTimeByScript;
    private List<paragraphIdToExpectedTime> expectedTimeByParagraphs;

}

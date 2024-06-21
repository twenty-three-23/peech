package com.twentythree.peech.common.dto.response;

import com.twentythree.peech.common.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GPTResponse {
    private List<Choice> choices;


    @Data
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;

    }
}

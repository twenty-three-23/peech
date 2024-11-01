package com.twentythree.peech.analyzescript.dto.request;

import com.twentythree.peech.common.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GPTRequest {
    private String model;
    private List<Message> messages;
}

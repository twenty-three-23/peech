package com.twentythree.peech.interviewquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class InterviewQuestionResponseDTO {
    private List<String> interviewQuestions;
}

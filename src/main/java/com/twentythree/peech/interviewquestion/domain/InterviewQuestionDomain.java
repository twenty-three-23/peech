package com.twentythree.peech.interviewquestion.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
@Builder
public class InterviewQuestionDomain {
    private Long scriptId;
    private String scriptContent;

    private List<String> interviewQuestions;

    public static InterviewQuestionDomain ofScript(Long scriptId, String scriptContent) {
        return InterviewQuestionDomain.builder()
                .scriptId(scriptId)
                .scriptContent(scriptContent)
                .build();
    }
}

package com.twentythree.peech.interviewquestion.presentation;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.interviewquestion.application.InterviewQuestionService;
import com.twentythree.peech.interviewquestion.dto.InterviewQuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class InterviewQusetionControlloer {
    private final InterviewQuestionService interviewQuestionService;

    @GetMapping("api/v2/themes/{themeId}/scripts/{scriptId}/interview-questions")
    public InterviewQuestionResponseDTO getInterviewQuestionsByScriptId(@PathVariable Long scriptId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return InterviewQuestionResponseDTO.of(interviewQuestionService.getInterviewQuestionsByScriptId(scriptId, userId));
    }
}

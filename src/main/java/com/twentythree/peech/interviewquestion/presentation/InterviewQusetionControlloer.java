package com.twentythree.peech.interviewquestion.presentation;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.interviewquestion.application.InterviewQuestionService;
import com.twentythree.peech.interviewquestion.dto.InterviewQuestionRequestDTO;
import com.twentythree.peech.interviewquestion.dto.InterviewQuestionResponseDTO;
import com.twentythree.peech.meta.conversionapi.annotation.MetaEventTrigger;
import com.twentythree.peech.meta.conversionapi.eventhandler.event.FeatureType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class InterviewQusetionControlloer {
    private final InterviewQuestionService interviewQuestionService;

    @MetaEventTrigger(name = FeatureType.EXPECTED_QUESTION_LIST)
    @GetMapping("api/v2/themes/{themeId}/scripts/{scriptId}/interview-questions")
    public InterviewQuestionResponseDTO getInterviewQuestionsByScriptId(@PathVariable Long scriptId) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return InterviewQuestionResponseDTO.of(interviewQuestionService.getInterviewQuestionsByScriptId(scriptId, userId));
    }

    @MetaEventTrigger(name = FeatureType.EXPECTED_QUESTION_LIST)
    @PostMapping("api/v2/interview-questions")
    public InterviewQuestionResponseDTO getInterviewQuestionsByScript(@RequestBody InterviewQuestionRequestDTO interviewQuestionRequestDTO) {
        Long userId = SecurityContextHolder.getContextHolder().getUserId();

        return InterviewQuestionResponseDTO.of(interviewQuestionService.getInterviewQuestionsByScript(interviewQuestionRequestDTO.getScriptContent()));
    }
}
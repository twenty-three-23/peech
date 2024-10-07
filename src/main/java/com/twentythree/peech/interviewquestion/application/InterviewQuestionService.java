package com.twentythree.peech.interviewquestion.application;

import com.twentythree.peech.interviewquestion.domain.InterviewQuestionDomain;
import com.twentythree.peech.interviewquestion.domain.InterviewQuestionFetcher;
import com.twentythree.peech.interviewquestion.domain.InterviewQuestionPredictor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InterviewQuestionService {

    private final UserValidator userValidator;
    private final InterviewQuestionPredictor interviewQuestionPredictor;
    private final InterviewQuestionFetcher interviewQuestionFetcher;

    public List<String> getInterviewQuestionsByScriptId(Long scriptId, Long userId) {
        if(!userValidator.isOwnerOfScriptByUserId(userId, scriptId)) {
            throw new IllegalArgumentException("자기소개서의 주인이 아닙니다.");
        }

        InterviewQuestionDomain interviewQuestionDomain = interviewQuestionFetcher.fetchInterviewQuestion(scriptId);
        interviewQuestionDomain = interviewQuestionPredictor.predictInterviewQuestion(interviewQuestionDomain);

        return interviewQuestionDomain.getInterviewQuestions();
    }

    public List<String> getInterviewQuestionsByScript(String scriptContent) {
        InterviewQuestionDomain interviewQuestionDomain = InterviewQuestionDomain.of(0L, scriptContent, null);
        interviewQuestionDomain = interviewQuestionPredictor.predictInterviewQuestion(interviewQuestionDomain);

        return interviewQuestionDomain.getInterviewQuestions();
    }
}

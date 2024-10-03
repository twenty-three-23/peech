package com.twentythree.peech.interviewquestion.infrastructure;

import com.twentythree.peech.interviewquestion.domain.InterviewQuestionDomain;
import com.twentythree.peech.interviewquestion.domain.InterviewQuestionFetcher;
import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.repository.ScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InterviewQuestionInfra implements InterviewQuestionFetcher {

    private final ScriptRepository scriptRepository;

    @Override
    public InterviewQuestionDomain fetchInterviewQuestion(Long scriptId) {
        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(
                () -> new IllegalArgumentException("Script not found")
        );

        return InterviewQuestionDomain.ofScript(scriptEntity.getScriptId(), scriptEntity.getScriptContent());
    }
}

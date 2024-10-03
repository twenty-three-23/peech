package com.twentythree.peech.interviewquestion.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.interviewquestion.client.InterviewQuestionGPT;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class InterviewQuestionPredictor {
    private static final Logger log = LoggerFactory.getLogger(InterviewQuestionPredictor.class);

    private final InterviewQuestionGPT interviewQuestionPredictorGPT;

    public InterviewQuestionDomain predictInterviewQuestion(InterviewQuestionDomain interviewQuestionDomain) {
        String scriptContent = interviewQuestionDomain.getScriptContent();

        String prompt = "아래의 글은 자기소개글 입니다. 이 자기소개를 토대로 수준 높은 질문을 4개를 만들어서 [\n" +
                "\"면접질문 내용\", \"면접질문 내용\"] 이런형식으로 만들어줘 \n"  + scriptContent;

        GPTRequest gptRequest = new GPTRequest("gpt-4o-mini", prompt);
        GPTResponse gptResponse = interviewQuestionPredictorGPT.predictQuestion(gptRequest);

        String result = gptResponse.getChoices(). get(0).getMessage().getContent();

        try {
            List<String> interviewQuestions = new ObjectMapper().readValue(result, new TypeReference<List<String>>() {});
            interviewQuestionDomain.setInterviewQuestions(interviewQuestions);
            return interviewQuestionDomain;
        } catch (JsonProcessingException e) {
            log.error("면접 질문 생성 실패", e);
            throw new RuntimeException("면접 질문 생성 실패");
        }
    }
}

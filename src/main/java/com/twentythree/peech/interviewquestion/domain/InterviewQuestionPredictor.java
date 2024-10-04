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

        String prompt = "아래의 글은 회사 면접을 위한 자기소개 글입니다\n" +
                "\n" +
                "면접자의 자기소개에 언급된 경험을 바탕으로 회사에서 어떻게 역량을 발휘할 수 있을지 물어보는 질문을 2개를 만들어야 합니다.\n" +
                "추가로 면접자의 자기소개에 언급된 내용을 바탕으로 회사에서 어떤게 응용할 수 있을지 물어보는 질문을 2개 추가로 만들어 주세요\n" +
                "\n" +
                "답변 형식은 무조건 다른 어떤한 것도 추가하지 말고 [\"면접질문 내용\", \"면접질문 내용\"] 의 형식으로 만들어야 합니다.\n" +
                "\n" +
                "자기소개:\n" +
                scriptContent;

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

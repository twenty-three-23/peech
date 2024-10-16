package com.twentythree.peech.interviewquestion.client;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "interviewQuestionGPT",
        url = "https://api.openai.com/")
public interface InterviewQuestionGPT {

    @PostMapping("v1/chat/completions")
    GPTResponse predictQuestion(GPTRequest gptRequest);
}

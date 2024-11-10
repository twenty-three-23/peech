package com.twentythree.peech.analyzescript.domain;

import com.twentythree.peech.analyzescript.client.AnalyzeScriptGPT;
import com.twentythree.peech.analyzescript.dto.request.GPTRequest;
import com.twentythree.peech.aop.annotation.Trace;
import com.twentythree.peech.common.dto.Message;
import com.twentythree.peech.common.dto.response.GPTResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Async
@RequiredArgsConstructor
@Component
public class AnalyzeScriptPredictor {

    @Value("${gpt.prompt.system}")
    private String gptSystemPrompt;

    private final AnalyzeScriptGPT analyzeScriptGPT;

    public CompletableFuture<String> requestAnalyzeScript(String scriptContent) {
        List<Message> messages = new ArrayList<>();

        String systemPrompt = gptSystemPrompt;
        messages.add(new Message("system", systemPrompt));

        String userPrompt = "아래의 글은 회사 면접을 위한 자기소개서입니다. 내용을 보고 자기소개서를 분석해주세요.\n"+
                "자기소개서: " +
                scriptContent;
        messages.add(new Message("user", userPrompt));

        GPTRequest gptRequest = new GPTRequest("gpt-4o-mini", messages);
        GPTResponse gptResponse = analyzeScriptGPT.analyzeScript(gptRequest);
        String result = gptResponse.getChoices().get(0).getMessage().getContent();

        return CompletableFuture.completedFuture(result);
    }

}

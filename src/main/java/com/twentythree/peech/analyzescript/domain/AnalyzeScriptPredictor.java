package com.twentythree.peech.analyzescript.domain;

import com.twentythree.peech.analyzescript.client.AnalyzeScriptGPT;
import com.twentythree.peech.analyzescript.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.Message;
import com.twentythree.peech.common.dto.response.GPTResponse;
import lombok.RequiredArgsConstructor;
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

        String userPrompt = "분석 결과에 대한 응답값은 아래의 형식에 맞게 작성해야합니다.\n" +
                "1. 전반적인 분석결과: 입력받은 자기소개서를 분석한 결과를 요약하여 작성해주세요\n" +
                "2. 세부 분석결과 : 자기소개서 내용을 세부적으로 분석한 결과를 작성해주세요. 이때 작성방식은 아래와 같으며, 카테고리는 최대 5개를 넘기지 않도록 합니다.\n" +
                "\t[분석된 항목에 대한 카테고리 이름]\n" +
                "\t\t- 분석 내용: 해당 항목에서 드러난 내용에 대한 강점이 있다면 이를 포함하여 분석 내용을 간결하게 제시해 주세요.\n" +
                "\t\t- 개선점 및 고려할 사항: 이 항목에서 보완할 수 있는 부분이 있다면 간결하게 제시해 주세요. " +
                "보완할 내용이 없다면, 면접관으로부터 질문으로 받을 수 있을 만한 부분들을 제시해 주거나 큰 보완사항은 없습니다라고 응답해도 좋습니다.\n"+
                "자기소개서: " +
                scriptContent;
        messages.add(new Message("user", userPrompt));

        GPTRequest gptRequest = new GPTRequest("gpt-4o-mini", messages);
        GPTResponse gptResponse = analyzeScriptGPT.analyzeScript(gptRequest);
        String result = gptResponse.getChoices().get(0).getMessage().getContent();

        return CompletableFuture.completedFuture(result);
    }

}

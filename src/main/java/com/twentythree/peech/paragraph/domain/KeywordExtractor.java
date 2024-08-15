package com.twentythree.peech.paragraph.domain;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.paragraph.client.GPTClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class KeywordExtractor {

    private final GPTClient gptClient;

    public List<String> extractKeywords(String text) {

        String prompt = "다음 문단에서 핵심이 되는 단어를 2가지 뽑아서 \'안녕 하세요\'와 같이 두 단어를 한칸 띄워서 답해' " + text;
        String model = "gpt-4o-mini";

        GPTRequest gptRequest = new GPTRequest(model, prompt);

        GPTResponse gptResponse = gptClient.extractKeyWords(gptRequest);
        String result = gptResponse.getChoices().get(0).getMessage().getContent();
        List<String> keyWords = List.of(result.split(" "));
        return keyWords;
    }
}

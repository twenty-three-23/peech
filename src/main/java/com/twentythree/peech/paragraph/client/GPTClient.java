package com.twentythree.peech.paragraph.client;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "GPTClient",
        url = "https://api.openai.com/")
public interface GPTClient {

    @PostMapping("v1.1/chat/completions")
    GPTResponse extractKeyWords(GPTRequest gptRequest);
}

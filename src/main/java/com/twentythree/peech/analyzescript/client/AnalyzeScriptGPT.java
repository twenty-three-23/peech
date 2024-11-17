package com.twentythree.peech.analyzescript.client;

import com.twentythree.peech.analyzescript.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "analyzeScriptGPT",
        url = "https://api.openai.com/")
public interface AnalyzeScriptGPT {

    @PostMapping("v1/chat/completions")
    GPTResponse analyzeScript(GPTRequest gptRequest);
}

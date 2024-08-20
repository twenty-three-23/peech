package com.twentythree.peech.paragraph.client;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Value("${gpt.api.key}")
    private String gptApiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (requestTemplate.method() == Request.HttpMethod.POST.name()) {
            requestTemplate.header("Authorization", "Bearer " + gptApiKey);
        }
    }
}

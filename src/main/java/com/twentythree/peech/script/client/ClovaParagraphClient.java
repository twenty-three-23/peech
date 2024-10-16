package com.twentythree.peech.script.client;

import com.twentythree.peech.script.dto.request.RequestClovaDTO;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(
        name = "ClovaParagraphClient",
        url = "https://clovastudio.apigw.ntruss.com${clova.divide-sentence-api.url}"
)
public interface ClovaParagraphClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ParagraphDivideResponseDto divideScript(
            @RequestHeader("X-NCP-CLOVASTUDIO-API-KEY") String apiKey,
            @RequestHeader("X-NCP-APIGW-API-KEY") String apiGWApiKey,
            @RequestBody RequestClovaDTO body);
}

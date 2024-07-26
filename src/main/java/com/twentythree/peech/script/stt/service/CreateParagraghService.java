package com.twentythree.peech.script.stt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CreateParagraghService {

    @Value("${clova.divide-sentence-api.key}")
    private String apiKey;

    @Value("${clova.divide-sentence-api.gw-key}")
    private String apiGWApiKey;

    @Value("${clova.divide-sentence-api.url}")
    private String apiUrl;

    private final WebClient.Builder webClientBuilder;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // 문단 나누기 api
    public CompletableFuture<ParagraphDivideResponseDto> requestClovaParagraphApi(String totalText) {
        // HTTP 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        httpHeaders.set("X-NCP-APIGW-API-KEY", apiGWApiKey);


        Map<String, String> body = new HashMap<>();
        body.put("text", totalText);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTexts = null;
        try {
            jsonTexts = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new IllegalArgumentException("json 변환 중 오류가 발생했습니다.");
        }

        log.info("jsonTexts = " + jsonTexts);

        // Webclient를 이용한 비동기 통신 - 문단 나누기 api 호출
        return webClientBuilder.baseUrl("https://clovastudio.apigw.ntruss.com").build()
                .post()
                .uri(apiUrl)
                .headers(h -> h.addAll(httpHeaders))
                .bodyValue(jsonTexts)
                .retrieve()
                .bodyToMono(ParagraphDivideResponseDto.class)
                .toFuture();
    }
}

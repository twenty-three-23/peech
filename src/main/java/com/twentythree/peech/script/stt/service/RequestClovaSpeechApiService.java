package com.twentythree.peech.script.stt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestClovaSpeechApiService {

        @Value("${clova.speech-api.url}")
        private String clovaSpeechApiUrl;

        @Value("${clova.speech-api.secret}")
        private String clovaSpeechApiSecret;

        private final WebClient.Builder webClientBuilder;

        public Mono<ClovaResponseDto> requestClovaSpeechApi(STTRequestDto request){
                // client에서 받은 파일을 임시파일로 변환
                File tempFile;
                try {
                        tempFile = File.createTempFile("temp", request.media().getOriginalFilename());
                        request.media().transferTo(tempFile);
                }catch (Exception e){
                        throw new IllegalArgumentException("파일 변환 중 오류가 발생했습니다.");
                }

                try{
                        // HTTP 헤더 설정
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.set("Accept", "application/json;UTF-8");
                        httpHeaders.set("X-CLOVASPEECH-API-KEY", clovaSpeechApiUrl);
                        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                        // HTTP body에 보낼 정보
                        Map<String, String> params = new HashMap<>();
                        params.put("language", "ko-KR");
                        params.put("completion", "sync");

                        ObjectMapper objectMapper = new ObjectMapper();
                        String paramsJson = objectMapper.writeValueAsString(params);
                        HttpHeaders jsonHeader = new HttpHeaders();
                        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> jsonEntity = new HttpEntity<>(paramsJson, jsonHeader);

                        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                        body.add("media", new FileSystemResource(tempFile));
                        body.add("params", jsonEntity);

                        // HTTP 요청
                        return webClientBuilder.baseUrl(clovaSpeechApiUrl).build()
                                .post()
                                .uri("/recognizer/upload")
                                .headers(h -> h.addAll(httpHeaders))
                                .bodyValue(body)
                                .retrieve()
                                .bodyToMono(ClovaResponseDto.class);

                } catch (Exception e) {
                        throw new IllegalArgumentException("Clova Speech API 요청에 실패했습니다.");
                }
        }
}

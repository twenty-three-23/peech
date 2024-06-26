package com.twentythree.peech.script.service;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.SaveScriptDTO;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.script.repository.ScriptRepository;
import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.script.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.time.LocalTime;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScriptService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;
    private final ScriptRepository scriptRepository;
    private final ThemeRepository themeRepository;
    private final VersionRepository versionRepository;

    @Transactional
    public SaveScriptDTO saveInputScript(Long themeId, String[] paragraphs) {

        String script = String.join("\n", paragraphs);

        LocalTime expectedTime = ScriptUtils.calculateExpectedTime(script);

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInthemeId(themeId);

        VersionEntity versionEntity = VersionEntity.ofCreateInputScriptVersion(latestMajorVersion, ThemeEntity);
        ScriptEntity scriptEntity = ScriptEntity.ofCreateInputScript(versionEntity, script, expectedTime, InputAndSttType.INPUT);

        versionRepository.save(versionEntity);
        scriptRepository.save(scriptEntity);

        return new SaveScriptDTO(scriptEntity, ScriptUtils.calculateExpectedTime(script));
    }

    public LocalTime getInputExpectedScriptTime(Long scriptId) {
        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        return scriptEntity.getTotalExpectTime();
    }

    // GPT

    public String[] sliceScriptToParagraph(String text){

        String prompt = "아래의 스크립트를 문장으로 나누고 그 문장들을 문단으로 다시 합쳐줘 " + text;

        GPTRequest request = new GPTRequest(model, prompt);
        GPTResponse Response = restTemplate.postForObject(apiURL, request, GPTResponse.class);
        String result = Response.getChoices().get(0).getMessage().getContent();

        String[] paragraphs = result.split("\n");

        return paragraphs;
    }

}

package com.twentythree.peech.script.service;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.script.domain.InputAndSttType;
import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.VersionPk;
import com.twentythree.peech.script.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;


@Service
public class ScriptService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;
    private final ScriptRepository scriptRepository;


    @Autowired
    public ScriptService(ScriptRepository scriptRepository, RestTemplate restTemplate) {
        this.scriptRepository = scriptRepository;
        this.restTemplate = restTemplate;
    }


    public ScriptEntity saveInputScript(Long packageId, String text) {
        LocalTime expectedTime = calculateExpectedTime(text);


        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInPackageId(packageId);

        VersionPk versionPk;

        if (latestMajorVersion == null) {
            versionPk = new VersionPk(1L, 0L);
        } else {
            versionPk = new VersionPk(latestMajorVersion + 1, 0L);
        }

        ScriptEntity scriptEntity = ScriptEntity.ofCreateInputScript(versionPk, text, expectedTime, InputAndSttType.INPUT);



        scriptRepository.save(scriptEntity);
        return scriptEntity;
    }

    public String[] saveScriptToParagraph(String text) {
        String[] paragraphs = sliceScriptToParagraph(text);


    }


    // 메소드

    private LocalTime calculateExpectedTime(String text) {

        final float DEFAULT_TIME_PER_WORD_SECOND = 1.75F; //LocalTime.of(0,0,1, 750000000);

        String[] words = text.split(" ");
        int wordsCount = words.length;

        float expectedTimeToSecond = wordsCount * DEFAULT_TIME_PER_WORD_SECOND;

        LocalTime expectedTime = transferSeoondToLocalTime(expectedTimeToSecond);

        return expectedTime;
    }

    private LocalTime transferSeoondToLocalTime(float time) {

        int second = (int) time;

        int minute = (int) (second / 60);
        int hour = (int) (second / (60 * 60));

        int minuteSet = minute - (hour * 60);
        int secondSet = second - (minuteSet * 60);

        return LocalTime.of(hour, minuteSet, secondSet);
    }


    public String[] sliceScriptToParagraph(String text){

        String prompt = "아래의 스크립트를 문장으로 나누고 그 문장들을 문단으로 다시 합쳐줘 " + text;

        GPTRequest request = new GPTRequest(model, prompt);
        GPTResponse Response = restTemplate.postForObject(apiURL, request, GPTResponse.class);
        String result = Response.getChoices().get(0).getMessage().getContent();

        String[] paragraphs = result.split("\n");

        return paragraphs;
    }
}

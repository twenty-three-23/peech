package com.twentythree.peech.script.service;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.MajorScriptDTO;
import com.twentythree.peech.script.dto.MinorScriptDTO;
import com.twentythree.peech.script.dto.SaveScriptDTO;
import com.twentythree.peech.script.dto.response.MajorScriptsResponseDTO;
import com.twentythree.peech.script.dto.response.MinorScriptsResponseDTO;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.script.repository.ScriptRepository;
import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.script.repository.VersionRepository;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;
import org.springframework.web.client.RestTemplate;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


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

    public SaveSTTScriptVO saveSTTScriptVO(Long themeId, Long scriptId, ClovaResponseDto clovaResponseDto) {

        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        // 해당 스크립트의 MajorVersion과 MinorVersion을 가져옴
        Long majorVersion = scriptEntity.getVersion().getMajorVersion();

        Long minorVersion = scriptEntity.getVersion().getMinorVersion();

        VersionEntity versionEntity = VersionEntity.ofCreateSTTScriptVersion(majorVersion, minorVersion, ThemeEntity);


        return saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity);
    }

    @Transactional
    // Version과 SCRIPT Entity 저장 로직은 공통이므로 묶어서 처리
    public SaveSTTScriptVO saveSTTScriptEntity(Long themeId, ClovaResponseDto clovaResponseDto, VersionEntity versionEntity) {


        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        ScriptEntity sttScriptEntity = ScriptEntity.ofCreateSTTScript(versionEntity, clovaResponseDto.getFullText(), clovaResponseDto.getTotalRealTime(), InputAndSttType.STT);

        versionRepository.save(versionEntity);
        scriptRepository.save(sttScriptEntity);

        return new SaveSTTScriptVO(sttScriptEntity, clovaResponseDto.getTotalRealTime());
    }

    public SaveSTTScriptVO saveSTTScriptVO(Long themeId, ClovaResponseDto clovaResponseDto) {

        String script = clovaResponseDto.getFullText();

        LocalTime totalRealTime = clovaResponseDto.getTotalRealTime();

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        VersionEntity versionEntity = VersionEntity.ofCreateSTTScriptVersion(ThemeEntity);

        return saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity);
    }

    public LocalTime getInputExpectedScriptTime(Long scriptId) {
        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        return scriptEntity.getTotalExpectTime();
    }

    public MajorScriptsResponseDTO getMajorScripts(Long themeId) {
        List<ScriptEntity> scripts = scriptRepository.findMajorScriptByThemeId(themeId);
        List<MajorScriptDTO> majorScript = new ArrayList<>();
        for (ScriptEntity script : scripts) {
            majorScript.add(new MajorScriptDTO(script.getScriptId(), script.getVersion().getMajorVersion(), script.getScriptContent(), script.getCreatedAt()));
        }

        return new MajorScriptsResponseDTO(majorScript);
    }

    public MinorScriptsResponseDTO getMinorScripts(Long themeId, Long majorVersion) {
        List<ScriptEntity> scripts = scriptRepository.findMinorScriptByThemeIdAndMajorVersion(themeId, majorVersion);

        List<MinorScriptDTO> minorScripts = new ArrayList<>();

        for (ScriptEntity script : scripts) {
            minorScripts.add(new MinorScriptDTO(script.getVersion().getMinorVersion(), script.getScriptContent(), script.getCreatedAt()));
        }

        return new MinorScriptsResponseDTO(minorScripts);
    }

    // GPT

    public String[] sliceScriptToParagraph(String text){

        String prompt = "아래의 스크립트를 문장으로 나누고 그 문장들을 문단으로 다시 합쳐줘 " + text;

        GPTRequest request = new GPTRequest(model, prompt);
        GPTResponse Response = restTemplate.postForObject(apiURL, request, GPTResponse.class);
        String result = Response.getChoices().get(0).getMessage().getContent();

        String[] paragraphs = result.split("\n");
        System.out.println(paragraphs);

        return paragraphs;
    }

}

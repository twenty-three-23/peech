package com.twentythree.peech.script.service;

import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.script.cache.CacheService;
import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.*;
import com.twentythree.peech.script.dto.response.MajorScriptsResponseDTO;
import com.twentythree.peech.script.dto.response.MinorScriptsResponseDTO;
import com.twentythree.peech.script.dto.response.ModifyScriptResponseDTO;
import com.twentythree.peech.script.repository.*;
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
import reactor.core.publisher.Mono;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final SentenceRepository sentenceRepository;
    private final CacheService scriptRedisRepository;

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

    public Mono<SaveSTTScriptVO> saveSTTScriptVO(Long themeId, Long scriptId, ClovaResponseDto clovaResponseDto) {

        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        // 해당 스크립트의 MajorVersion과 MinorVersion을 가져옴
        Long majorVersion = scriptEntity.getVersion().getMajorVersion();

        Long minorVersion = scriptEntity.getVersion().getMinorVersion();

        VersionEntity versionEntity = VersionEntity.ofCreateSTTScriptVersion(majorVersion, minorVersion, ThemeEntity);


        return Mono.just(saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity));
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

    public Mono<SaveSTTScriptVO> saveSTTScriptVO(Long themeId, ClovaResponseDto clovaResponseDto) {

        String script = clovaResponseDto.getFullText();

        LocalTime totalRealTime = clovaResponseDto.getTotalRealTime();

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        VersionEntity versionEntity = VersionEntity.ofCreateSTTScriptVersion(ThemeEntity);

        return Mono.just(saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity));
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

    @Transactional // Redis는 분산 락을 걸어야 하나?
    public ModifyScriptResponseDTO modifyScriptService(List<ParagraphDTO> modifiedParagraphs, Long scriptId, Long userId) {
        
        Map<ParagraphId, Map<SentenceId, RedisSentenceDTO>> redisSentences = new HashMap<>(); // Redis에서 문단 id별로 문장들을 저장하기 위한 map
        List<Long>                                          sentenceIds    = scriptRedisRepository.findAllByUserKey("user"+userId);

        for (Long sentenceId : sentenceIds) { // Redis에서 가져온 문장들을 문단별로 분리
            RedisSentenceDTO                  sentence                     = scriptRedisRepository.findByKey(sentenceId);
            Map<SentenceId, RedisSentenceDTO> redisSentencesPerParagraphId = redisSentences.get(new ParagraphId(sentence.getParagraphId()));

            redisSentencesPerParagraphId.put(new SentenceId(sentenceId), new RedisSentenceDTO(sentence.getParagraphId(), sentence.getParagraphOrder(), sentence.getSentenceOrder(), sentence.getSentenceContent(), sentence.getTime(), sentence.isChanged()));
        }

        List<ModifiedParagraphDTO> modifiedParagraphList = new ArrayList<>(); // ResponseDTO를 만들기 위함
        
        // 이러면 paragraph domain을 만드는게 맞지 않을까요..
        for (ParagraphDTO modifiedParagraph : modifiedParagraphs) {
                
            List<SentenceDTO> modifiedSentenceList = new ArrayList<>(); // ResponseDTO를 만들기 위함
            
            Long              modifiedParagraphId    = modifiedParagraph.getParagraphId();
            Long              modifiedParagraphOrder = modifiedParagraph.getParagraphOrder();
            List<SentenceDTO> modifiedSentences      = modifiedParagraph.getSentences();

            Map<SentenceId, RedisSentenceDTO> redisSentencesByParagraphId = redisSentences.get(modifiedParagraphId);
            
            Map<SentenceId, RedisSentenceDTO> temporaryRedisList = new HashMap<>(); // redis에 새롭게 저장할 문장들의 임시저장소

            if (redisSentencesByParagraphId.isEmpty()) { // 새로운 문단 생성 domain관점에는 isNewLine(parameter) 이런식이 되어야 할 듯

                LocalTime expectedTimePerParagraph = LocalTime.of(0,0,0,0);

                for (SentenceDTO modifiedSentence : modifiedSentences) {
                    Long      modifiedSentenceId           = modifiedSentence.getSentenceId();
                    Long      modifiedSentenceOrder        = modifiedSentence.getSentenceOrder();
                    String    modifiedSentenceContent      = modifiedSentence.getSentenceContent();
                    LocalTime modifiedSentenceExpectedTime = ScriptUtils.calculateExpectedTime(modifiedSentenceContent);

                    temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphId,
                                                                modifiedParagraphOrder,
                                                                modifiedSentenceOrder,
                                                                modifiedSentenceContent,
                                                                modifiedSentenceExpectedTime,
                                                                true));

                    SentenceDTO sentenceDTO = new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent);
                    modifiedSentenceList.add(sentenceDTO);
                    expectedTimePerParagraph = ScriptUtils.sumLocalTime(expectedTimePerParagraph, modifiedSentenceExpectedTime);

                }
                modifiedParagraphList.add(new ModifiedParagraphDTO(modifiedParagraphId,
                                                                   modifiedParagraphOrder,
                                                                   expectedTimePerParagraph,
                                                                   true,
                                                                   modifiedSentenceList));
                continue; // 새로운 문단을 생성했다면 기존 문장과 비교할 필요가 없다.
            }

            //기존에 존재하던 문단

            boolean isCalculated = false;
            LocalTime timePerParagraph = LocalTime.of(0,0,0,0);

            for (SentenceDTO modifiedSentence : modifiedSentences) {

                Long             modifiedSentenceId      = modifiedSentence.getSentenceId();
                String           modifiedSentenceContent = modifiedSentence.getSentenceContent();
                Long             modifiedSentenceOrder   = modifiedSentence.getSentenceOrder();


                RedisSentenceDTO redisSentence           = redisSentencesByParagraphId.get(new SentenceId(modifiedSentenceId)); // 수정된 문장과 일치하던 문장을 redis에서 가져오기

                if (redisSentence == null) { // 새로운 문장이 추가
                    LocalTime modifiedSentenceExpectedTime = ScriptUtils.calculateExpectedTime(modifiedSentenceContent);

                    temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphId,
                                                                                                    modifiedParagraphOrder,
                            modifiedSentenceOrder,
                                                                                                    modifiedSentenceContent,
                                                                                                    modifiedSentenceExpectedTime,
                                                                                                    true));

                    timePerParagraph = ScriptUtils.sumLocalTime(timePerParagraph, modifiedSentenceExpectedTime);
                    modifiedSentenceList.add(new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent));
                    isCalculated = true;
                    continue; // 새로운 문장이 추가 된 것이면 기준 문장과 비교할 필요가 없다.
                }

                String redisSentenceContent = redisSentence.getSentenceContent();
                
                // 문장을 비교해서 같은 내요이면 수정이 안된것, 다른 내용이면 수정이 된것으로 예상시간을 계산해서 넣는다.
                if (redisSentenceContent.equals(modifiedSentenceContent)) { // 이런 로직이 sentence domain에 들어있어야 하는 것 같다.
                    temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphId,
                                                                                                    modifiedParagraphOrder,
                                                                                                    modifiedSentenceOrder,
                                                                                                    redisSentenceContent,
                                                                                                    redisSentence.getTime(),
                                                                                                    false));
                    timePerParagraph = ScriptUtils.sumLocalTime(timePerParagraph, redisSentence.getTime());
                } else {
                    temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphId, // 위의 class 생성 부분과 겹치는게 많은데 이런것도 공통화를 하고 setter로 다른 부분만 넣어줘야하나?
                                                                                                    modifiedParagraphOrder,
                                                                                                    modifiedSentenceOrder,
                                                                                                    modifiedSentenceContent,
                                                                                                    ScriptUtils.calculateExpectedTime(modifiedSentenceContent),
                                                                                                    true));
                    timePerParagraph = ScriptUtils.sumLocalTime(timePerParagraph, ScriptUtils.calculateExpectedTime(modifiedSentenceContent));
                }
                modifiedSentenceList.add(new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent));
            }
            modifiedParagraphList.add(new ModifiedParagraphDTO(modifiedParagraphId, modifiedParagraphOrder, timePerParagraph, isCalculated, modifiedSentenceList));

            List<Long> newSentenceIds = new ArrayList<>();

            for (Map.Entry<SentenceId, RedisSentenceDTO> redisSentenceMap : temporaryRedisList.entrySet()) {
                Long newSentenceId = redisSentenceMap.getKey().getSentenceId();
                RedisSentenceDTO newSentence = redisSentenceMap.getValue();

                scriptRedisRepository.saveSentenceInfo(newSentenceId, newSentence);
                newSentenceIds.add(newSentenceId);

            }
            scriptRedisRepository.saveSentencesIdList("user"+userId, newSentenceIds);
            
        }
        return new ModifyScriptResponseDTO(modifiedParagraphList);
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

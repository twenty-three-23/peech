package com.twentythree.peech.script.service;

import com.twentythree.peech.aop.annotation.Trace;
import com.twentythree.peech.common.dto.request.GPTRequest;
import com.twentythree.peech.common.dto.response.GPTResponse;
import com.twentythree.peech.script.cache.CacheService;
import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.client.ClovaParagraphClient;
import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.*;
import com.twentythree.peech.script.dto.request.RequestClovaDTO;
import com.twentythree.peech.script.dto.response.*;
import com.twentythree.peech.script.repository.*;
import com.twentythree.peech.script.repository.VersionRepository;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;


import java.time.LocalTime;
import java.util.*;

import static com.twentythree.peech.common.utils.ScriptUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScriptService {

    private final RedisTemplateImpl redisTemplateImpl;
    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;
    private final ScriptRepository scriptRepository;
    private final ThemeRepository themeRepository;
    private final VersionRepository versionRepository;
    private final CacheService scriptRedisRepository;

    @Value("${clova.divide-sentence-api.key}")
    private String clovaApiKey;

    @Value("${clova.divide-sentence-api.gw-key}")
    private String clovaGWApiKey;
    private final ClovaParagraphClient clovaParagraphClient;

    @Transactional
    public SaveScriptDTO saveInputScript(Long themeId, String[] paragraphs) {

        String script = String.join("\n", paragraphs);

        LocalTime expectedTime = calculateExpectedTime(script);

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInthemeId(themeId);

        VersionEntity versionEntity = VersionEntity.ofCreateInputScriptVersion(latestMajorVersion, themeId, ThemeEntity);
        ScriptEntity scriptEntity = ScriptEntity.ofCreateInputScript(versionEntity, script, expectedTime, InputAndSttType.INPUT);

        versionRepository.save(versionEntity);
        scriptRepository.save(scriptEntity);

        return new SaveScriptDTO(scriptEntity, calculateExpectedTime(script));
    }

    @Transactional
    public Mono<SaveSTTScriptVO> saveSTTScriptVO(Long themeId, Long scriptId, ClovaResponseDto clovaResponseDto, LocalTime totalExpectedTime) {

        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        // 해당 스크립트의 MajorVersion과 MinorVersion을 가져옴
        Long majorVersion = scriptEntity.getVersion().getMajorVersion();

        // 입력받은 대본에서 가장 최신의 MinorVersion을 가져옴
        Long latestMinorVersion = versionRepository.findByMaxMinorVersion(themeId, majorVersion);

        VersionEntity versionEntity = VersionEntity.ofCreateSTTScriptVersionAfterInput(majorVersion, latestMinorVersion, ThemeEntity);


        return Mono.just(saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity, totalExpectedTime));
    }

    @Transactional
    // Version과 SCRIPT Entity 저장 로직은 공통이므로 묶어서 처리
    public SaveSTTScriptVO saveSTTScriptEntity(Long themeId, ClovaResponseDto clovaResponseDto, VersionEntity versionEntity, LocalTime totalExpectedTime) {


        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        ScriptEntity sttScriptEntity = ScriptEntity.ofCreateSTTScript(versionEntity, clovaResponseDto.getFullText(), clovaResponseDto.getTotalRealTime(), totalExpectedTime,InputAndSttType.STT);

        versionRepository.save(versionEntity);
        scriptRepository.save(sttScriptEntity);

        return new SaveSTTScriptVO(sttScriptEntity, clovaResponseDto.getTotalRealTime());
    }

    @Transactional
    public Mono<SaveSTTScriptVO> saveSTTScriptVO(Long themeId, ClovaResponseDto clovaResponseDto, LocalTime totalExpectedTime) {

        String script = clovaResponseDto.getFullText();

        LocalTime totalRealTime = clovaResponseDto.getTotalRealTime();

        ThemeEntity ThemeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInthemeId(themeId);

        // 대본 입력이 없는 경우에는 해당 스크립트를 Input script 취급
        VersionEntity versionEntity = VersionEntity.ofCreateInputScriptVersion(latestMajorVersion, themeId, ThemeEntity);

        return Mono.just(saveSTTScriptEntity(themeId, clovaResponseDto, versionEntity, totalExpectedTime));
    }

    public LocalTime getInputExpectedScriptTime(Long scriptId) {
        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        return scriptEntity.getTotalExpectTime();
    }

    public MajorScriptsResponseDTO getMajorScripts(Long themeId) {
        List<ScriptEntity> scripts = scriptRepository.findMajorScriptByThemeId(themeId);

        List<MajorScriptDTO> majorScript = new ArrayList<>();
        for (ScriptEntity script : scripts) {
            int minorScriptsCountPerMajorScript = versionRepository.findMinorScriptsCount(script.getVersion().getMajorVersion());
            majorScript.add(new MajorScriptDTO(script.getScriptId(), script.getVersion().getMajorVersion(), script.getScriptContent(), script.getCreatedAt(), minorScriptsCountPerMajorScript));
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

    @Transactional
    public ModifyScriptResponseDTO modifyScriptService(List<ParagraphDTO> modifiedParagraphs, Long scriptId, Long userId) {

        // start: redis에서 user가 수정 할 수 있는 문장id들을 모두 조회
        List<String> redisSentenceIdsByUserId = scriptRedisRepository.findAllByUserKey("user"+userId);
        log.info("redis redisSentenceIdsByUserId: {}", redisSentenceIdsByUserId);
        // end: redis에서 user가 수정 할 수 있는 문장id들을 모두 조회

        // start: redis에서 user가 수정 할 수 있는 문장들을 가져온뒤 문단별로 합치기
        Map<ParagraphId, Map<SentenceId, RedisSentenceDTO>> redisAllSentencesPerParagraphId = new HashMap<>(); // Redis에서 문단 id별로 문장들을 저장하기 위한 map

        for (String sentenceId : redisSentenceIdsByUserId) {
            RedisSentenceDTO sentence = scriptRedisRepository.findByKey(sentenceId);
            log.info("sentence: {}", sentence);

            Map<SentenceId, RedisSentenceDTO> redisSentencesPerSentenceIdByParagraphId = redisAllSentencesPerParagraphId.get(new ParagraphId(sentence.getParagraphId()));
            if (redisSentencesPerSentenceIdByParagraphId == null) { // 문단이 새로 생성 될 때
                redisSentencesPerSentenceIdByParagraphId = new HashMap<>();
                redisSentencesPerSentenceIdByParagraphId.put(new SentenceId(sentenceId), new RedisSentenceDTO(sentence.getParagraphId(), sentence.getParagraphOrder(), sentence.getSentenceOrder(), sentence.getSentenceContent(), sentence.getTime(), sentence.getNowStatus()));
                redisAllSentencesPerParagraphId.put(new ParagraphId(sentence.getParagraphId()), redisSentencesPerSentenceIdByParagraphId);
            } else { // 문단이 생성되어 있을 때
                redisSentencesPerSentenceIdByParagraphId.put(new SentenceId(sentenceId), new RedisSentenceDTO(sentence.getParagraphId(), sentence.getParagraphOrder(), sentence.getSentenceOrder(), sentence.getSentenceContent(), sentence.getTime(), sentence.getNowStatus()));
                redisAllSentencesPerParagraphId.put(new ParagraphId(sentence.getParagraphId()), redisSentencesPerSentenceIdByParagraphId);
            }
        }
        log.info("redisAllSentencesPerParagraphId: {}", redisAllSentencesPerParagraphId);
        redisTemplateImpl.delete("user"+userId); // redis에 저장된 문장들을 삭제, 밑에 새로 저장하는 부분에서 값을 다시 채워준다.
        // end: redis에서 user가 수정 할 수 있는 문장들을 가져온뒤 문단별로 합치기

        List<ModifiedParagraphDTO> modifiedParagraphListForResponseDTO = new ArrayList<>();

        // start: 수정된 문장들과 redis에 저장된 문장들을 비교하고 수정 및 생성
        for (ParagraphDTO modifiedParagraph : modifiedParagraphs) { // TODO: paragraph domain 만들어 보기

            List<SentenceDTO> modifiedSentencesForResponseDTO = new ArrayList<>();
            
            Long              modifiedParagraphId    = modifiedParagraph.getParagraphId();
            Long              modifiedParagraphOrder = modifiedParagraph.getParagraphOrder();
            List<SentenceDTO> modifiedSentences      = modifiedParagraph.getSentences();
            Map<SentenceId, RedisSentenceDTO> redisSentencesPerSentenceIdByParagraphId = redisAllSentencesPerParagraphId.get(new ParagraphId(modifiedParagraphId));
            Map<SentenceId, RedisSentenceDTO> temporaryRedisList = new HashMap<>(); // redis에 새롭게 저장할 문장들의 임시저장소
            log.info("modifiedParagraphId {}, redisSentencesPerSentenceIdByParagraphId {}, modifiedSentences {}", modifiedParagraphId, redisSentencesPerSentenceIdByParagraphId, modifiedSentences);

            // start: 새로운 문단이 생성된 경우
            if (redisSentencesPerSentenceIdByParagraphId == null) { // TODO: 새로운 문단 생성 domain관점에는 isNewLine(parameter) 이런식이 되어야 할 듯

                LocalTime expectedTimePerParagraph = LocalTime.of(0,0,0,0);

                for (SentenceDTO modifiedSentence : modifiedSentences) {
                    String    modifiedSentenceId           = UUID.randomUUID().toString();
                    Long      modifiedSentenceOrder        = modifiedSentence.getSentenceOrder();
                    String    modifiedSentenceContent      = modifiedSentence.getSentenceContent();
                    LocalTime modifiedSentenceExpectedTime = calculateExpectedTime(modifiedSentenceContent);

                    temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphOrder,
                                                                                                               modifiedParagraphOrder,
                                                                                                               modifiedSentenceOrder,
                                                                                                               modifiedSentenceContent,
                                                                                                               modifiedSentenceExpectedTime,
                                                                                                               NowStatus.EXPECTEDTIME));

                    SentenceDTO sentenceDTO = new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent);
                    modifiedSentencesForResponseDTO.add(sentenceDTO);

                    expectedTimePerParagraph = sumLocalTime(expectedTimePerParagraph, modifiedSentenceExpectedTime);
                }

                modifiedParagraphListForResponseDTO.add(new ModifiedParagraphDTO(modifiedParagraphOrder,
                                                                                            modifiedParagraphOrder,
                                                                                            expectedTimePerParagraph,
                                                                                            NowStatus.EXPECTEDTIME,
                                                                                            modifiedSentencesForResponseDTO));
                log.info("새로운 문단 생성 {}", modifiedParagraphId);
            // end: 새로운 문단이 생성된 경우

            // start: 기존의 문단이 수정 또는 변화
            } else {

                log.info("기존에 존재하던 문단");
                NowStatus nowStatus = NowStatus.REALTIME;
                LocalTime timePerParagraph = LocalTime.of(0, 0, 0, 0);

                for (SentenceDTO modifiedSentence : modifiedSentences) {

                    String modifiedSentenceId = modifiedSentence.getSentenceId();
                    String modifiedSentenceContent = modifiedSentence.getSentenceContent();
                    Long modifiedSentenceOrder = modifiedSentence.getSentenceOrder();


                    RedisSentenceDTO redisSentenceMatchedModifiedSentence = redisSentencesPerSentenceIdByParagraphId.get(new SentenceId(modifiedSentenceId));
                    log.info("modifiedSentence: {}, modifiedSentenceId: {}, redisSentenceMatchedModifiedSentence: {}", modifiedSentence, modifiedSentenceId, redisSentenceMatchedModifiedSentence);

                    // start: 새로운 문장이 추가된 경우
                    if (redisSentenceMatchedModifiedSentence == null) {

                        log.info("새로운 문장 생성");
                        LocalTime modifiedSentenceExpectedTime = calculateExpectedTime(modifiedSentenceContent);
                        modifiedSentenceId = UUID.randomUUID().toString();

                        temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphOrder,
                                                                                                                   modifiedParagraphOrder,
                                                                                                                   modifiedSentenceOrder,
                                                                                                                   modifiedSentenceContent,
                                                                                                                   modifiedSentenceExpectedTime,
                                                                                                                   NowStatus.EXPECTEDTIME));

                        timePerParagraph = sumLocalTime(timePerParagraph, modifiedSentenceExpectedTime);
                        modifiedSentencesForResponseDTO.add(new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent));
                        nowStatus = NowStatus.REALANDEXPECTEDTIME;
                    // end: 새로운 문장이 추가된 경우

                    // start: 기존에 존재 하던 문장이 수정되거나 유지
                    } else {

                        log.info("기존 문장 수정 {}", modifiedSentenceId);
                        String sentenceContentByRedisSentenceMatchedModifiedSentence = redisSentenceMatchedModifiedSentence.getSentenceContent();

                        // start: 기존 문장이 유지
                        if (sentenceContentByRedisSentenceMatchedModifiedSentence.equals(modifiedSentenceContent)) { // 이런 로직이 sentence domain에 들어있어야 하는 것 같다.
                            temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphOrder,
                                    modifiedParagraphOrder,
                                    modifiedSentenceOrder,
                                    sentenceContentByRedisSentenceMatchedModifiedSentence,
                                    redisSentenceMatchedModifiedSentence.getTime(),
                                    redisSentenceMatchedModifiedSentence.getNowStatus()));
                            timePerParagraph = sumLocalTime(timePerParagraph, redisSentenceMatchedModifiedSentence.getTime());
                        // end: 기존 문장이 유지

                        // start: 기존 문장이 수정된 경우
                        } else {

                            LocalTime modifiedSentenceExpectedTime = calculateExpectedTime(modifiedSentenceContent);

                            log.info("modifiedSentenceId: {}", modifiedSentenceId);
                            temporaryRedisList.put(new SentenceId(modifiedSentenceId), new RedisSentenceDTO(modifiedParagraphOrder, // 위의 class 생성 부분과 겹치는게 많은데 이런것도 공통화를 하고 setter로 다른 부분만 넣어줘야하나?
                                    modifiedParagraphOrder,
                                    modifiedSentenceOrder,
                                    modifiedSentenceContent,
                                    modifiedSentenceExpectedTime,
                                    NowStatus.EXPECTEDTIME));
                            timePerParagraph = sumLocalTime(timePerParagraph, modifiedSentenceExpectedTime);
                        }
                        modifiedSentencesForResponseDTO.add(new SentenceDTO(modifiedSentenceId, modifiedSentenceOrder, modifiedSentenceContent));

                        // end: 기존 문장이 수정된 경우
                    }
                }

                modifiedParagraphListForResponseDTO.add(new ModifiedParagraphDTO(modifiedParagraphOrder, modifiedParagraphOrder, timePerParagraph, nowStatus, modifiedSentencesForResponseDTO));
            }
            // end: 기존의 문단이 수정 또는 변화

            // start: 변화 사항을 redis에 반영 및 덮어쓰기

            List<String> newSentenceIds = new ArrayList<>();
            log.info("temporaryRedisList: {}", temporaryRedisList);

            // start: key: sentenceId, value: RedisSentenceDTO 로 저장
            for (Map.Entry<SentenceId, RedisSentenceDTO> redisSentenceMap : temporaryRedisList.entrySet()) {
                String newSentenceId = redisSentenceMap.getKey().getSentenceId();
                RedisSentenceDTO newSentence = redisSentenceMap.getValue();

                log.info("redisSentenceMap: {}", redisSentenceMap);
                scriptRedisRepository.saveSentenceInformation(newSentenceId, newSentence);
                newSentenceIds.add(newSentenceId);
            }
            // end: key: sentenceId, value: RedisSentenceDTO 로 저장

            // start: key: user+userId, value: sentenceId 로 저장
            log.info("newSentenceIds: {}", newSentenceIds);
            scriptRedisRepository.rightPushSentenceIdList("user"+userId, newSentenceIds);
            // end: key: user+userId, value: sentenceId 로 저장

            // end: 변화 사항을 redis에 반영 및 덮어쓰기
            
        }
        // end: 수정된 문장들과 redis에 저장된 문장들을 비교하고 수정 및 생성

        // start: 전체 대본 시간을 문단별로 가지고 있는 시간의 합산으로 구하기
        LocalTime totalTime = LocalTime.of(0, 0, 0, 0);

        for (ModifiedParagraphDTO modifiedParagraph : modifiedParagraphListForResponseDTO) {
            totalTime = sumLocalTime(totalTime, modifiedParagraph.getTime());
        }
        // end: 전체 대본 시간을 문단별로 가지고 있는 시간의 합산으로 구하기

        return new ModifyScriptResponseDTO(scriptId, totalTime, modifiedParagraphListForResponseDTO);
    }

    public ParagraphsResponseDTO getParagraphsByScriptId(Long scriptId) {
        ScriptEntity scriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("scriptId가 잘못 되었습니다."));

        List<SentenceEntity> sentences = scriptRepository.findAllSentencesByScriptId(scriptId).orElseThrow(() -> new IllegalArgumentException("잘못된 대본 입니다."));

        sentences.sort(Comparator.comparingLong(SentenceEntity::getParagraphId).thenComparingLong(SentenceEntity::getSentenceOrder));
        List<ParagraphContent> paragraphs = new ArrayList<>();

        Long paragraphId = 0L;
        String paragraphContent = "";
        for (SentenceEntity sentence : sentences) {
            Long paragraphIdBySentence = sentence.getParagraphId();
            if (paragraphIdBySentence != paragraphId) {
                paragraphs.add(new ParagraphContent(paragraphContent));
                paragraphId = paragraphIdBySentence;
                paragraphContent = sentence.getSentenceContent();
            } else {
                paragraphContent += sentence.getSentenceContent();
            }
        }
        paragraphs.add(new ParagraphContent(paragraphContent));
        return new ParagraphsResponseDTO(paragraphs);
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

    // 특정 주제에 버전 값을 모두 입력받으면 해당 버전의 음성 스크립트를 응답한다.
    public ScriptEntity getMinorScriptDetail(Long themeId, Long majorVersion, Long minorVersion) {

        return scriptRepository.findMinorScriptDetailByThemeIAndMajorVersionAndMinorVersion(themeId, majorVersion, minorVersion);
    }

    public HistoryListResponseDTO getScriptByThemeId(Long themeId) {
        List<ScriptDTO> scripts = new ArrayList<>();

        List<ScriptEntity> scriptEntityList = scriptRepository.findScriptByThemeId(themeId);

        for (ScriptEntity script : scriptEntityList) {
            scripts.add(new ScriptDTO(script.getScriptId(), script.getScriptContent(), script.getCreatedAt()));
        }

        return new HistoryListResponseDTO(scripts);
    }

    public ScriptExpectedTimeDTO getParagraphExpectedTime(String fullScript) {

        ParagraphDivideResponseDto result = clovaParagraphClient.divideScript(clovaApiKey, clovaGWApiKey, new RequestClovaDTO(fullScript));

        List<List<String>> paragraphs = result.getResult().getParagraphList();
        LocalTime totalExpectedTime = LocalTime.of(0, 0, 0, 0);
        List<ParagraphExpectedTimeDTO> paragraphExpectedTimeDTOList = new ArrayList<>();

        for(List<String> paragraph :  paragraphs) {
            LocalTime paragraphTime = calculateParagraphTime(paragraph);
            paragraphExpectedTimeDTOList.add(new ParagraphExpectedTimeDTO(paragraphTime, paragraph));
            totalExpectedTime = sumLocalTime(totalExpectedTime, paragraphTime);
        }

        return new ScriptExpectedTimeDTO(totalExpectedTime, paragraphExpectedTimeDTOList);

    }

    @Transactional
    public void reflectAnalyzeResult(Long scriptId, String result) {
        scriptRepository.saveAnalyzeResult(scriptId, result);
    }

    public AnalyzeResultDTO getAnalyzeResult(Long scriptId) {
        return scriptRepository.findAnalyzeResultByScriptId(scriptId)
                .map(result -> new AnalyzeResultDTO(200, result))
                .orElseGet(() -> new AnalyzeResultDTO(202, "스크립트를 분석중입니다."));
    }
}

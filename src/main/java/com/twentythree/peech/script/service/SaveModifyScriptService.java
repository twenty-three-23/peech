package com.twentythree.peech.script.service;

import com.twentythree.peech.script.cache.CacheService;
import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.RedisSentenceDTO;
import com.twentythree.peech.script.dto.response.SaveScriptAndSentencesResponseDTO;
import com.twentythree.peech.script.repository.ScriptRepository;
import com.twentythree.peech.script.repository.SentenceRepository;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.script.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveModifyScriptService {

    private final CacheService cacheService;
    private final ScriptRepository scriptRepository;
    private final ThemeRepository themeRepository;
    private final SentenceRepository sentenceRepository;
    private final VersionRepository versionRepository;

    @Transactional
    public SaveScriptAndSentencesResponseDTO saveModifyScript(Long themeId, Long scriptId, Long userId) {

        // 문장 리스트 가져오기
        List<String> sentenceIdList = cacheService.findAllByUserKey("user"+userId);

        // 문장 정보들 저장
        List<RedisSentenceDTO> redisSentenceInformationList = new ArrayList<>();

        for(String sentenceId : sentenceIdList) {
            // 문장 정보 가져오기
            RedisSentenceDTO redisSentence = cacheService.findByKey(sentenceId);
            redisSentenceInformationList.add(redisSentence);
        }

        ScriptEntity sttScriptEntity = scriptRepository.findById(scriptId).orElseThrow(() -> new IllegalArgumentException("해당 스크립트가 존재하지 않습니다."));

        Long majorVersion = sttScriptEntity.getVersion().getMajorVersion();
        Long minorVersion = sttScriptEntity.getVersion().getMinorVersion();

        ThemeEntity themeEntity = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        // 최신 minorVersion 생성
        VersionEntity versionEntity = VersionEntity
                .ofCreateSTTScriptVersionAfterInput(majorVersion, minorVersion, themeEntity);

        versionRepository.save(versionEntity);
        // 스크립트 저장
        // 스크립트 저장 전에 List<RedisSentenceDTO> 에서 SentenceContent를 합쳐서 전체 내용을 만들어야함
        String fullScript = addFullScript(redisSentenceInformationList);
        LocalTime totalScriptTime = addTotalScriptTime(redisSentenceInformationList);

        ScriptEntity scriptEntity = ScriptEntity
                .ofCreateInputScript(versionEntity, fullScript, totalScriptTime, InputAndSttType.INPUT);

        ScriptEntity saveScript = scriptRepository.save(scriptEntity);

        // 문장 저장
        redisSentenceInformationList.forEach(redisSentenceDTO -> {

            SentenceEntity sentenceEntity = SentenceEntity.ofCreateInputSentence(scriptEntity,
                    redisSentenceDTO.getParagraphId(), redisSentenceDTO.getSentenceContent(),
                    redisSentenceDTO.getSentenceOrder(), redisSentenceDTO.getTime());

            sentenceRepository.save(sentenceEntity);

        });

        return new SaveScriptAndSentencesResponseDTO(saveScript.getScriptId());
    }
    // 문장 합치기
    private String addFullScript(List<RedisSentenceDTO> redisSentenceDTOList) {

        String[] sentenceContentList = redisSentenceDTOList.stream()
                .sorted(Comparator.comparingLong(RedisSentenceDTO::getParagraphId))
                .sorted(Comparator.comparingLong(RedisSentenceDTO::getSentenceOrder))
                .map(RedisSentenceDTO::getSentenceContent).toArray(String[]::new);

        return String.join(" ", sentenceContentList);
    }
    // total 시간 계산
    private LocalTime addTotalScriptTime (List<RedisSentenceDTO> redisSentenceDTOList) {

        return redisSentenceDTOList.stream()
                .map(RedisSentenceDTO::getTime)
                .reduce(LocalTime.of(0, 0, 0, 0),
                        ((localTime, localTime2) ->
                                localTime.plusHours(localTime2.getHour()).plusMinutes(localTime2.getMinute())
                                        .plusSeconds(localTime2.getSecond()).plusNanos(localTime2.getNano())));
    }

}

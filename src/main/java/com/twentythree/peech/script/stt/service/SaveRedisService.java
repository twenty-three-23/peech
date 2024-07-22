package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.CacheService;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.dto.NowStatus;
import com.twentythree.peech.script.dto.RedisSentenceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaveRedisService {

    private final CacheService cacheService;

    public void saveSTTScriptInformation(Long userId, List<SentenceEntity> sentenceEntityList) {

        // redis key에 들어갈 부분은 user + userId 형태임
        String userKey = "user" + userId.toString();
        List<String> sentenceIdList = sentenceEntityList.stream().map(SentenceEntity::getSentenceId).toList();

        cacheService.saveSentencesIdList(userKey,sentenceIdList);

        for (String sentenceId : sentenceIdList) {
            RedisSentenceDTO redisSentenceDTO = new RedisSentenceDTO();

            // SentenceEntityList에서 sentenceId와 같은 객체를 찾아주는 로직
            Optional<SentenceEntity> sentenceEntityOptional = sentenceEntityList.stream()
                    .filter(sentenceInformation -> sentenceInformation.getSentenceId().equals(sentenceId))
                    .findFirst();



            if (sentenceEntityOptional.isPresent()) {
                SentenceEntity sentenceEntity = sentenceEntityOptional.get();
                redisSentenceDTO.setParagraphId(sentenceEntity.getParagraphId());
                redisSentenceDTO.setParagraphOrder(sentenceEntity.getParagraphId());
                redisSentenceDTO.setSentenceContent(sentenceEntity.getSentenceContent());
                redisSentenceDTO.setSentenceOrder(sentenceEntity.getSentenceOrder());
                redisSentenceDTO.setTime(sentenceEntity.getSentenceRealTime());
                redisSentenceDTO.setNowStatus(NowStatus.REALTIME);
                cacheService.saveSentenceInformation(sentenceId, redisSentenceDTO);
            }
        }
    }
}

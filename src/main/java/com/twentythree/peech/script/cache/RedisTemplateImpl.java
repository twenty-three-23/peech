package com.twentythree.peech.script.cache;

import com.twentythree.peech.script.dto.RedisSentenceDTO;
import com.twentythree.peech.script.dto.SaveRedisSentenceInfoDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RedisTemplateImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveSentencesIdList(String userId, List<Long> sentencesIdList){

        try{
            // Long 타입의 SentenceId List를 String 타입으로 변환
            List<String> sentenceIdListLongToString = sentencesIdList.stream().map(String::valueOf).toList();

            for(String sentenceId : sentenceIdListLongToString) {
                redisTemplate.opsForList().rightPush(userId, sentenceId);
            }
            log.info("Successfully saved sentenceID List for user: {}", userId);
        } catch (Exception e) {
            log.error("Error saving sentenceID List for user: {}", userId, e);
            throw new RuntimeException("Error saving sentenceID List for user: " + userId);
        }
    }

    @Override
    public void saveSentenceInfo(Long sentenceId, RedisSentenceDTO redisSentence){

        try {
                Map<String, String> sentenceInfomations = new HashMap<>();
                sentenceInfomations.put("paragraphId", redisSentence.getParagraphId().toString());
                sentenceInfomations.put("paragraphOrder", redisSentence.getParagraphOrder().toString());
                sentenceInfomations.put("sentenceContent", redisSentence.getSentenceContent());
                sentenceInfomations.put("sentenceOrder", redisSentence.getSentenceOrder().toString());
                sentenceInfomations.put("sentenceTime", redisSentence.getTime().toString());
                sentenceInfomations.put("isChanged", redisSentence.toStringIsChanged());

                // 해당 문장의 정보를 담아주는 Hash 저장
                redisTemplate.opsForHash().putAll(sentenceId.toString(), sentenceInfomations);
                log.info("Successfully saved redisSentence: {}", redisSentence);
        }catch (Exception e) {
            log.error("Error saving redisSentence List: {}",sentenceId,e);
            throw new RuntimeException("Error saving redisSentence List: " + sentenceId);
        }
    }
}


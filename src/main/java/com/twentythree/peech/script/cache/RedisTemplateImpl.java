package com.twentythree.peech.script.cache;

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
public class RedisTemplateImpl implements CacheTemplate {

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
    public void saveSentenceInfo(List<SaveRedisSentenceInfoDto> sentencesInfoList){

        try {
            for (SaveRedisSentenceInfoDto sentenceInfo : sentencesInfoList) {
                Map<String, String> sentenceDetails = new HashMap<>();
                sentenceDetails.put("paragraphId", sentenceInfo.paragraphId().toString());
                sentenceDetails.put("paragraphOrder", sentenceInfo.paragraphOrder().toString());
                sentenceDetails.put("sentenceContent", sentenceInfo.sentenceContent());
                sentenceDetails.put("sentenceOrder", sentenceInfo.sentenceOrder().toString());
                sentenceDetails.put("sentenceTime", sentenceInfo.sentenceTime().toString());
                sentenceDetails.put("isChanged", sentenceInfo.isChanged().toString());

                // 해당 문장의 정보를 담아주는 Hash 저장
                redisTemplate.opsForHash().putAll(sentenceInfo.sentenceId().toString(), sentenceDetails);
                log.info("Successfully saved sentenceInfo: {}", sentenceInfo);
            }
        }catch (Exception e) {
            log.error("Error saving sentenceInfo List: {}", sentencesInfoList,e);
            throw new RuntimeException("Error saving sentenceInfo List: " + sentencesInfoList);
        }
    }
}


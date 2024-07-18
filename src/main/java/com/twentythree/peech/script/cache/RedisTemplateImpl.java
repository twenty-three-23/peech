package com.twentythree.peech.script.cache;

import com.twentythree.peech.script.dto.RedisSentenceDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RedisTemplateImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveSentencesIdList(String userKey, List<Long> sentencesIdList){



        try{
            // Long 타입의 SentenceId List를 String 타입으로 변환
            List<String> sentenceIdListLongToString = sentencesIdList.stream().map(String::valueOf).toList();

            // 만약 해당 userKey 이미 존재한다면 삭제
            // 현재 방식이 overwrite되는 방식이 아니라서 해당 방식을 적용하고 추가로 수정할 예정 -> 없는 듯해서 대체방안까지 고려해야할듯 합니다 ㅠㅠ
            if(Boolean.TRUE.equals(redisTemplate.hasKey(userKey))){
                redisTemplate.delete(userKey);
            }

            for(String sentenceId : sentenceIdListLongToString) {
                redisTemplate.opsForList().rightPush(userKey, sentenceId);
            }
            log.info("Successfully saved sentenceID List for user: {}", userKey);
        } catch (Exception e) {
            log.error("Error saving sentenceID List for user: {}", userKey, e);
            throw new RuntimeException("Error saving sentenceID List for user: " + userKey);
        }
    }

    @Override
    public void saveSentenceInformation(Long sentenceId, RedisSentenceDTO redisSentence){

        try {

            Map<String, String> sentenceInformations = new HashMap<>();
            sentenceInformations.put("paragraph_id", redisSentence.getParagraphId().toString());
            sentenceInformations.put("paragraph_order", redisSentence.getParagraphOrder().toString());
            sentenceInformations.put("sentence_content", redisSentence.getSentenceContent());
            sentenceInformations.put("sentence_order", redisSentence.getSentenceOrder().toString());
            sentenceInformations.put("time", redisSentence.getTime().toString());
            sentenceInformations.put("now_status", redisSentence.getNowStatus().toString());

            // 해당 문장의 정보를 담아주는 Hash 저장
            redisTemplate.opsForHash().putAll(sentenceId.toString(), sentenceInformations);
            log.info("Successfully saved redisSentence: {}", redisSentence);

        }catch (Exception e) {
            log.error("Error saving redisSentence List: {}",sentenceId,e);
            throw new RuntimeException("Error saving redisSentence List: " + sentenceId);
        }
    }

    @Override
    public List<Long> findAllByUserKey(String userKey) {

        List<Long> list = Optional.ofNullable(redisTemplate.opsForList().range(userKey, 0, -1))
                .orElseThrow( () -> new RuntimeException("해당 유저에 대한 대본이 존재하지 않습니다.: " + userKey))
                .stream()
                .map(String::valueOf)
                .map(Long::parseLong)
                .toList();

        List<Long> sentenceIdsList = Optional.ofNullable(list).orElseThrow( () -> new RuntimeException("해당 유저에 대한 대본이 존재하지 않습니다.: " + userKey));

        return sentenceIdsList;
    }

    @Override
    public RedisSentenceDTO findByKey(Long sentenceId) {
        Map<Object, Object> sentenceInformation = redisTemplate.opsForHash().entries(sentenceId.toString());

        return new RedisSentenceDTO(sentenceInformation.get("paragraph_id").toString(),
                sentenceInformation.get("paragraph_order").toString(),
                sentenceInformation.get("sentence_order").toString(),
                sentenceInformation.get("sentence_content").toString(),
                sentenceInformation.get("time").toString(),
                sentenceInformation.get("now_status").toString());
    }
}


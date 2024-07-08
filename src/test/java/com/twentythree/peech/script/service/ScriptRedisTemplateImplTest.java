package com.twentythree.peech.script.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.dto.SaveRedisSentenceInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScriptRedisTemplateImplTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplateImpl redisTemplateImpl;


    @Test
    public void userId에_해당하는_문장_리스트_저장() throws Exception {
        String userId = "user1";
        List<Long> sentencesIdList = List.of(1L, 2L, 3L, 4L, 5L);

        redisTemplateImpl.saveSentencesIdList(userId, sentencesIdList);

        List<Object> savedSentences = redisTemplate.opsForList().range(userId, 0, -1);

        // 저장된 문장 리스트가 예상한 결과와 일치하는지 확인
        assertThat(savedSentences).hasSize(5).containsExactly("1", "2", "3", "4", "5");

    }

    @Test
    public void 문장_정보_저장() throws Exception {
        List<SaveRedisSentenceInfoDto> sentencesInfoList = List.of(
                new SaveRedisSentenceInfoDto(1L, 1L, 1L, "sentence1", 1L, LocalTime.of(0, 0, 12), false),
                new SaveRedisSentenceInfoDto(2L, 1L, 1L, "sentence2", 2L, LocalTime.of(0, 0, 12), false),
                new SaveRedisSentenceInfoDto(3L, 1L, 1L, "sentence3", 3L, LocalTime.of(0, 0, 12), false),
                new SaveRedisSentenceInfoDto(4L, 1L, 1L, "sentence4", 4L, LocalTime.of(0, 0, 12), false),
                new SaveRedisSentenceInfoDto(5L, 1L, 1L, "sentence5", 5L, LocalTime.of(0, 0, 12), false)
        );

        redisTemplateImpl.saveSentenceInfo(sentencesInfoList);

        for (SaveRedisSentenceInfoDto sentenceInfo : sentencesInfoList) {
            Map<Object, Object> savedSentenceInfo = redisTemplate.opsForHash().entries(sentenceInfo.sentenceId().toString());

            // 저장된 문장 정보가 예상한 결과와 일치하는지 확인
            assertEquals(sentencesInfoList.size(), redisTemplate.keys("*").size());
        }
    }
}

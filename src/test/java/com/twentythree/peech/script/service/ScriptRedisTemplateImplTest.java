package com.twentythree.peech.script.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.dto.RedisSentenceDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScriptRedisTemplateImplTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplateImpl redisTemplateImpl;

    // 전체 테스트로 돌릴 시 기존의 데이터가 남아있어서 테스트가 꼬일 수 있으므로 데이터 초기화
    @BeforeAll
    public void setUp() {
        // 모든 키를 가져와서 삭제
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    public void userKey에_해당하는_문장_리스트_저장() throws Exception {
        String userId = "user1";
        List<Long> sentencesIdList = List.of(1L, 2L, 3L, 4L, 5L);

        redisTemplateImpl.saveSentencesIdList(userId, sentencesIdList);

        List<Object> savedSentences = redisTemplate.opsForList().range(userId, 0, -1);

        // 저장된 문장 리스트가 예상한 결과와 일치하는지 확인
        assertThat(savedSentences).hasSize(5).containsExactly("1", "2", "3", "4", "5");

    }

    @Test
    public void 문장_정보_저장() throws Exception {
        List<RedisSentenceDTO> sentenceInfomations = List.of(
                new RedisSentenceDTO( 1L, 1L, 1L,"sentence1", LocalTime.of(0, 0, 12), false),
                new RedisSentenceDTO( 1L, 1L, 2L,"sentence2", LocalTime.of(0, 0, 12), false),
                new RedisSentenceDTO( 1L, 1L, 3L,"sentence3", LocalTime.of(0, 0, 12), false),
                new RedisSentenceDTO( 1L, 1L, 3L,"sentence4", LocalTime.of(0, 0, 12), false),
                new RedisSentenceDTO( 1L, 1L, 3L,"sentence5", LocalTime.of(0, 0, 12), false)
        );

        redisTemplateImpl.saveSentenceInformation(1L, sentenceInfomations.get(0));
        redisTemplateImpl.saveSentenceInformation(2L, sentenceInfomations.get(1));
        redisTemplateImpl.saveSentenceInformation(3L, sentenceInfomations.get(2));
        redisTemplateImpl.saveSentenceInformation(4L, sentenceInfomations.get(3));
        redisTemplateImpl.saveSentenceInformation(5L, sentenceInfomations.get(4));

        long sentenceId = 1L;

        for (RedisSentenceDTO sentenceInfo : sentenceInfomations) {
            Map<Object, Object> savedSentenceInfo = redisTemplate.opsForHash().entries(sentenceInfo.toString());
            sentenceId++;

            // 저장된 문장 정보가 예상한 결과와 일치하는지 확인
            assertEquals(sentenceInfomations.size(), redisTemplate.keys("*").size());
        }
    }

    @Test
    public void userKey에_해당하는_문장_리스트_조회() throws Exception {

        String userKey = "user1";

        List<Long> sentenceIdsList = redisTemplateImpl.findAllByUserKey(userKey);

        assertThat(sentenceIdsList).hasSize(5).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    public void 문장_정보_조회() throws Exception {

        Long sentenceId = 1L;

        RedisSentenceDTO redisSentenceDTO = redisTemplateImpl.findByKey(sentenceId);

        assertThat(redisSentenceDTO).isEqualTo(new RedisSentenceDTO(1L, 1L, 1L, "sentence1", LocalTime.of(0, 0, 12), false));
    }
}
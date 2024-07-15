package com.twentythree.peech.script.cache;

import com.twentythree.peech.script.dto.RedisSentenceDTO;

import java.util.List;

public interface CacheService {
    // User의 문장 리스트를 저장
    void saveSentencesIdList(String userKey, List<Long> sentencesIds);
    // 문장 정보를 저장
    void saveSentenceInformation(Long sentenceId, RedisSentenceDTO redisSentence);

    List<Long> findAllByUserKey(String userKey);

    RedisSentenceDTO findByKey(Long sentenceId);
}

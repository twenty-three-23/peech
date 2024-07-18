package com.twentythree.peech.script.cache;

import com.twentythree.peech.script.dto.RedisSentenceDTO;

import java.util.List;

public interface CacheService {
    // User의 문장 리스트를 저장
    void saveSentencesIdList(String userKey, List<String> sentencesIds);
    // 문장 정보를 저장
    void saveSentenceInformation(String sentenceId, RedisSentenceDTO redisSentence);

    List<String> findAllByUserKey(String userKey);

    RedisSentenceDTO findByKey(String sentenceId);
}

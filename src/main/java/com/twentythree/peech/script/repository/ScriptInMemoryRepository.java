package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.dto.RedisSentenceDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScriptInMemoryRepository {
    List<Long> findAllByUserKey(String userKey);
    RedisSentenceDTO findByKey(Long sentenceId);
    void setKeyUserValueSentenceIds(String userId, List<Long> sentenceIds);
    void setKeySentenceIdValueSentenceInformations(Long sentenceId, RedisSentenceDTO redisSentence);
}

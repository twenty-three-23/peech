package com.twentythree.peech.script.cache;

import com.twentythree.peech.script.dto.SaveRedisSentenceInfoDto;

import java.util.List;

public interface CacheTemplate {
    // User의 문장 리스트를 저장
    void saveSentencesIdList(String userId, List<Long> sentencesIdList);
    // 문장 정보를 저장
    void saveSentenceInfo(List<SaveRedisSentenceInfoDto> sentencesInfoList);
}

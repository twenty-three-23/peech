package com.twentythree.peech.script.dto;

import java.util.List;

// Redis에 저장할 문장 정보 DTO
public record SaveRedisUserSentenceListDto(
        String userId,
        List<Long> sentencesIdList
) {}

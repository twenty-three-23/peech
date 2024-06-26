package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.SentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SentenceRepository extends JpaRepository<SentenceEntity, Long> {

    @Query("select s from SentenceEntity s where s.scriptEntity.scriptId = :scriptId")
    public List<SentenceEntity> findBySentencesToScriptId(Long scriptId);
}

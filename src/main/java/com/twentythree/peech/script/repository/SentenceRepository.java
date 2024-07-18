package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.SentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SentenceRepository extends JpaRepository<SentenceEntity, String> {

    @Query("select s from SentenceEntity s where s.scriptEntity.scriptId = :scriptId")
    List<SentenceEntity> findBySentencesToScriptId(@Param("scriptId") Long scriptId);
}

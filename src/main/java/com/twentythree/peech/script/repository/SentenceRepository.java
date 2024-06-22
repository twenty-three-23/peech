package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.SentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SentenceRepository extends JpaRepository<SentenceEntity, Long> {

}

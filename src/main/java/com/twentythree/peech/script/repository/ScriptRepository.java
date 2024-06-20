package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ScriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {
}

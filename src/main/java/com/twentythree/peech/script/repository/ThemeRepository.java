package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {
}

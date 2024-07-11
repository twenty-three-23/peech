package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {

    @Query("select t.userEntity.id from ThemeEntity t where t.themeId =: themeId ")
    Long findUserByThemeId(Long themeId);
}

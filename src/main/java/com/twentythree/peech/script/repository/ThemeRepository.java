package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ThemeEntity;
import com.twentythree.peech.script.domain.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {

    @Query("select t from ThemeEntity t where t.userEntity.id = :userId")
    List<ThemeEntity> findAllThemesByUserId(Long userId);

    @Query("select v from VersionEntity v where v.themeEntity.themeId = :themeId")
    Optional<List<VersionEntity>> findAllVersionsByThemeId(Long themeId);

    @Query("select t.themeId from ThemeEntity t where t.userEntity.id = :userId and t.themeTitle = 'default'")
    Long findThemeIdByUserId(Long userId);

    @Query("select t from ThemeEntity t where t.userEntity.id = :userId and t.themeTitle = 'default'")
    Optional<ThemeEntity> findDefaultThemeByUserId(Long userId);
}

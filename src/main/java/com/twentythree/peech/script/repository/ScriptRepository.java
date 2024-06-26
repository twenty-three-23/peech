package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ScriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {

    @Query("select v.majorVersion from VersionEntity v where v.ThemeEntity.themeId = :themeId order by v.majorVersion desc limit 1")
    Long findByMaxMajorVersionInthemeId(Long themeId);

}

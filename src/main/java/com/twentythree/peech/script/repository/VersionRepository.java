package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VersionRepository extends JpaRepository<VersionEntity, Long> {

    @Query("select count(v) from VersionEntity v where v.majorVersion=:majorVersion and v.minorVersion != 0")
    int findMinorScriptsCount(Long majorVersion);


    @Query(value = "select v.minorVersion from VersionEntity v where v.ThemeEntity.themeId = :themeId and v.majorVersion = :majorVersion order by v.minorVersion desc limit 1")
    public Long findByMaxMinorVersion(Long themeId, Long majorVersion);
}

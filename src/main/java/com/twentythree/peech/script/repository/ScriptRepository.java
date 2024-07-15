package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ScriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {

    @Query("select v.majorVersion from VersionEntity v where v.ThemeEntity.themeId = :themeId order by v.majorVersion desc limit 1")
    Long findByMaxMajorVersionInthemeId(Long themeId);

    @Query("select s from ScriptEntity s " +
            "join fetch VersionEntity v " +
            "on s.version.majorVersion = v.majorVersion and s.version.minorVersion = v.minorVersion and s.version.ThemeEntity.themeId = v.ThemeEntity.themeId " +
            "where v.ThemeEntity.themeId = :themeId and v.minorVersion = 0")
    List<ScriptEntity> findMajorScriptByThemeId(Long themeId);

    @Query("select s from ScriptEntity s " +
            "join fetch VersionEntity v " +
            "on s.version.majorVersion = v.majorVersion and s.version.minorVersion = v.minorVersion " +
            "where v.minorVersion != 0 " +
            "and v.ThemeEntity.themeId = :themeId " +
            "and v.majorVersion = :majorVersion")
    List<ScriptEntity> findMinorScriptByThemeIdAndMajorVersion(Long themeId, Long majorVersion);

    @Query("select s from ScriptEntity s where s.version.themeId = :themeId and s.version.majorVersion = :majorVersion and s.version.minorVersion = :minorVersion")
    ScriptEntity findMinorScriptDetailByThemeIAndMajorVersionAndMinorVersion(@Param("themeId") Long themeId,@Param("majorVersion") Long majorVersion,@Param("minorVersion") Long minorVersion);

    @Query("select s.version.minorVersion " +
            "from ScriptEntity s where s.scriptId = :scriptId " +
            "order by s.version.minorVersion desc limit 1")
    Long findMaxMinorVersionByInputScriptId(Long scriptId);
}

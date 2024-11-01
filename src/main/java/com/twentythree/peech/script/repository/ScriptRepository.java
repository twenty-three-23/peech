package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.SentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {

    @Query("select v.majorVersion from VersionEntity v where v.themeEntity.themeId = :themeId order by v.majorVersion desc limit 1")
    Long findByMaxMajorVersionInthemeId(Long themeId);

    @Query("select s from ScriptEntity s " +
            "join fetch VersionEntity v " +
            "on s.version.majorVersion = v.majorVersion and s.version.minorVersion = v.minorVersion and s.version.themeEntity.themeId = v.themeEntity.themeId " +
            "where v.themeEntity.themeId = :themeId and v.minorVersion = 0")
    List<ScriptEntity> findMajorScriptByThemeId(Long themeId);

    @Query("select s from ScriptEntity s " +
            "join fetch VersionEntity v " +
            "on s.version.majorVersion = v.majorVersion " +
            "and s.version.minorVersion = v.minorVersion " +
            "and s.version.themeEntity.themeId = v.themeEntity.themeId " +
            "and v.majorVersion = :majorVersion " +
            "where v.minorVersion != 0 " +
            "and v.themeEntity.themeId = :themeId ")
    List<ScriptEntity> findMinorScriptByThemeIdAndMajorVersion(Long themeId, Long majorVersion);

    @Query("select s from ScriptEntity s where s.version.themeEntity.themeId = :themeId and s.version.majorVersion = :majorVersion and s.version.minorVersion = :minorVersion")
    ScriptEntity findMinorScriptDetailByThemeIAndMajorVersionAndMinorVersion(@Param("themeId") Long themeId,@Param("majorVersion") Long majorVersion,@Param("minorVersion") Long minorVersion);

    @Query("select s.version.minorVersion " +
            "from ScriptEntity s where s.scriptId = :scriptId " +
            "order by s.version.minorVersion desc limit 1")
    Long findMaxMinorVersionByInputScriptId(Long scriptId);

    @Query("select se from SentenceEntity se where se.scriptEntity.scriptId = :scriptId")
    Optional<List<SentenceEntity>> findAllSentencesByScriptId(Long scriptId);

    @Query("select script from ScriptEntity script where script.scriptId = :scriptId")
    Optional<ScriptEntity> findScriptByScriptId(Long scriptId);

    @Query("select s from ScriptEntity s where s.version.themeEntity.themeId = :themeId and s.DType = 'STT'")
    List<ScriptEntity> findScriptByThemeId(Long themeId);

    @Modifying
    @Query("update ScriptEntity s set s.analysisResult = :result where s.scriptId = :scriptId")
    void saveAnalyzeResult(Long scriptId, String result);
}

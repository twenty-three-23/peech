package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ScriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {

    @Query("select max(v.majorVersion) from VersionEntity v group by v.packageId having v.packageId = :packageId")
    Long findByMaxMajorVersionInPackageId(Long packageId);
}

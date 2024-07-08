package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository extends JpaRepository<VersionEntity, Long> {

    @Query("select count(v) from VersionEntity v where v.majorVersion=:majorVersion and v.minorVersion != 0")
    int findMinorScriptsCount(Long majorVersion);

}

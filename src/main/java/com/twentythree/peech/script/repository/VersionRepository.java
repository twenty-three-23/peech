package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<VersionEntity, Long> {
}

package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
}

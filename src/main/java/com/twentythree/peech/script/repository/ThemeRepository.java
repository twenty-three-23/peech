package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.ThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {

    @Query("select t from ThemeEntity t where t.userEntity.id = :userId")
    List<ThemeEntity> findAllThemesByUserId(Long userId);
}

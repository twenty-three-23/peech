package com.twentythree.peech.script.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ScriptRepositoryTest {

    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    void 대본입력및저장() {
        Long id = scriptRepository.findByMaxMajorVersionInPackageId(1L);
        assertThat(id).isEqualTo(2);
    }

}
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
    public void 패키지_아이디에서_가장_최신의_major_version_찾기() throws Exception {
        // given

        // when
        Long id = scriptRepository.findByMaxMajorVersionInthemeId(1L);

        // then
        assertThat(id).isEqualTo(3L);

    }


}
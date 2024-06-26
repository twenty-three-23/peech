package com.twentythree.peech.script.repository;

import com.twentythree.peech.script.domain.SentenceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SentenceRepositoryTest {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Test
    public void script_id_하위에_있는_모든_문장_조회() throws Exception {
        // given

        // when
        List<SentenceEntity> sentences = sentenceRepository.findBySentencesToScriptId(1L);

        // then
        assertThat(sentences.size()).isEqualTo(10);
    }

}
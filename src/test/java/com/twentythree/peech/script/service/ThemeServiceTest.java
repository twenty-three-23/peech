package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.ThemeEntity;
import com.twentythree.peech.script.dto.response.ThemesResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ThemeServiceTest {

    @Autowired private ThemeService themeService;

    @DisplayName("유저아이디로_주제_전체_가져오기")
    @Test
    public void 유저아이디로_주제_전체_가져오기() throws Exception {
        //Given
        Long userId = 1L;

        //When
        ThemesResponseDTO themesByUserId = themeService.getThemesByUserId(userId);

        //Then
        assertThat(themesByUserId.getThemes().size()).isEqualTo(1);

    }
}
package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.ThemeEntity;
import com.twentythree.peech.script.domain.VersionEntity;
import com.twentythree.peech.script.dto.ThemeDTO;
import com.twentythree.peech.script.dto.response.ThemesResponseDTO;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.user.domain.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public Long saveTheme(Long userId, String themeTitle) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("userId가 존재하지 않습니다."));

        ThemeEntity themeEntity = ThemeEntity.of(user, themeTitle);
        ThemeEntity savedThemeEntity = themeRepository.save(themeEntity);

        return savedThemeEntity.getThemeId();
    }

    public ThemesResponseDTO getThemesByUserId(Long userId) {

        List<ThemeEntity> themes = themeRepository.findAllThemesByUserId(userId);

        List<ThemeDTO> themesDTO = new ArrayList<>();
        for (ThemeEntity theme : themes) {

            List<VersionEntity> versions = themeRepository.findAllVersionsByThemeId(theme.getThemeId()).orElseThrow(() -> new IllegalArgumentException("존재하는 테마가 없습니다."));

            int majorVersionCnt = 0;
            for (VersionEntity version : versions) {
                if (version.getMinorVersion() == 0L) {
                    majorVersionCnt += 1;
                }
            }

            themesDTO.add(new ThemeDTO(theme.getThemeId(), theme.getThemeTitle(), theme.getCreatedAt(), majorVersionCnt));
        }

        return new ThemesResponseDTO(themesDTO);
    }
}

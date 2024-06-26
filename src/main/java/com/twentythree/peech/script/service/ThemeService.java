package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.ThemeEntity;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.user.domain.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

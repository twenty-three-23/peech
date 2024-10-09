package com.twentythree.peech.script.validator;

import com.twentythree.peech.script.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class ThemeValidatorImpl implements ThemeValidator {

    private final ThemeRepository themeRepository;

    @Override
    public boolean existThemeById(Long userId) {
        return themeRepository.findThemeIdByUserId(userId) != null;
    }
}

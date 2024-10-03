package com.twentythree.peech.interviewquestion.application;

import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.ThemeEntity;
import com.twentythree.peech.script.repository.ScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final ScriptRepository scriptRepository;


    public boolean isOwnerOfScriptByUserId(Long userId, Long scriptId) {
        ScriptEntity script = scriptRepository.findScriptByScriptId(scriptId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 자기소개서 입니다.")
        );

        ThemeEntity themeEntity = script.getVersion().getThemeEntity();
        Long userIdByScriptId = themeEntity.getUserEntity().getId();

        return userId.equals(userIdByScriptId);
    }
}

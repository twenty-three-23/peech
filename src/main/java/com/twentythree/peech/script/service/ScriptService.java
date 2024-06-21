package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.InputAndSttType;
import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.domain.VersionPk;
import com.twentythree.peech.script.dto.response.ProcessedScriptResponseDTO;
import com.twentythree.peech.script.repository.ScriptRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;

    @Autowired
    public ScriptService(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }



    public ScriptEntity saveInputScript(Long packageId, String text) {
        LocalTime expectedTime = calculateExpectedTime(text);


        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInPackageId(packageId);

        VersionPk versionPk;

        if (latestMajorVersion == null) {
            versionPk = new VersionPk(1L, 0L);
        } else {
            versionPk = new VersionPk(latestMajorVersion + 1, 0L);
        }

        ScriptEntity scriptEntity = ScriptEntity.ofCreateInputScript(versionPk, text, expectedTime, InputAndSttType.INPUT);

        scriptRepository.save(scriptEntity);
        return scriptEntity;
    }





    // 메소드

    private LocalTime calculateExpectedTime(String text) {

        final float DEFAULT_TIME_PER_WORD_SECOND = 1.75F; //LocalTime.of(0,0,1, 750000000);

        String[] words = text.split(" ");
        int wordsCount = words.length;

        float expectedTimeToSecond = wordsCount * DEFAULT_TIME_PER_WORD_SECOND;

        LocalTime expectedTime = transferSeoondToLocalTime(expectedTimeToSecond);

        return expectedTime;
    }

    private LocalTime transferSeoondToLocalTime(float time) {

        int second = (int)time;

        int minute = (int) (second/60);
        int hour = (int) (second / (60 * 60));

        int minuteSet = minute - (hour * 60);
        int secondSet = second - (minuteSet * 60);

        return LocalTime.of(hour, minuteSet, secondSet);
    }
}

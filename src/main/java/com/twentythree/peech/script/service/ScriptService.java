package com.twentythree.peech.script.service;

import com.twentythree.peech.script.domain.*;
import com.twentythree.peech.script.dto.SaveScripDTO;
import com.twentythree.peech.script.repository.PackageRepository;
import com.twentythree.peech.script.repository.ScriptRepository;
import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.script.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.time.LocalTime;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScriptService {
    
    private final ScriptRepository scriptRepository;
    private final PackageRepository packageRepository;
    private final VersionRepository versionRepository;

    @Transactional
    public SaveScripDTO saveInputScript(Long packageId, String[] paragraphs) {

        String script = String.join("\n", paragraphs);

        LocalTime expectedTime = ScriptUtils.calculateExpectedTime(script);

        PackageEntity packageEntity = packageRepository.findById(packageId).orElseThrow(() -> new IllegalArgumentException("패키지 아이디가 잘못되었습니다."));

        Long latestMajorVersion = scriptRepository.findByMaxMajorVersionInPackageId(packageId);

        VersionEntity versionEntity = VersionEntity.ofCreateInputScriptVersion(latestMajorVersion, packageEntity);
        ScriptEntity scriptEntity = ScriptEntity.ofCreateInputScript(versionEntity, script, expectedTime, InputAndSttType.INPUT);

        versionRepository.save(versionEntity);
        scriptRepository.save(scriptEntity);

        return new SaveScripDTO(scriptEntity, ScriptUtils.calculateExpectedTime(script));
    }

}

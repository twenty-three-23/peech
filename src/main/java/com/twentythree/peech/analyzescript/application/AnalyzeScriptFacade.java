package com.twentythree.peech.analyzescript.application;

import com.twentythree.peech.analyzescript.domain.AnalyzeScriptPredictor;
import com.twentythree.peech.fcm.application.NotificationService;
import com.twentythree.peech.script.service.ScriptService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnalyzeScriptFacade {

    private final AnalyzeScriptPredictor analyzeScriptPredictor;
    private final ScriptService scriptService;
    private final NotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    public void analyzeScriptAndSave(Long userId, Long scriptId, String scriptContent) {
        analyzeScriptPredictor.requestAnalyzeScript(scriptContent)
                .thenAccept(result -> {
                    scriptService.reflectAnalyzeResult(scriptId, result);
                    notificationService.pushNotification(userId);
                })
                .exceptionally(e -> {
                    log.error("스크립트 분석 중 예외 발생", e);
                    return null;
                });
    }
}

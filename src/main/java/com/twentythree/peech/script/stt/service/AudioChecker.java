package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.common.utils.ExtractDuration;
import com.twentythree.peech.usagetime.constant.UsageConstantValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AudioChecker {

    private final ExtractDuration extractDuration;

    public double checkMaxAudioDuration(String filePath) {
        try {
            double duration = extractDuration.getDuration(filePath);

            return duration < UsageConstantValue.MAX_AUDIO_TIME + UsageConstantValue.BUFFER_TIME ? duration : -1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkRemainingAudioDuration(String filePath, Long remainingTime) {
        try {
            double duration = extractDuration.getDuration(filePath);

            return duration > remainingTime;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkRemainingAudioDuration(double duration, Long remainingTime) {
        try {
            return duration > remainingTime;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.twentythree.peech.usagetime.service;

import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.usagetime.constant.ConstantValue;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.dto.RemainingTimeDTO;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondTimeResponseDTO;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UsageTimeService {

    private final UsageTimeRepository usageTimeRepository;

    @Transactional
    public Long subUsageTimeByAudio(Long userId, MultipartFile audioFile) {

        try {
            // 음성 파일의 시간을 가져오는 로직
            File tempFile = File.createTempFile("audio", null);
            audioFile.transferTo(tempFile);

            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(tempFile);
            long audioFileLength = tempFile.length();
            int frameSize = fileFormat.getFormat().getFrameSize();
            float frameRate = fileFormat.getFormat().getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));

            UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                    orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
            Long remainingTime = usageTime.getRemainingTime();
            remainingTime -= (long) durationInSeconds;

            usageTimeRepository.updateRemainingTime(userId, remainingTime);
            return remainingTime;

        } catch (Exception e) {
            throw new RuntimeException("파일 저장에 실패하였습니다.");
        }
    }

    @Transactional
    public Long subUsageTimeByTimePerSecond(Long userId, Long time) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        Long remainingTime = usageTime.getRemainingTime();
        remainingTime -= time;

        usageTimeRepository.updateRemainingTime(userId, remainingTime);
        return remainingTime;
    }

    public TextAndSecondTimeResponseDTO getUsageTime(Long userId) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        log.info("remaining Time: {}", usageTime.getRemainingTime());

        Long remainingTimeToSecond = usageTime.getRemainingTime();

        LocalTime remainingTimeToLocalTime = ScriptUtils.transferSeoondToLocalTime(remainingTimeToSecond);
        log.info("remaining Time LocalTime: {}", remainingTimeToLocalTime.getSecond());

        int hour = remainingTimeToLocalTime.getHour();
        int minute = remainingTimeToLocalTime.getMinute();
        int second = remainingTimeToLocalTime.getSecond();

        String remainingTimeToText = "";

        if (hour != 0) {
            remainingTimeToText = remainingTimeToText.concat(hour + "시간 ");
        }
        if (minute != 0) {
            remainingTimeToText = remainingTimeToText.concat(minute + "분 ");
        }
        if (second != 0) {
            remainingTimeToText = remainingTimeToText.concat(second + "초 ");
        }

        remainingTimeToText = remainingTimeToText.trim();

        return new TextAndSecondTimeResponseDTO(new RemainingTimeDTO(remainingTimeToText, remainingTimeToSecond));
    }

    public CheckRemainingTimeResponseDTO checkRemainingTime(Long userId, Long audioTime) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        Long remainingTime = usageTime.getRemainingTime();
        return (remainingTime >= audioTime) ? new CheckRemainingTimeResponseDTO("성공") : new CheckRemainingTimeResponseDTO("사용 시간이 부족합니다.");
    }

    public TextAndSecondTimeResponseDTO getMaxAudioTime() {
        Long maxAudioTimeToSecond = ConstantValue.MAX_AUDIO_TIME;
        LocalTime maxAudioTimeToLocalTime = ScriptUtils.transferSeoondToLocalTime(maxAudioTimeToSecond);

        int hour = maxAudioTimeToLocalTime.getHour();
        int minute = maxAudioTimeToLocalTime.getMinute();
        int second = maxAudioTimeToLocalTime.getSecond();

        String maxAudioTimeToText = "";

        if (hour != 0) {
            maxAudioTimeToText = maxAudioTimeToText.concat(hour + "시간 ");
        }
        if (minute != 0) {
            maxAudioTimeToText = maxAudioTimeToText.concat(minute + "분 ");
        }
        if (second != 0) {
            maxAudioTimeToText = maxAudioTimeToText.concat(second + "초 ");
        }

        maxAudioTimeToText = maxAudioTimeToText.trim();

        return new TextAndSecondTimeResponseDTO(new RemainingTimeDTO(maxAudioTimeToText, maxAudioTimeToSecond));
    }
}

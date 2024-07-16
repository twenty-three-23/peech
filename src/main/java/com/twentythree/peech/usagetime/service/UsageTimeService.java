package com.twentythree.peech.usagetime.service;

import com.twentythree.peech.auth.dto.UserIdDTO;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.dto.response.RemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;

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

    public RemainingTimeResponseDTO getUsageTime(Long userId) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        log.info("remaining Time: {}", usageTime.getRemainingTime());
        return new RemainingTimeResponseDTO(usageTime.getRemainingTime());
    }

    public CheckRemainingTimeResponseDTO checkRemainingTime(Long userId, Long audioTime) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        Long remainingTime = usageTime.getRemainingTime();
        return (remainingTime >= audioTime) ? new CheckRemainingTimeResponseDTO("성공") : new CheckRemainingTimeResponseDTO("사용 시간이 부족합니다.");
    }
}

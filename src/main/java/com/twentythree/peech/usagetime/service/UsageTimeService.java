package com.twentythree.peech.usagetime.service;

import com.twentythree.peech.common.utils.ScriptUtils;
import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import com.twentythree.peech.usagetime.dto.response.TextAndSecondResponseDTO;
import com.twentythree.peech.usagetime.dto.response.CheckRemainingTimeResponseDTO;
import com.twentythree.peech.usagetime.repository.UsageTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;

import static com.twentythree.peech.usagetime.constant.UsageConstantValue.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UsageTimeService {

    private final UsageTimeRepository usageTimeRepository;

    @Transactional
    public long getAudioLength(Long userId, File file) throws IOException {

        long calculatedTime = 0;

        Metadata metadata = new Metadata();
        ContentHandler handler = new BodyContentHandler();
        ParseContext parseContext = new ParseContext();
        AutoDetectParser autoDetectParser = new AutoDetectParser();

        FileInputStream inputStream = new FileInputStream(file);
        try {
            autoDetectParser.parse(inputStream, handler, metadata, parseContext);
        } catch (SAXException | TikaException e) {
            throw new RuntimeException(e);
        }

        String duration = metadata.get("xmpDM:duration");
        if (duration == null) {
            throw new IOException("오디오 파일 시간 추출에 실패하였습니다.");
        }

        double durationDouble = Double.parseDouble(duration);

        calculatedTime = (long) Math.floor(durationDouble);

        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        Long remainingTime = usageTime.getRemainingTime();
        remainingTime -= (long) calculatedTime;
        usageTimeRepository.updateRemainingTime(userId, remainingTime);

        return remainingTime;


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

    public TextAndSecondResponseDTO getUsageTime(Long userId) {
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

        return new TextAndSecondResponseDTO(remainingTimeToText, remainingTimeToSecond);
    }

    public CheckRemainingTimeResponseDTO checkRemainingTime(Long userId, Long audioTime) {

        if (audioTime > MAX_AUDIO_TIME + BUFFER_TIME) {
            throw new IllegalArgumentException("최대 사용 시간을 초과하였습니다.");
        }

        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        Long remainingTime = usageTime.getRemainingTime();
        return (remainingTime >= audioTime) ? new CheckRemainingTimeResponseDTO("성공") : new CheckRemainingTimeResponseDTO("사용 시간이 부족합니다.");
    }

    public TextAndSecondResponseDTO getMaxAudioTime() {
        Long maxAudioTimeToSecond = MAX_AUDIO_TIME;
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

        return new TextAndSecondResponseDTO(maxAudioTimeToText, maxAudioTimeToSecond);
    }

    // 사용자의 남은 시간을 가져오는 로직
    public Long getRemainingTime(Long userId) {
        UsageTimeEntity usageTime = usageTimeRepository.findByUserId(userId).
                orElseThrow(() -> new IllegalArgumentException("사용자 아이디가 잘 못 되었습니다."));
        return usageTime.getRemainingTime();
    }
}

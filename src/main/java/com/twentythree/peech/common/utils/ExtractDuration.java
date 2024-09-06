package com.twentythree.peech.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExtractDuration {

    private final FFprobe ffprobe;

    public double getDuration(String sourcePath) throws IOException {
        // 영상 경로
        Path videoPath = Paths.get(sourcePath);

        // 영상 메타데이터 조회
        FFmpegProbeResult probeResult = ffprobe.probe(videoPath.toString());

        // 영상 길이 추출
        FFmpegStream videoStream = probeResult.getStreams().get(0);
        double durationInSeconds = videoStream.duration;


        return durationInSeconds;
    }

}

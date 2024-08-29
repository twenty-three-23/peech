package com.twentythree.peech.config;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class FFMPEGConfig {

    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Bean
    public FFprobe ffProbe() throws IOException {
        return new FFprobe(ffprobePath);
    }

}

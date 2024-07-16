package com.twentythree.peech.usagetime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class AudioFileRequestDTO {
    private MultipartFile audioFile;
}

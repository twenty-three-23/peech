package com.twentythree.peech.script.stt.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record STTRequestDto(
        MultipartFile media
) { }

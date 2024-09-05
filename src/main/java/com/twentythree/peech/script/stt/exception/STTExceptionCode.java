package com.twentythree.peech.script.stt.exception;

import lombok.Getter;

@Getter
public enum STTExceptionCode {

    VOICE_FILE_PROCESSING_ERROR( "음성 파일 처리중 에러가 발생하였습니다."),
    NOT_EXIST_VOICE_CONTENT( "녹음 파일에 음성이 존재하지 않습니다."),
    VOICE_LENGTH_TOO_LONG( "음성 녹음 길이가 초과되었습니다."),
    TIME_CHECKING_ERROR("음성 파일 시간 측정중 에러가 발생하였습니다."),
    LACK_OF_REMAINING_TIME( "남은 시간이 부족합니다."),
    FAIL_TO_REQUEST_CLOVA_SPEECH("Clova Speech API 요청에 실패하였습니다.");

    private final String message;

    STTExceptionCode(String message) {
        this.message = message;
    }

}

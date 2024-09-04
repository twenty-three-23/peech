package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.service.ScriptService;
import com.twentythree.peech.script.service.SentenceService;
import com.twentythree.peech.script.stt.dto.AddSentenceInformationVO;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTScriptResponseDTO;
import com.twentythree.peech.usagetime.service.UsageTimeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

// STT 결과를 클라이언트에게 전달할 VO 생성
@Service
@RequiredArgsConstructor
public class ProcessSTTService {

    private final RequestClovaSpeechApiService requestClovaSpeechApiService;

    private final AddRealTimeToSentenceService addRealTimeToSentenceService;

    private final CreateParagraghService createParagraghService;

    private final ScriptService scriptService;

    private final SentenceService sentenceService;

    private final CreateSTTResultService createSTTResultService;

    private final SaveRedisService saveRedisService;
    
    private final UsageTimeService usageTimeService;

    private final AudioChecker audioChecker;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // 대본 입력 없이 STT 결과 생성
    public Mono<STTScriptResponseDTO> createSTTResult(STTRequestDto request, Long themeId, Long userId) {

        // 음성파일 길이 로그 출력

        File tempFile = saveTempFile(request);

        double time = audioChecker.checkMaxAudioDuration(tempFile.getAbsolutePath());
        if(time != -1) {

            Long remainingTime = usageTimeService.getRemainingTime(userId);

            if (audioChecker.checkRemainingAudioDuration(time, remainingTime)) {
                throw new IllegalStateException("STT 실행이 불가합니다. 남은 시간이 부족합니다.");
            }

            Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(tempFile);

            return clovaResponseDtoMono
                    .flatMap(clovaResponseDto -> {
                        String totalText = clovaResponseDto.getFullText();
                        // stt 길이에서 사용시간 차감
                        long totalRealSeconds = clovaResponseDto.getTotalRealTime().toSecondOfDay();
                        log.info("STT 요청 시간: {}초", totalRealSeconds);

                        usageTimeService.subUsageTimeByTimePerSecond(userId, totalRealSeconds);

                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);
                            // Script Entity 저장
                            LocalTime totalExpectedTime = sentenceService.getTotalExpectedTime(sentenceAndRealTimeList);
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, clovaResponseDto, totalExpectedTime);
                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                // 생성된 scriptId 가져오기
                                long scriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, scriptId);
                                // Redis 저장 로직
                                saveRedisService.saveSTTScriptInformation(userId, sentenceEntityList);

                                // 최종 클라이언트 반환 DTO
                                return Mono.just(sttScriptResponseDTO);
                            });
                        });
                    })
                    .onErrorResume(e -> {
                        // 적절한 오류 메시지 반환
                        return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
                    });
        }else {
            throw new IllegalArgumentException("음성 녹음 길이가 초과되었습니다.");
        }
    }

    // 대본 입력 후 STT 결과 생성
    public Mono<STTScriptResponseDTO> createSTTResult(STTRequestDto request, Long themeId, Long scriptId, Long userId)  {

        File tempFile = saveTempFile(request);

        double time = audioChecker.checkMaxAudioDuration(tempFile.getAbsolutePath());

        if (time != -1) {


            Long remainingTime = usageTimeService.getRemainingTime(userId);

            if (audioChecker.checkRemainingAudioDuration(time, remainingTime)) {
                throw new IllegalStateException("STT 실행이 불가합니다. 남은 시간이 부족합니다.");
            }

            Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(tempFile);

            return clovaResponseDtoMono
                    .flatMap(clovaResponseDto -> {
                        String totalText = clovaResponseDto.getFullText();
                        // stt 길이에서 사용시간 차감
                        long totalRealSeconds = clovaResponseDto.getTotalRealTime().toSecondOfDay();
                        log.info("STT 요청 시간: {}초", totalRealSeconds);
                        usageTimeService.subUsageTimeByTimePerSecond(userId, request.time());


                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);
                            // Script Entity 저장
                            LocalTime totalExpectedTime = sentenceService.getTotalExpectedTime(sentenceAndRealTimeList);
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, scriptId, clovaResponseDto, totalExpectedTime);

                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                // scriptId 저장
                                long newScriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, newScriptId);

                                // Redis 저장 로직
                                saveRedisService.saveSTTScriptInformation(userId, sentenceEntityList);

                                // 최종 클라이언트 반환 DTO
                                return Mono.just(sttScriptResponseDTO);
                            });
                        });
                    })
                    .onErrorResume(e -> {
                        // 예외 처리 로직 추가
                        e.printStackTrace(); // 예외 로그 출력
                        // 적절한 오류 메시지 반환
                        return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
                    });
        }else {
            throw new IllegalArgumentException("음성 녹음 길이가 초과되었습니다.");
        }
    }

    public Mono<STTScriptResponseDTO> createSTTResult(File file, Long themeId, Long scriptId, Long userId)  {

        double time = audioChecker.checkMaxAudioDuration(file.getAbsolutePath());
        Long longTime = (long) time;

        if (time != -1) {

            Long remainingTime = usageTimeService.getRemainingTime(userId);

            if (!audioChecker.checkRemainingAudioDuration(time, remainingTime)) {
                throw new IllegalStateException("STT 실행이 불가합니다. 남은 시간이 부족합니다.");
            }

        Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(file);

        return clovaResponseDtoMono
                .flatMap(clovaResponseDto -> {
                    String totalText = clovaResponseDto.getFullText();
                    // stt 길이에서 사용시간 차감
                    long totalRealSeconds = clovaResponseDto.getTotalRealTime().toSecondOfDay();
                    log.info("STT 요청 시간: {}초", totalRealSeconds);
                    usageTimeService.subUsageTimeByTimePerSecond(userId, totalRealSeconds);


                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);
                            // Script Entity 저장
                            LocalTime totalExpectedTime = sentenceService.getTotalExpectedTime(sentenceAndRealTimeList);
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, scriptId, clovaResponseDto, totalExpectedTime);

                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                // scriptId 저장
                                long newScriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, newScriptId);

                                // Redis 저장 로직
                                saveRedisService.saveSTTScriptInformation(userId, sentenceEntityList);

                                // 최종 클라이언트 반환 DTO
                                return Mono.just(sttScriptResponseDTO);
                            });
                        });
                    })
                    .onErrorResume(e -> {
                        // 예외 처리 로직 추가
                        e.printStackTrace(); // 예외 로그 출력
                        // 적절한 오류 메시지 반환
                        return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
                    });
        } else {
            throw new IllegalArgumentException("음성 녹음 길이가 초과되었습니다.");
        }
    }

    public Mono<STTScriptResponseDTO> createSTTResult(File file, Long themeId, Long userId) {

        // 음성파일 길이 로그 출력

        double time = audioChecker.checkMaxAudioDuration(file.getAbsolutePath());

        if (time != -1) {

            Long remainingTime = usageTimeService.getRemainingTime(userId);

            if (!audioChecker.checkRemainingAudioDuration(time, remainingTime)) {
                throw new IllegalStateException("STT 실행이 불가합니다. 남은 시간이 부족합니다.");
            }

            Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(file);

            return clovaResponseDtoMono
                    .flatMap(clovaResponseDto -> {
                        String totalText = clovaResponseDto.getFullText();
                        // stt 길이에서 사용시간 차감
                        long totalRealSeconds = clovaResponseDto.getTotalRealTime().toSecondOfDay();
                        log.info("STT 요청 시간: {}초", totalRealSeconds);

                        usageTimeService.subUsageTimeByTimePerSecond(userId, totalRealSeconds);

                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);
                            // Script Entity 저장
                            LocalTime totalExpectedTime = sentenceService.getTotalExpectedTime(sentenceAndRealTimeList);
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, clovaResponseDto, totalExpectedTime);
                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                // 생성된 scriptId 가져오기
                                long scriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, scriptId);
                                // Redis 저장 로직
                                saveRedisService.saveSTTScriptInformation(userId, sentenceEntityList);

                                // 최종 클라이언트 반환 DTO
                                return Mono.just(sttScriptResponseDTO);
                            });
                        });
                    })
                    .onErrorResume(e -> {
                        // 적절한 오류 메시지 반환
                        return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
                    });
        } else {
            throw new IllegalArgumentException("음성 녹음 길이가 초과되었습니다.");
        }
    }


    // request 파일을 임시 저장하는 로직
    private File saveTempFile(STTRequestDto request) {
        if (request.file() == null) {
            throw new IllegalArgumentException("파일이 유효하지 않습니다.");
        }
        File tempFile;
        try {
            String originalFilename = request.file().getOriginalFilename();
            if (originalFilename == null) {
                throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
            }

            // 3. 파일명에서 가장 마지막에 오는 '.'의 index 확인
            int index = originalFilename.lastIndexOf(".");

            // 4. 확장자 추출
            String suffix = null;

            if (index > 0) {
                suffix = originalFilename.substring(index + 1);
            }

            tempFile = File.createTempFile("temp", suffix);
            request.file().transferTo(tempFile);

        } catch (IOException e) {
            // IO 예외 처리
            throw new IllegalArgumentException("파일 변환 중 IO 오류가 발생했습니다.", e);
        } catch (IllegalStateException e) {
            // IllegalStateException 예외 처리
            throw new IllegalArgumentException("파일 변환 중 상태 오류가 발생했습니다.", e);
        } catch (Exception e) {
            // 일반 예외 처리
            throw new IllegalArgumentException("파일 변환 중 오류가 발생했습니다.", e);
        }
        return tempFile;
    }
}

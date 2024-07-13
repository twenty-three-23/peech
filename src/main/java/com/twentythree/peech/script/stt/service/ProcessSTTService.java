package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.script.service.ScriptService;
import com.twentythree.peech.script.service.SentenceService;
import com.twentythree.peech.script.stt.dto.EditClovaSpeechSentenceVO;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.SentenceVO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

// STT 결과를 클라이언트에게 전달할 VO 생성
@Service
@RequiredArgsConstructor
public class ProcessSTTService {

    private final RequestClovaSpeechApiService requestClovaSpeechApiService;

    private final EditClovaSpeechResponseService editClovaSpeechResponseService;

    private final CreateParagraghService createParagraghService;

    private final ScriptService scriptService;

    private final SentenceService sentenceService;

    private final CreateSTTResultService createSTTResultService;

    private final ThemeRepository themeRepository;

    private final RedisTemplateImpl redisTemplateImpl;

    public Mono<STTResultResponseDto> createSTTResult(STTRequestDto request, Long themeId, Long userId) {

        // redis key에 들어갈 부분은 user + userId 형태임
        String userKey = "user"+userId.toString();

        Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(request);

        return clovaResponseDtoMono.flatMap(clovaResponseDto -> {List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList = editClovaSpeechResponseService.editClovaSpeechResponseSentences(clovaResponseDto);

            String totalText = sentenceAndRealTimeList
                    .stream().map(EditClovaSpeechSentenceVO::sentenceContent)
                    .collect(Collectors.joining("\\n"));

            Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
            return paragraphDivideResponseDtoMono.flatMap(
                    paragraphDivideResponseDto -> {
                        // Script Entity 저장
                        Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, clovaResponseDto);

                        // Sentence Entity 저장
                        return saveSTTScriptVOMono.flatMap(
                                saveSTTScriptVO -> {
                                    List<SentenceVO> sentenceList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                    STTResultResponseDto sttResultResponseDto = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceAndRealTimeList, sentenceList, paragraphDivideResponseDto);
                                    // Redis 저장 로직
                                    List<Long> sentenceIds = sentenceList.stream().map(sentenceVO -> sentenceVO.sentenceEntity().getSentenceId()).toList();
                                    redisTemplateImpl.saveSentencesIdList(userKey, sentenceIds);
                                    // 최종 클라이언트 반환 DTO
                                    return Mono.just(sttResultResponseDto);
                                });
                            });
        });


    }

    public Mono<STTResultResponseDto> createSTTResult(STTRequestDto request, Long themeId, Long scriptId, Long userId) {

        // redis key에 들어갈 부분은 user + userId 형태임
        String userKey = "user"+userId.toString();

        Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(request);

        return clovaResponseDtoMono.flatMap(clovaResponseDto -> {List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList = editClovaSpeechResponseService.editClovaSpeechResponseSentences(clovaResponseDto);
            String totalText = sentenceAndRealTimeList
                    .stream().map(EditClovaSpeechSentenceVO::sentenceContent)
                    .collect(Collectors.joining("\\n"));

            Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
            return paragraphDivideResponseDtoMono.flatMap(
                    paragraphDivideResponseDto -> {
                        // Script Entity 저장
                        Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, scriptId, clovaResponseDto);

                        // Sentence Entity 저장
                        return saveSTTScriptVOMono.flatMap(
                                saveSTTScriptVO -> {
                                    List<SentenceVO> sentenceList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                    STTResultResponseDto sttResultResponseDto = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceAndRealTimeList, sentenceList, paragraphDivideResponseDto);
                                    // Redis 저장 로직
                                    List<Long> sentenceIds = sentenceList.stream().map(sentenceVO -> sentenceVO.sentenceEntity().getSentenceId()).collect(Collectors.toList());
                                    redisTemplateImpl.saveSentencesIdList(userKey, sentenceIds);

                                    // 최종 클라이언트 반환 DTO
                                    return Mono.just(sttResultResponseDto);
                                });
                    });
        });
}
}

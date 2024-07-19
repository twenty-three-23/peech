package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.cache.RedisTemplateImpl;
import com.twentythree.peech.script.domain.SentenceEntity;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.script.service.ScriptService;
import com.twentythree.peech.script.service.SentenceService;
import com.twentythree.peech.script.stt.dto.AddSentenceInformationVO;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.SentenceVO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTScriptResponseDTO;
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

    private final AddRealTimeToSentenceService addRealTimeToSentenceService;

    private final CreateParagraghService createParagraghService;

    private final ScriptService scriptService;

    private final SentenceService sentenceService;

    private final CreateSTTResultService createSTTResultService;

    private final RedisTemplateImpl redisTemplateImpl;

    public Mono<STTScriptResponseDTO> createSTTResult(STTRequestDto request, Long themeId, Long userId) {
        // redis key에 들어갈 부분은 user + userId 형태임
        String userKey = "user" + userId.toString();

        try {
            Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(request);

            return clovaResponseDtoMono
                    .flatMap(clovaResponseDto -> {

                        String totalText = clovaResponseDto.getFullText();

                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);

                            // Script Entity 저장
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, clovaResponseDto);
                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                // 생성된 scriptId 가져오기
                                long scriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, scriptId);
                                // Redis 저장 로직
                                List<String> sentenceIdList = sentenceEntityList.stream().map(SentenceEntity::getSentenceId).toList();
                                redisTemplateImpl.saveSentencesIdList(userKey, sentenceIdList);
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

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace(); // 예외 로그 출력
            // 적절한 오류 메시지 반환
            return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
        }
    }

    public Mono<STTScriptResponseDTO> createSTTResult(STTRequestDto request, Long themeId, Long scriptId, Long userId) {

        // redis key에 들어갈 부분은 user + userId 형태임
        String userKey = "user" + userId.toString();

        try {
            Mono<ClovaResponseDto> clovaResponseDtoMono = requestClovaSpeechApiService.requestClovaSpeechApi(request);

            return clovaResponseDtoMono
                    .flatMap(clovaResponseDto -> {
                        String totalText = clovaResponseDto.getFullText();

                        Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));
                        return paragraphDivideResponseDtoMono.flatMap(paragraphDivideResponseDto -> {

                            List<AddSentenceInformationVO> sentenceAndRealTimeList = addRealTimeToSentenceService.addRealTimeToSentence(clovaResponseDto, paragraphDivideResponseDto);
                            // Script Entity 저장
                            Mono<SaveSTTScriptVO> saveSTTScriptVOMono = scriptService.saveSTTScriptVO(themeId, scriptId, clovaResponseDto);

                            // Sentence Entity 저장
                            return saveSTTScriptVOMono.flatMap(saveSTTScriptVO -> {

                                long newScriptId = saveSTTScriptVO.scriptEntity().getScriptId();
                                List<SentenceEntity> sentenceEntityList = sentenceService.saveSTTSentences(saveSTTScriptVO.scriptEntity(), sentenceAndRealTimeList, paragraphDivideResponseDto.getResult().getSpan());
                                STTScriptResponseDTO sttScriptResponseDTO = createSTTResultService.createSTTResultResponseDto(clovaResponseDto, sentenceEntityList, sentenceAndRealTimeList, newScriptId);
                                // Redis 저장 로직
                                List<String> sentenceIdList = sentenceEntityList.stream().map(SentenceEntity::getSentenceId).toList();
                                redisTemplateImpl.saveSentencesIdList(userKey, sentenceIdList);
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

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace(); // 예외 로그 출력
            // 적절한 오류 메시지 반환
            return Mono.error(new RuntimeException("STT 결과 생성 중 오류가 발생했습니다.", e));
        }
    }
}

package com.twentythree.peech.script.stt.service;

import com.twentythree.peech.script.domain.ScriptEntity;
import com.twentythree.peech.script.service.ScriptService;
import com.twentythree.peech.script.service.SentenceService;
import com.twentythree.peech.script.stt.dto.EditClovaSpeechSentenceVO;
import com.twentythree.peech.script.stt.dto.SaveSTTScriptVO;
import com.twentythree.peech.script.stt.dto.request.STTRequestDto;
import com.twentythree.peech.script.stt.dto.response.ClovaResponseDto;
import com.twentythree.peech.script.stt.dto.response.ParagraphDivideResponseDto;
import com.twentythree.peech.script.stt.dto.response.STTResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessSTTService {

    private final RequestClovaSpeechApiService requestClovaSpeechApiService;

    private final EditClovaSpeechResponseService editClovaSpeechResponseService;

    private final CreateParagraghService createParagraghService;

    private final ScriptService scriptService;

    private final SentenceService sentenceService;

    public Mono<STTResultResponseDto> createSTTResult(STTRequestDto request, Long themeId) {

        requestClovaSpeechApiService.requestClovaSpeechApi(request)
                .map(clovaResponseDto -> {
                    List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList = editClovaSpeechResponseService.editClovaSpeechResponseSentences(clovaResponseDto);

                    String totalText = sentenceAndRealTimeList
                            .stream().map(EditClovaSpeechSentenceVO::sentenceContent)
                            .collect(Collectors.joining("\\n"));

                    Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));

                    // DB에 저장하는 로직 + Mono 객체에 null값이 들어갔을 경우에 대한 예외처리 필요
                    SaveSTTScriptVO saveSTTScriptVO = scriptService.saveSTTScriptVO(themeId, clovaResponseDto);
                    ScriptEntity scriptEntity = saveSTTScriptVO.scriptEntity();
                    // Sentence Entity 저장 후 Id list 가져오기 + Mono 타입의 dto를 조회할때 block() 메소드를 사용하면 null값에 대한 예외처리 필요
                    List<Long> sentenceIdList = sentenceService.saveSTTSentences(scriptEntity, sentenceAndRealTimeList, paragraphDivideResponseDtoMono.block().getResult().getSpan());

                    //redis에 저장하는 로직 + Mono 객체에 null값이 들어갔을 경우에 대한 예외처리 필요 redis 저장 로직은 추후 추가 예정






                })


    }

    public Mono<STTResultResponseDto> createSTTResult(STTRequestDto request, Long themeId, Long scriptId) {

        requestClovaSpeechApiService.requestClovaSpeechApi(request)
                .map(clovaResponseDto -> {
                    List<EditClovaSpeechSentenceVO> sentenceAndRealTimeList = editClovaSpeechResponseService.editClovaSpeechResponseSentences(clovaResponseDto);
                    String totalText = sentenceAndRealTimeList
                            .stream().map(EditClovaSpeechSentenceVO::sentenceContent)
                            .collect(Collectors.joining("\\n"));

                    Mono<ParagraphDivideResponseDto> paragraphDivideResponseDtoMono = Mono.fromFuture(createParagraghService.requestClovaParagraphApi(totalText));

                    // DB 및 Redis에 저장하는 로직 + Mono 객체에 null값이 들어갔을 경우에 대한 예외처리 필요
                    scriptService.saveSTTScriptVO(themeId, scriptId, clovaResponseDto);

                })


    }
}

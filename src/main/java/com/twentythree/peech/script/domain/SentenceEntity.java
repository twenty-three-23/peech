package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SENTENCE")
@Getter
public class SentenceEntity extends BaseCreatedAtEntity {

    @Id
    @Column(name = "sentence_id")
    private String sentenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id", nullable = false)
    private ScriptEntity scriptEntity;

    @Column(name = "paragraph_id", nullable = false)
    private Long paragraphId;

    @Column(name = "sentence_content", nullable = false, length = 65535, columnDefinition = "TEXT")
    private String sentenceContent;

    @Column(name = "sentence_order", nullable = false)
    private Long sentenceOrder;

    @Column(name = "sentence_expect_time")
    private LocalTime sentenceExpectTime;

    @Column(name = "sentence_real_time")
    private LocalTime sentenceRealTime;

    private SentenceEntity(ScriptEntity scriptEntity, Long paragraphId, String sentenceContent, Long sentenceOrder, LocalTime time){
        this.sentenceId = UUID.randomUUID().toString();
        this.scriptEntity = scriptEntity;
        this.paragraphId = paragraphId;
        this.sentenceContent = sentenceContent;
        this.sentenceOrder = sentenceOrder;
        if(scriptEntity.getDType() == InputAndSttType.INPUT) {
            this.sentenceExpectTime = time;
        } else if(scriptEntity.getDType() == InputAndSttType.STT){
            this.sentenceRealTime = time;
        } else {
            throw new IllegalArgumentException("InputAndSttType이 올바르지 않게 입력 되었다.");
        }
        this.sentenceContent = sentenceContent;
    }

    // 대본 없이 녹음을 시작하면 해당 스크립트는 입력 대본이면서 STT 대본이기에 예상시간, 실제시간 둘다 필요하다.
    public SentenceEntity(ScriptEntity scriptEntity, Long paragraphId, String sentenceContent, Long sentenceOrder, LocalTime realTime, LocalTime expectedTime) {
        this.sentenceId = UUID.randomUUID().toString();
        this.scriptEntity = scriptEntity;
        this.sentenceContent = sentenceContent;
        this.paragraphId = paragraphId;
        this.sentenceOrder = sentenceOrder;
        this.sentenceRealTime = realTime;
        this.sentenceExpectTime = expectedTime;
    }

    public static SentenceEntity ofCreateInputSentence(ScriptEntity scriptEntity,
                                                       Long paragraphId, String sentenceContent,
                                                       Long sentenceOrder, LocalTime sentenceExpectTime){
        if(scriptEntity.getDType() != InputAndSttType.INPUT) {
            throw new IllegalArgumentException("팩토리얼 함수를 잘못 사용했습니다.");
        }
        return new SentenceEntity(scriptEntity, paragraphId, sentenceContent, sentenceOrder, sentenceExpectTime);
    }

    public static SentenceEntity ofCreateSTTSentence(ScriptEntity scriptEntity,
                                                     Long paragraphId, String sentenceContent,
                                                       Long sentenceOrder, LocalTime sentenceRealTime){
        if(scriptEntity.getDType() != InputAndSttType.STT) {
            throw new IllegalArgumentException("팩토리얼 함수를 잘못 사용했습니다.");
        }
        return new SentenceEntity(scriptEntity, paragraphId, sentenceContent, sentenceOrder, sentenceRealTime);
    }

    public static SentenceEntity ofCreateInputAndSTTSentence(ScriptEntity scriptEntity,
                                                             Long paragraphId, String sentenceContent, Long sentenceOrder,
                                                             LocalTime realTime, LocalTime expectedTime) {
        return new SentenceEntity(scriptEntity, paragraphId, sentenceContent, sentenceOrder, realTime, expectedTime);
    }

    public boolean sentenceEquals(String sentenceContent) {
//        if (this.sentenceId.equals(sentence.sentenceId)) {
//            return this.sentenceContent.equals(sentence.getSentenceContent());
//        } else {
//            return false
//        }

        return this.sentenceContent.equals(sentenceContent);
    }

    public String[] sliceSentences(String sentenceContent) {
        return sentenceContent.split("\\.");
    }

}

package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SENTENCE")
@Getter
public class SentenceEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id")
    private Long sentenceId;

    @ManyToOne
    @JoinColumn(name = "script_id")
    private ScriptEntity scriptEntity;

    @Column(name = "paragraph_id", nullable = false)
    private Long paragraphId;

    @Column(name = "sentence_content", nullable = false)
    private String sentenceContent;

    @Column(name = "sentence_order", nullable = false)
    private Long sentenceOrder;

    @Column(name = "sentence_expect_time")
    private LocalTime sentenceExpectTime;

    @Column(name = "sentence_real_time")
    private LocalTime sentenceRealTime;

    private SentenceEntity(ScriptEntity scriptEntity, Long paragraphId, String sentenceContent, Long sentenceOrder, LocalTime time){
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
    }

    public static SentenceEntity ofCreateInputSentence(ScriptEntity scriptEntity,
                                                       Long paragraphId, String sentenceContent,
                                                       Long sentenceOrder, LocalTime sentenceExpectTime){
        if(scriptEntity.getDType() != InputAndSttType.INPUT) {
            throw new IllegalArgumentException("팩토리얼 함수를 잘못 사용했습니다.");
        }
        log.info("여기까지는 됨 {}", paragraphId);
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

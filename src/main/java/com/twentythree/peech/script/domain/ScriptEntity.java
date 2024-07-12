package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SCRIPT")
@Entity
public class ScriptEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "script_id")
    private Long scriptId;

    @OneToOne
    @JoinColumns(value = {
            @JoinColumn(name = "major_version", referencedColumnName = "major_version"),
            @JoinColumn(name = "minor_version", referencedColumnName = "minor_version")
    })
    private VersionEntity version;

    @Column(name = "script_content", nullable = false, length = 65535, columnDefinition = "TEXT")
    private String scriptContent;

    @Column(name = "total_expect_time")
    private LocalTime totalExpectTime;

    @Column(name = "total_real_time")
    private LocalTime totalRealTime;

    @Column(name = "d_type", nullable = false)
    private InputAndSttType DType;

    @OneToMany(mappedBy = "scriptEntity")
    private List<SentenceEntity> sentenceEntities;


    private ScriptEntity(VersionEntity version, String scriptContent, LocalTime time, InputAndSttType DType) {
        this.version = version;
        this.scriptContent = scriptContent;

        if (DType == InputAndSttType.INPUT) {
            this.totalExpectTime = time;
            this.DType = InputAndSttType.INPUT;
        } else if (DType == InputAndSttType.STT) {
            this.totalRealTime = time;
            this.DType = InputAndSttType.STT;
        } else {
            throw new IllegalArgumentException("InputAndSttType이 올바르지 않게 입력 되었다.");
        }
    }


    public static ScriptEntity ofCreateInputScript(VersionEntity version,
                                                   String scriptContent,
                                                   LocalTime totalExpectTime,
                                                   InputAndSttType DType) {
        if (DType != InputAndSttType.INPUT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(version, scriptContent, totalExpectTime, DType);
    }

    public static ScriptEntity ofCreateSTTScript(VersionEntity version,
                                                   String scriptContent,
                                                   LocalTime totalRealTime,
                                                 InputAndSttType DType) {
        if (DType != InputAndSttType.STT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(version, scriptContent, totalRealTime, DType);
    }
}

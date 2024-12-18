package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalTime;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "SCRIPT")
@Entity
public class ScriptEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "script_id")
    private Long scriptId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "major_version", referencedColumnName = "major_version", nullable = false),
            @JoinColumn(name = "minor_version", referencedColumnName = "minor_version", nullable = false),
            @JoinColumn(name = "theme_id", referencedColumnName = "theme_id", nullable = false)
    })
    private VersionEntity version;

    @Column(name = "script_content", nullable = false, length = 65535, columnDefinition = "TEXT")
    private String scriptContent;

    @Column(name = "total_expect_time")
    private LocalTime totalExpectTime;

    @Column(name = "total_real_time")
    private LocalTime totalRealTime;

    @Column(name = "d_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InputAndSttType DType;

    @Column(name = "analysis_result", length = 65535, columnDefinition = "TEXT")
    private String analysisResult;

    private ScriptEntity(VersionEntity version, String scriptContent, LocalTime time, InputAndSttType DType) {
        this.version = version;
        this.scriptContent = scriptContent;

        if (DType == InputAndSttType.INPUT) {
            this.totalExpectTime = time;
            this.DType = InputAndSttType.INPUT;
        } else if (DType == InputAndSttType.MODIFY) {
            this.DType = InputAndSttType.MODIFY;
            this.totalRealTime = time;
        }
        else {
            throw new IllegalArgumentException("InputAndSttType이 올바르지 않게 입력 되었다.");
        }
    }

    private ScriptEntity(VersionEntity version, String scriptContent, LocalTime totalRealTime, LocalTime totalExpectTime, InputAndSttType dType) {
        if (dType != InputAndSttType.STT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        this.version = version;
        this.scriptContent = scriptContent;
        this.totalRealTime = totalRealTime;
        this.totalExpectTime = totalExpectTime;
        this.DType = dType;
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
                                                   LocalTime totalExpectTime,
                                                 InputAndSttType DType) {
        if (DType != InputAndSttType.STT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(version, scriptContent, totalRealTime, totalExpectTime, DType);
    }

    // 추후 리팩토링 예정
    public static ScriptEntity ofCreateModifyScript(VersionEntity version,
                                                   String scriptContent,
                                                   LocalTime totalRealTime,
                                                   InputAndSttType DType) {
        if (DType != InputAndSttType.MODIFY) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(version, scriptContent, totalRealTime, DType);
    }
}

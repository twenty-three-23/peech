package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;


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

    @JoinColumns(value = {
            @JoinColumn(name = "major_vesion"),
            @JoinColumn(name = "minor_version")
    })
    private VersionPk versionFk;

    @Column(name = "script_content", nullable = false )
    private String scriptContent;

    @Column(name = "total_expect_time")
    private LocalTime totalExpectTime;

    @Column(name = "total_real_time")
    private LocalTime totalRealTime;

    @Column(name = "d_type")
    private InputAndSttType DType;

    private ScriptEntity(VersionPk versionFk, String scriptContent, LocalTime time, InputAndSttType DType) {
        this.versionFk = versionFk;
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


    public static ScriptEntity ofCreateInputScript(VersionPk versionFk,
                                                   String scriptContent,
                                                   LocalTime totalExpectTime,
                                                   InputAndSttType DType) {
        if (DType != InputAndSttType.INPUT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(versionFk, scriptContent, totalExpectTime, DType);
    }

    public static ScriptEntity ofCreateSTTScript(VersionPk versionFk,
                                                   String scriptContent,
                                                   LocalTime totalRealTime,
                                                 InputAndSttType DType) {
        if (DType != InputAndSttType.STT) {
            throw new IllegalArgumentException("팩토리 함수를 잘못사용했습니다.");
        }
        return new ScriptEntity(versionFk, scriptContent, totalRealTime, DType);
    }
}

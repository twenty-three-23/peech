package com.twentythree.peech.script.domain;


import com.twentythree.peech.common.domain.BaseTimeEntity;
import com.twentythree.peech.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "THEME")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long themeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "theme_title", nullable = false)
    private String themeTitle;


    private ThemeEntity(UserEntity userEntity, String themeTitle) {
        this.userEntity = userEntity;
        this.themeTitle = themeTitle;
    }

    public static ThemeEntity of(UserEntity userEntity, String themeTitle) {
        return new ThemeEntity(userEntity, themeTitle);
    }

}


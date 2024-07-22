package com.twentythree.peech.script.domain;


import com.twentythree.peech.common.domain.BaseTimeEntity;
import com.twentythree.peech.script.repository.ThemeRepository;
import com.twentythree.peech.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "theme_title")
    private String themeTitle;


    private ThemeEntity(UserEntity userEntity, String themeTitle) {
        this.userEntity = userEntity;
        this.themeTitle = themeTitle;
    }

    public static ThemeEntity of(UserEntity userEntity, String themeTitle) {
        return new ThemeEntity(userEntity, themeTitle);
    }

}


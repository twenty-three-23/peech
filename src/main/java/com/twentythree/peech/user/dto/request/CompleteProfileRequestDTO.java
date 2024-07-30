package com.twentythree.peech.user.dto.request;

import com.twentythree.peech.user.value.UserGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CompleteProfileRequestDTO {

    private String firstName;
    private String lastName;

    private String nickName;

    private LocalDate birth;

    private UserGender gender;
}

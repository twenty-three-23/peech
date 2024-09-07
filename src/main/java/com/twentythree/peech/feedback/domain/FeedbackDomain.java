package com.twentythree.peech.feedback.domain;


import com.twentythree.peech.user.value.*;
import lombok.*;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class FeedbackDomain {

    private Long feedbackId;
    private String feedbackMessage;

    private Long userId;
    private AuthorizationServer authorizationServer;
    private String firstName;
    private String lastName;
    private LocalDate birth;
    private UserGender gender;
    private String email;
    private String nickName;
    private UserRole role;
    private SignUpFinished signUpFinished;
    private UserStatus userStatus;
}

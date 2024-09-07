package com.twentythree.peech.feedback.entity;


import com.twentythree.peech.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FEEDBACK")
@Entity
public class Feedback {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "feedback_message", nullable = false)
    private String feedbackMessage;

    public static Feedback ofJpa(UserEntity user, String feedbackMessage) {
        return Feedback.builder().user(user).feedbackMessage(feedbackMessage).build();
    }
}

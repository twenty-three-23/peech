package com.twentythree.peech.feedback.application;

import com.twentythree.peech.auth.service.SecurityContextHolder;
import com.twentythree.peech.feedback.domain.FeedbackDomain;
import com.twentythree.peech.feedback.domain.FeedbackMapper;
import com.twentythree.peech.script.service.ThemeService;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final ThemeService themeService;
    private final UserRepository userRepository;

    public void createFeedback(String message) {
        Long userId = SecurityContextHolder.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        FeedbackDomain feedbackDomain = FeedbackDomain.of(null, message, userId, user.getAuthorizationServer(), user.getFirstName(), user.getLastName(), user.getBirth(), user.getGender(), user.getEmail(), user.getNickName(), user.getRole(), user.getSignUpFinished(), user.getUserStatus());

        feedbackMapper.insertFeedback(feedbackDomain);
    }
}

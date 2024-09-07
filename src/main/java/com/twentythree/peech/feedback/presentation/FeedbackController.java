package com.twentythree.peech.feedback.presentation;

import com.twentythree.peech.feedback.application.FeedbackService;
import com.twentythree.peech.feedback.value.request.CreateFeedbackRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("api/v1.1/users/feedbacks")
    public void createFeedback(@RequestBody CreateFeedbackRequestDTO message) {
        feedbackService.createFeedback(message.getMessage());
    }
}

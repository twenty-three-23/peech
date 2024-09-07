package com.twentythree.peech.feedback.infrastructure;

import com.twentythree.peech.feedback.domain.FeedbackDomain;
import com.twentythree.peech.feedback.domain.FeedbackMapper;
import com.twentythree.peech.feedback.entity.Feedback;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Repository
public class FeedbackMapperImpl implements FeedbackMapper {

    private final JpaFeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void insertFeedback(FeedbackDomain feedbackDomain) {

        UserEntity userEntity = userRepository.findById(feedbackDomain.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 입니다."));

        feedbackRepository.save(Feedback.ofJpa(userEntity, feedbackDomain.getFeedbackMessage()));
    }
}

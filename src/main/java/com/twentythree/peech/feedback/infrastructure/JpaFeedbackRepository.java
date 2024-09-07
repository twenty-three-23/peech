package com.twentythree.peech.feedback.infrastructure;

import com.twentythree.peech.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFeedbackRepository extends JpaRepository<Feedback, Long> {
}

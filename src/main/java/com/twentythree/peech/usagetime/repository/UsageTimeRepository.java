package com.twentythree.peech.usagetime.repository;

import com.twentythree.peech.usagetime.domain.UsageTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsageTimeRepository extends JpaRepository<UsageTimeEntity, Long> {

    @Query("select us from UsageTimeEntity us " +
            "where us.userEntity.id = :userId")
    Optional<UsageTimeEntity> findByUserId(Long userId);

    @Modifying
    @Query("update UsageTimeEntity us set us.remainingTime = :remainingTime " +
            "where us.userEntity.id = :userId")
    void updateRemainingTime(Long userId, Long remainingTime);
}

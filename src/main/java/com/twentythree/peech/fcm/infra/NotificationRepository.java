package com.twentythree.peech.fcm.infra;

import com.twentythree.peech.fcm.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n.fcmToken from NotificationEntity n where n.userEntity.id = :userId")
    List<String> findAllByUserId(Long userId);

}

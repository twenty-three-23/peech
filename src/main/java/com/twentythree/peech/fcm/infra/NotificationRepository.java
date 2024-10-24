package com.twentythree.peech.fcm.infra;

import com.twentythree.peech.fcm.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n.fcmToken from NotificationEntity n where n.userEntity.id = :userId")
    List<String> findAllByUserId(Long userId);

    @Query("select n from NotificationEntity n where n.deviceId = :deviceId")
    Optional<NotificationEntity> findNotificationByDeviceId(String deviceId);

    @Modifying
    @Query("update NotificationEntity n set n.fcmToken = :fcmToken where n.deviceId = :deviceId")
    void updateTokenByDeviceId(String fcmToken, String deviceId);
}

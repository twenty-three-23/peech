package com.twentythree.peech.user.repository;

import com.twentythree.peech.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByDeviceId(String deviceId);

    Optional<UserEntity> findByNickName(String nickName);
}

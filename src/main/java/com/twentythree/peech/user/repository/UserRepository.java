package com.twentythree.peech.user.repository;

import com.twentythree.peech.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

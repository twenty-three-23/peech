package com.twentythree.peech.usagetime.domain;

import com.twentythree.peech.common.domain.BaseTimeEntity;
import com.twentythree.peech.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "USAGE_TIME")
@Entity
public class UsageTimeEntity extends BaseTimeEntity {

    @Id @Column(name = "usage_time_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usageTimeId;

    @Column(name = "remaining_time") @ColumnDefault("9000")
    private Long remainingTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    public void updateRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

    // factory method

    public static UsageTimeEntity of(UserEntity userEntity) {
        return new UsageTimeEntity(userEntity);
    }


    // constructor

    public UsageTimeEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}

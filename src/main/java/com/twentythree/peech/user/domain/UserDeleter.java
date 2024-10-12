package com.twentythree.peech.user.domain;

import com.twentythree.peech.common.utils.TimeUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserDeleter {

    public LocalDateTime deleteUser(UserDomain userDomain) {
        userDomain.changeUserStatusToDelete();

        LocalDateTime nowUTC = TimeUtils.CreateNowUTC();

        return userDomain.setDeleteAt(nowUTC);
    }
}

package com.twentythree.peech.user.domain;

import com.twentythree.peech.common.utils.TimeUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserDeleter {

    public LocalDate deleteUser(UserDomain userDomain) {
        userDomain.changeUserStatusToDelete();

        LocalDate nowUTC = TimeUtils.CreateNowUTC();

        return userDomain.setDeleteAt(nowUTC);
    }
}

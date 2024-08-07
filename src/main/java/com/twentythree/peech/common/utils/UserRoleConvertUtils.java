package com.twentythree.peech.common.utils;

import com.twentythree.peech.user.value.UserRole;

public class UserRoleConvertUtils {

    public static UserRole convertStringToUserRole(String role) {

        if (role.equals("ROLE_ADMIN")) {
            return UserRole.ROLE_ADMIN;
        } else if (role.equals("ROLE_COMMON")) {
            return UserRole.ROLE_COMMON;
        } else {
            throw new IllegalArgumentException("존재하지 않는 권한입니다.");
        }
        
    }
}

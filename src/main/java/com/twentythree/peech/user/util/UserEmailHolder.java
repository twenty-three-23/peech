package com.twentythree.peech.user.util;


import static com.twentythree.peech.meta.util.MetaUtil.*;

public class UserEmailHolder {
    private static final ThreadLocal<String> userEmailThreadLocal = new ThreadLocal<>();

    public static void setUserEmail(String userEmail) {
        userEmailThreadLocal.set(encryptSHA256(userEmail));
    }

    public static String getUserEmail() {
        return userEmailThreadLocal.get();
    }

    public void clear() {
        userEmailThreadLocal.remove();
    }
}

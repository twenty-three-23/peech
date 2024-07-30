package com.twentythree.peech.user.service;


import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;

import java.time.LocalDate;

public interface UserService {
    String createUserByDeviceId(String deviceId) throws UserAlreadyExistException;
    String reIssuanceUserToken(String deviceId);
    AccessAndRefreshToken loginBySocial(String socialToken, AuthorizationServer authorizationServer);
}

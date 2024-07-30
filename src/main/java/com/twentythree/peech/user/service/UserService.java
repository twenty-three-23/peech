package com.twentythree.peech.user.service;


import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.domain.UserDomain;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;

import java.time.LocalDate;

public interface UserService {
    String createUserByDeviceId(String deviceId) throws UserAlreadyExistException;
    String reIssuanceUserToken(String deviceId);
    AccessAndRefreshToken createUserBySocial(String socialId, AuthorizationServer authorizationServer, String firstName, String lastName, LocalDate birth, String email, UserGender gender, String nickName);
    UserDomain deleteUser(Long userId);
}

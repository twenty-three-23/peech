package com.twentythree.peech.user.service;


import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.domain.UserDomain;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;


public interface UserService {
    String createUserByDeviceId(String deviceId) throws UserAlreadyExistException;
    String reIssuanceUserToken(String deviceId);
    UserDomain deleteUser(Long userId);
    AccessAndRefreshToken loginBySocial(String socialToken, AuthorizationServer authorizationServer);
}

package com.twentythree.peech.user.service;


import com.twentythree.peech.common.exception.UserAlreadyExistException;

public interface UserService {
    String createUser(String deviceId) throws UserAlreadyExistException;
    String reIssuanceUserToken(String deviceId);
}

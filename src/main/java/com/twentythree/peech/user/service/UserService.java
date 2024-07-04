package com.twentythree.peech.user.service;


public interface UserService {
    String createUser(String deviceId);
    String reIssuanceUserToken(String deviceId);
}

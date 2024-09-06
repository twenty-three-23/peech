package com.twentythree.peech.user.service;


import com.twentythree.peech.common.exception.UserAlreadyExistException;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import com.twentythree.peech.user.dto.response.GetUserInformationResponseDTO;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.value.AuthorizationServer;
import com.twentythree.peech.user.value.UserGender;
import com.twentythree.peech.user.domain.UserDomain;
import com.twentythree.peech.user.dto.LoginBySocial;
import java.time.LocalDate;


public interface UserService {
    String createUserByDeviceId(String deviceId) throws UserAlreadyExistException;
    String reIssuanceUserToken(String deviceId);
    UserDomain deleteUser(Long userId);
    LoginBySocial loginBySocial(String socialToken, AuthorizationServer authorizationServer, String funnel);
    LoginBySocial completeProfile(Long userId, String firstName, String lastName, String nickName, LocalDate birth, UserGender gender, String funnel);
    UserEntity existUserById(Long userId);
    GetUserInformationResponseDTO getUserInformation();
    AccessAndRefreshToken createNewToken(String refreshToken, Long userId, String funnel);
}

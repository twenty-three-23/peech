package com.twentythree.peech.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AuthorizationIdentifier {

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "authorization_server", nullable = false)
    private AuthorizationServer authorizationServer;

    public static AuthorizationIdentifier of(String socialId, AuthorizationServer authorizationServer) {
        return new AuthorizationIdentifier(socialId, authorizationServer);
    }
}

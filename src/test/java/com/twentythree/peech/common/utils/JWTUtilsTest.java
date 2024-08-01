package com.twentythree.peech.common.utils;

import com.twentythree.peech.user.dto.IdentityToken;
import com.twentythree.peech.user.dto.IdentityTokenHeader;
import com.twentythree.peech.user.dto.IdentityTokenPayload;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JWTUtilsTest {

    @Autowired private JWTUtils jwtUtils;

    @Test
    public void decodeJwt() throws Exception {
        //Given

        String jwt = "eyJhbGciOiJFUzI1NiIsImtpZCI6InF3ZSJ9.eyJzdWIiOiJjb20ubXl0ZXN0LmFwcCIsImlzcyI6IkRFRjEyM0dISUoiLCJpYXQiOjE0MzcxNzkwMzYsImV4cCI6MTIzMTIzMTIzLCJhdWQiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiZW1haWwiOiJxd2UifQ.u8y5Ujn3mYebx7xZ_SwyTqgPEQ9idLuDX8LWr3r1hbL36eOzthXS-EUmDxEaJDPR4TNn045SMdqMcjfAU7x29w";

        //When

        IdentityToken identityToken = jwtUtils.decodeIdentityToken(jwt);

        //Then

        IdentityTokenHeader identityTokenHeader = new IdentityTokenHeader("ES256", "qwe");
        IdentityTokenPayload identityTokenPayload = new IdentityTokenPayload("DEF123GHIJ", 1437179036L, 123123123L, "https://appleid.apple.com", "com.mytest.app", "qwe");
        IdentityToken resultIdentityToken = new IdentityToken(identityTokenHeader, identityTokenPayload);

        Assertions.assertThat(identityToken).isEqualTo(resultIdentityToken);

    }

}
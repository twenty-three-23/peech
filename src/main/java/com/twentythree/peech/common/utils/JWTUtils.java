package com.twentythree.peech.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.common.JwtProperties;
import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import com.twentythree.peech.user.dto.IdentityToken;
import com.twentythree.peech.user.dto.IdentityTokenHeader;
import com.twentythree.peech.user.dto.IdentityTokenPayload;
import com.twentythree.peech.user.value.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final JwtProperties jwtProperties;

    private String secretString;
    private String accessString;
    private String refreshString;

    private SecretKey secretKey;
    private SecretKey accessKey;
    private SecretKey refreshKey;

    private Date accessExpirationDate;
    private Date refreshExpirationDate;

    private final Long accessExpiration = 30L;
    private final Long refreshExpiration = 60L;


    @PostConstruct
    public void init() {

        secretString = jwtProperties.getSecretString();
        accessString = jwtProperties.getAccessString();
        refreshString = jwtProperties.getRefreshString();

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));

        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessString));

        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshString));

    }

    private void setExpirationDate() {
        Date today = new Date();
        this.accessExpirationDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * accessExpiration));
        this.refreshExpirationDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * refreshExpiration));
    }



    public String createJWT(Long userId) {
        return Jwts.builder().
                header().
                and().
                subject("Token").
                claim("userId", userId).
                signWith(secretKey, Jwts.SIG.HS256).
                compact();
    }

    public String createAccessToken(Long userId, UserRole userRole, String funnel) {
        setExpirationDate();

        return Jwts.builder().
                header().type("JWT").
                and().
                subject("AccessToken").
                claim("userId", userId).
                claim("userRole", userRole).
                claim("site", funnel).
                expiration(accessExpirationDate).
                signWith(accessKey, Jwts.SIG.HS256).
                compact();
    }

    public String createRefreshToken(Long userId, UserRole userRole, String funnel) {
        setExpirationDate();

        return Jwts.builder().
                header().type("JWT").
                and().
                subject("RefreshToken").
                claim("userId", userId).
                claim("userRole", userRole).
                claim("site", funnel).
                expiration(refreshExpirationDate).
                signWith(refreshKey, Jwts.SIG.HS256).
                compact();
    }

    public Jws<Claims> parseJWT(String token) {

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public Jws<Claims> parseAccessToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public Jws<Claims> parseRefreshToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Claims validateAccessToken(String token) throws JWTAuthenticationException {
        try {
            Jws<Claims> claimsJws = parseAccessToken(token);
            return claimsJws.getPayload();
        } catch (SignatureException e) {
            log.error("JWT Token Signature is invalid", e);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
            throw new JWTAuthenticationException(LoginExceptionCode.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT Token is empty", e);
        }
        throw new JWTAuthenticationException(LoginExceptionCode.LOGIN_EXCEPTION_CODE);
    }

    public Claims validateRefreshToken(String token) throws JWTAuthenticationException {
        try {
            Jws<Claims> claimsJws = parseRefreshToken(token);
            return claimsJws.getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token", e);
            throw new JWTAuthenticationException(LoginExceptionCode.REFRESH_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT Token is empty", e);
        }
        throw new JWTAuthenticationException(LoginExceptionCode.LOGIN_EXCEPTION_CODE);
    }

    public IdentityToken decodeIdentityToken(String token) {
        try {

            String[] splitToken = token.split("\\.");

            String header = splitToken[0];
            String payload = splitToken[1];

            String tokenHeader = new String(Decoders.BASE64.decode(header));
            String tokenPayload = new String(Decoders.BASE64.decode(payload));


            ObjectMapper objectMapper = new ObjectMapper();

            IdentityTokenHeader identityTokenHeader = objectMapper.readValue(tokenHeader, IdentityTokenHeader.class);
            IdentityTokenPayload identityTokenPayload = objectMapper.readValue(tokenPayload, IdentityTokenPayload.class);


            return new IdentityToken(identityTokenHeader, identityTokenPayload);
        } catch (Exception e) {
            throw new IllegalArgumentException("error", e);
        }
    }
}

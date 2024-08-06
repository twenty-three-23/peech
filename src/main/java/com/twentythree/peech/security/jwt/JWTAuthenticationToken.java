package com.twentythree.peech.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// 사용자의 인증
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    // 사용자의 정보가 저장되어 있는 JWTAuthentication 객체를 받아서 인증을 수행
    private final JWTAuthentication principal;

    // 검증 후 인증된 사용자의 정보를 저장
    public JWTAuthenticationToken(JWTAuthentication principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.principal = principal;
    }

    @Override
    public String getCredentials() {
        return "";
    }

    @Override
    public JWTAuthentication getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        super.setAuthenticated(false);
    }

}

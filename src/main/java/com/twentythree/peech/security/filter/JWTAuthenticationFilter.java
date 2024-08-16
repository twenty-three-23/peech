package com.twentythree.peech.security.filter;

import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import com.twentythree.peech.security.jwt.JWTAuthenticationToken;
import com.twentythree.peech.security.jwt.JWTUserDetails;
import com.twentythree.peech.security.jwt.JWTUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.twentythree.peech.common.utils.JWTUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// JWT 토큰 인증 정보를 Thread의 SecurityContext에 저장하는 필터
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final JWTUserDetailsService jwtUserDetailsService;
// 정규 표현식 -> 토큰 복호화시 Bearer 글자 제거
    private final Pattern bearerRegex = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
    private final String header = "Authorization";

    public JWTAuthenticationFilter(JWTUtils jwtUtils, JWTUserDetailsService jwtUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰이 유효한지 확인
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String jwtToken = getJWTToken(request);

            if (jwtToken != null) {
                try {
                    Claims claims;

                    // 검증할 토큰
                    if ("api/v1.1/auth/reissue".equals(request.getRequestURI())) {
                        claims = jwtUtils.validateRefreshToken(jwtToken);
                    } else {
                        claims = jwtUtils.validateAccessToken(jwtToken);
                    }

                    Long userId = claims.get("userId", Long.class);

                    List<GrantedAuthority> authorities = createAuthorities(claims);

                    if (userId != null && !authorities.isEmpty()) {

                        JWTUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(String.valueOf(userId));

                        Long userDetailsId = userDetails.getUserEntity().getId();

                        JWTAuthentication jwtAuthentication = new JWTAuthentication(userDetailsId);

                        JWTAuthenticationToken authenticationToken = new JWTAuthenticationToken(jwtAuthentication, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTAuthenticationException e) {
                    // 로그 기록
                    log.error("JWTAuthenticationFilter: JWT token validation failed", e);
                    // 예외를 다시 던져서 EntryPoint에서 처리하게 함
                    request.setAttribute("JWTAuthenticationException", e);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
        }
        filterChain.doFilter(request, response);

    }

    private String getJWTToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (token != null) {
            String[] parts = token.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];   // Bearer
                String credentials = parts[1];
                if (bearerRegex.matcher(scheme).matches()) {
                    return credentials;
                }
            }
        }
        return null;
    }

    private List<GrantedAuthority> createAuthorities(Claims claims) {

        String[] roles = claims.get("userRole", String.class).split(",");

        if (roles.length == 0) {
            throw new IllegalArgumentException("해당 토큰에 권한이 존재하지 않습니다");
        } else {
            return Arrays.stream(roles)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}

package org.zerock.board.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.board.security.exception.AccessTokenExepction;
import org.zerock.board.util.JWTUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
        }

        log.info("Token check Filter...");
        log.info("JWTUtil: " + jwtUtil);

        try {
            validateAccessToken(request);
            filterChain.doFilter(request, response);
        } catch (AccessTokenExepction accessTokenExepction) {
            accessTokenExepction.sendResponseError(response);
        }

    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenExepction {
        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8) {
            throw new AccessTokenExepction(AccessTokenExepction.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false) {
            throw new AccessTokenExepction(AccessTokenExepction.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException-------------------");
            throw new AccessTokenExepction(AccessTokenExepction.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            log.error("SignatureException-------------------");
            throw new AccessTokenExepction(AccessTokenExepction.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException-------------------");
            throw new AccessTokenExepction(AccessTokenExepction.TOKEN_ERROR.EXPIRED);
        }
    }
}

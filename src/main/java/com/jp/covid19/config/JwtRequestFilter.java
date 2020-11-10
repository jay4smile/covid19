package com.jp.covid19.config;

import com.jp.covid19.exception.CustomException;
import com.jp.covid19.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * Class: JwtRequestFilter
 *
 * Desc: Filter used to check authentication token.
 * If token exist it will authenticate user based on token
 * Else it will clear out security contexxt and throws error
 */
@Component
@Data
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String token = jwtTokenUtil.resolveToken(httpServletRequest);
        try {
            if (token != null && jwtTokenUtil.validateToken(token)) {
                Authentication auth = jwtTokenUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (CustomException exception) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(exception.getHttpStatus().value(), exception.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}


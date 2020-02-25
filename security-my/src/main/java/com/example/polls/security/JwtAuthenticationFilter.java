package com.example.polls.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            if (request.getRequestURI().contains("signin")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, false)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                /*
                    Note that you could also encode the user's username and roles inside JWT claims
                    and create the UserDetails object by parsing those claims from the JWT.
                    That would avoid the following database hit. It's completely up to you.
                 */

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException ex) {
            List<Cookie> requestCookieList = Arrays.asList(request.getCookies());
            for (Cookie cookie : requestCookieList) {
                if ("refreshToken".equals(cookie.getName())) {
                    try {
                        if (tokenProvider.validateToken(cookie.getValue(), true)) {
                            Long userId = tokenProvider.getUserIdFromRefreshJWT(cookie.getValue());
                            UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            String newAccessToken = tokenProvider.generateToken(authentication);
                            String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

                            response.addHeader(HttpHeaders.SET_COOKIE, "refreshToken=" + newRefreshToken + ";Max-Age=86400;" + "Domain=localhost;Path=/;HttpOnl");
                            response.setStatus(HttpStatus.CONFLICT.value());
                            response.setContentType("application/json");

                            PrintWriter out = response.getWriter();
                            String jsonStr = "{\"token\":\"" + newAccessToken + "\"}";
                            out.println(jsonStr);

                            return;
                        } else {
                            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                            return;
                        }
                    } catch (ExpiredJwtException refreshTokenEx) {
                        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                        refreshTokenEx.printStackTrace();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}

package ru.netology.cloudstorage.security.jwt;

import ru.netology.cloudstorage.security.UserDetailsService;
import ru.netology.cloudstorage.service.impl.JwtBlackListServiceImpl;
import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    JwtUtils jwtUtils;
    UserDetailsService userDetailsServiceImpl;
    String tokenPrefix = "Bearer ";
    String headerString = "auth-token";
    JwtBlackListServiceImpl jwtBlackListService;


    public JwtTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsServiceImpl,
                          JwtBlackListServiceImpl jwtBlackListService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(headerString);
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith(tokenPrefix)) {
            String jwt = authHeader.replace(tokenPrefix, "");
            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
                log.error("Invalid JWT Token in Bearer Header");
            } else if (jwtBlackListService.isBlacklisted(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please, login");
                log.error("there is JWT Token in blacklist");
            } else {
                try {
                    String username = jwtUtils.validateTokenRetrieveClaim(jwt);
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                                    userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                    log.error("Invalid JWT Token", e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

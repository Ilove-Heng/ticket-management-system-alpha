package com.nangseakheng.user.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nangseakheng.user.config.properties.JwtConfigProperties;
import com.nangseakheng.user.service.JwtService;
import com.nangseakheng.user.utils.CustomMessageExceptionUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationInternalFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("[DEBUG] JwtAuthenticationInternalFilter: Received request for URI: {}", uri);
        // Skip JWT validation for public endpoints
        if (uri.startsWith("/api/public/users/")) {
            log.info("[DEBUG] JwtAuthenticationInternalFilter: Skipping JWT validation for public endpoint: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        var accessToken = request.getHeader(jwtConfigProperties.getHeader());
        log.info("Do filter uri {}", request.getRequestURI());

        if(StringUtils.hasText(accessToken) && accessToken.startsWith(jwtConfigProperties.getPrefix())) {
            accessToken = accessToken.substring((jwtConfigProperties.getPrefix()).length());
            try {
                if(jwtService.isValidToken(accessToken)){
                    Claims claims = jwtService.extractClaims(accessToken);
                    var username = claims.getSubject();

                    List<String> authorities = claims.get("authorities", List.class);
                    if(username != null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        username, null,
                                        authorities.stream().map(SimpleGrantedAuthority::new)
                                                .collect(Collectors.toList())
                                );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }catch (Exception ex){
                log.error("{}", ex.getLocalizedMessage());
                var messageException = CustomMessageExceptionUtils.unauthorized();
                var msgJson = objectMapper.writeValueAsString(messageException);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(msgJson);
                return;
            }
        }

        log.info("Do filter {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

}
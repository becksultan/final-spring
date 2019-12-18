package com.web.router;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RibbonClient(name = "ribbon-client", configuration = FrontConfig.class)
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private RestTemplate restTemplate;

    public JwtRequestFilter(JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null) {
            String validity = restTemplate.getForObject(
                    "http://localhost:8005/validate/" + token,
                    String.class
            );
            assert validity != null;
            if (validity != "Invalid") {
                UserDetailsService service = new CustomUserDetails();
                UserDetails userDetails = service.loadUserByUsername(validity);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
    }
}

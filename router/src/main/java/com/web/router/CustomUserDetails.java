package com.web.router;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetailsService {
    private RestTemplate restTemplate;

    public CustomUserDetails(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CustomUserDetails() {}

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_USER");
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "$2y$12$TkYy.P00yefluw2.QBtVKuN2x4HFZwVdwFahyqxxm5UzkT2dCcVuu",
                authorityList
        );
    }
}

package com.auction.utils;

import com.auction.model.User;
import com.auction.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.auction.utils.Utils.validatePassword;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private MainService service;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String login = auth.getName();
        String password = auth.getCredentials().toString();
        List<GrantedAuthority> authorityList = new ArrayList<>();

        User user = service.getUser(login);
        if (user != null) {
            if (!validatePassword(password, login, user.getPassword())) {
                throw new BadCredentialsException("Wrong password.");
            }
            authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
            return new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), authorityList);
        }
        throw new BadCredentialsException("Wrong username.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.service;

import com.rcs.shoe.shop.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rajko
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;
    
    @Autowired
    private AuthenticationProvider authProvider;

    @Autowired
    private UserDetailsManager userDetailsManager;

    public UserDetails authenticate(String username, String password) {
        LOGGER.debug("In authenticate.");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = null;
        try {
            authentication = authProvider.authenticate(token);
        } catch (AuthenticationException e) {
            LOGGER.debug("AuthenticationException", e);
        }
        
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public void save(UserDetails user) {
        LOGGER.debug("In save.");
        userDetailsManager.createUser(user);
    }
    
    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public void logOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}

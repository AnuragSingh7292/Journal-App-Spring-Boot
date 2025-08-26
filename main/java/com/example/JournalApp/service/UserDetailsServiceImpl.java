package com.example.JournalApp.service;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private userRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
        Users user;

        if (loginInput.contains("@") && loginInput.contains(".")) {
            user = userRepo.findByEmail(loginInput);
        } else {
            user = userRepo.findByUsername(loginInput);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + loginInput);
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}

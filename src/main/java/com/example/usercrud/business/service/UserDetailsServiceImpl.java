package com.example.usercrud.business.service;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl /*implements UserDetailsService*/ {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        log.info("user: {}", user);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username with this email not found");
        }
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
    }
}

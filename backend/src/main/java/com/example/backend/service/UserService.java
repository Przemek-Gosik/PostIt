package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registerUser(UserDto userDto){
        User newUser = User.builder()
                .login(userDto.getLogin())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        User savedUser = userRepository.save(newUser);
    }

    public void logInUser(UserDto userDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userDto.getLogin(),userDto.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findUsersByLogin(userDto.getLogin())
                .orElseThrow(()->new UsernameNotFoundException("User not found for login "+userDto.getLogin()));

    }
}

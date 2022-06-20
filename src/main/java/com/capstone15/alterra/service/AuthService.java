package com.capstone15.alterra.service;

import com.capstone15.alterra.config.security.JwtTokenProvider;
import com.capstone15.alterra.domain.dao.UserDao;
import com.capstone15.alterra.domain.dto.UserDto;
import com.capstone15.alterra.domain.dto.payload.TokenResponse;
import com.capstone15.alterra.domain.dto.payload.UsernamePassword;
import com.capstone15.alterra.domain.dto.payload.UsernamePasswordFGD;
import com.capstone15.alterra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public UserDao register(UsernamePasswordFGD req) {
        UserDao user = new UserDao();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());

        return userRepository.save(user);
    }

    public TokenResponse generateToken(UsernamePassword req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()

                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            return tokenResponse;

        } catch (BadCredentialsException e) {
            log.error("Bad credential", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}


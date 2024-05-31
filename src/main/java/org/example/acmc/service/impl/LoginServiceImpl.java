package org.example.acmc.service.impl;

import jakarta.mail.MessagingException;

import org.example.acmc.exception.ResourceNotFoundException;
import org.example.acmc.model.AuthResponse;
import org.example.acmc.model.UserEntity;
import org.example.acmc.repository.UserRepository;
import org.example.acmc.security.JwtGenerator;

import org.example.acmc.service.LoginService;

import org.example.acmc.util.EmailUtil;
import org.example.acmc.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;
    private OtpUtil otpUtil;
    private EmailUtil emailUtil;
    private UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, OtpUtil otpUtil,
                            EmailUtil emailUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.userRepository = userRepository;
    }

    @Override
    public String auth(UserEntity userEntity) {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(userEntity.getEmail()
                        , userEntity.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "user exist";
    }

    @Override
    public String twoFactorAuth(UserEntity user) {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        UserEntity userEntity = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User", "Email", user.getEmail()));
        userEntity.setOtp(otp);
        userEntity.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(userEntity);

        return "OTP generated and send it to " + user.getEmail();
    }

    @Override
    public String verifyAccount(String email, String otp) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setActive(true);
            userRepository.save(user);
            return "You are successfully authenticated, please return to the website and click CONTINUE";
        }
        return "OTP timed-out, please try again  ";
    }

    @Override
    public AuthResponse verifyOtp(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        boolean isActive = user.isActive();
        if (isActive){
            String token = jwtGenerator.generateToken(email);
            return new AuthResponse(token);
        }
        return null;
    }
}

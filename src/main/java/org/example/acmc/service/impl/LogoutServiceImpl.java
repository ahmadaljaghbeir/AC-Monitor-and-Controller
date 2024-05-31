package org.example.acmc.service.impl;


import org.example.acmc.model.UserEntity;
import org.example.acmc.repository.UserRepository;

import org.example.acmc.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {
    private UserRepository userRepository;

    @Autowired
    public LogoutServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String logout(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        user.setActive(false);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
        return "Logged out successfully";
    }
}

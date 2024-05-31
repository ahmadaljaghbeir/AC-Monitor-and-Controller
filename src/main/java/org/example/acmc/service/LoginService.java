package org.example.acmc.service;

import org.example.acmc.model.AuthResponse;
import org.example.acmc.model.UserEntity;

public interface LoginService {
    String auth(UserEntity user);
    String twoFactorAuth(UserEntity user);
    String verifyAccount(String email, String otp);
    AuthResponse verifyOtp(String email);
}

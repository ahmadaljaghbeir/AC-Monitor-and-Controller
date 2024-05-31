package org.example.acmc.service;


import org.example.acmc.model.UserEntity;

public interface SignUpService {
    UserEntity addUser(UserEntity userEntity);

    UserEntity getUserByEmail(String email);

    UserEntity updateUser(UserEntity userEntity, String email);

    void deleteUser(long id);
}

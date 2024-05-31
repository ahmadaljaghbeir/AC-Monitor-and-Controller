package org.example.acmc.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.acmc.security.AES;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Convert(converter = AES.class)
    @Column
    private String first_name;

    @Convert(converter = AES.class)
    @Column
    private String last_name;

    @Convert(converter = AES.class)
    @Column
    private String email;

    @Convert(converter = AES.class)
    @Column
    private String password;

    @Convert(converter = AES.class)
    @Column
    private String date_of_birth;

    @Convert(converter = AES.class)
    @Column
    private String phone_number;

    @Convert(converter = AES.class)
    @Column
    private boolean active;

    @Convert(converter = AES.class)
    @Column
    private String otp;

    @Convert(converter = AES.class)
    @Column
    private LocalDateTime otpGeneratedTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private List<Role> roles = new ArrayList<>();
}

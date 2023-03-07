package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String phoneNumber;
    private String emailAddress;
    private LocalDate birthDate;
    private String nickname;
    private String password;
    private String profileImageUrl;
    private String name;
    private String introduce;
    private String gender;
    private String accountStatus;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

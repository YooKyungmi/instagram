package com.example.demo.src.user.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
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

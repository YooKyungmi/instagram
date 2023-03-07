package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserByEmailReq {

    @JsonProperty("email_address")
    private String emailAddress;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    private String nickname;
    private String password;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}


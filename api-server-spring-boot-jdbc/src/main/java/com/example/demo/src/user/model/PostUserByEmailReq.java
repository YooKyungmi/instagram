package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserByEmailReq {

    @JsonProperty("email_address")
    private String emailAddress;
    @JsonProperty("birth_date")
    private String birthDate;
    private String nickname;
    private String password;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}


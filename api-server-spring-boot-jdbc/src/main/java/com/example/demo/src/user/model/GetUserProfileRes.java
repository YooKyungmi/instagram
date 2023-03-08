package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileRes {

    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}

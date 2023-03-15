package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchProfileImageReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;

}

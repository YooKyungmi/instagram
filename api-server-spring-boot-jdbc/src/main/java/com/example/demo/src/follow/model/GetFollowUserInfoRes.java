package com.example.demo.src.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFollowUserInfoRes {

    private String nickname;
    private String name;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    @JsonProperty("follow_status")
    private int followStatus;
    @JsonProperty("story_status")
    private int storyStatus;
}
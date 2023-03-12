package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserSearchRes {

    @JsonProperty("user_id")
    private int userId;
    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    private String name;
    @JsonProperty("connected_count")
    private Integer connectedCount;
    @JsonProperty("connected_friend_nickname")
    private String connectedFriendNickname;

}

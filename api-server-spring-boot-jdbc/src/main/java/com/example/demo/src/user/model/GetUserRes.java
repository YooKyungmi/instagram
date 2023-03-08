package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserRes {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email_address")
    private String emailAddress;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    private String name;
    private String introduce;
    private String gender;
    @JsonProperty("follower_count")
    private int followerCount;
    @JsonProperty("following_count")
    private int followingCount;
    @JsonProperty("post_count")
    private int postCount;
    @JsonProperty("connectedCount")
    private int connectedCount;
    @JsonProperty("connected_friend_nickname_1")
    private int connectedFriendNickname1;
    @JsonProperty("connected_friend_nickname_2")
    private int connectedFriendNickname2;
    @JsonProperty("account_status")
    private String accountStatus;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}

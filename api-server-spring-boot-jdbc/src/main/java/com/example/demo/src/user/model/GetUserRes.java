package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String birthDate;
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
    @JsonProperty("connected_count")
    private int connectedCount;
    @JsonProperty("connected_friend_profiles")
    private List<GetUserProfileRes> connectedFriendProfiles;
    @JsonProperty("account_status")
    private String accountStatus;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}

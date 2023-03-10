package com.example.demo.src.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetConnectedFollowRes {

    @JsonProperty("follower_count")
    private Integer followerCount;
    @JsonProperty("following_count")
    private Integer followingCount;
    @JsonProperty("connected_count")
    private Integer connectedCount;
    @JsonProperty("connected_follows")
    private List<GetFollowUserInfoRes> getFollowUserInfoResList;

}

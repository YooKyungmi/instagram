package com.example.demo.src.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFollowingRes {

    @JsonProperty("follower_count")
    private int followerCount;
    @JsonProperty("following_count")
    private int followingCount;
    @JsonProperty("connected_count")
    private int connectedCount;
    @JsonProperty("followings")
    private List<GetFollowingInfoRes> getFollowingInfoResList;

}

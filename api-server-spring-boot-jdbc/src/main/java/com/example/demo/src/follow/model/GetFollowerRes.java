package com.example.demo.src.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFollowerRes {

    @JsonProperty("follower_count")
    private Integer followerCount;
    @JsonProperty("following_count")
    private Integer followingCount;
    @JsonProperty("connected_count")
    private Integer connectedCount;
    @JsonProperty("followers")
    private List<GetFollowUserInfoRes> getFollowUserInfoResList;

}

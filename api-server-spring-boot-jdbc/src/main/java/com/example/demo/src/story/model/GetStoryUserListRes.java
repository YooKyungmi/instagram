package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStoryUserListRes {

    @JsonProperty("online_story_user")
    private GetStoryUserRes onlineStoryUser;
    @JsonProperty("not_viewed_story_users")
    private List<GetStoryUserRes> notViewedStoryUserList;
    @JsonProperty("viewed_story_users")
    private List<GetStoryUserRes> viewedStoryUserList;



}

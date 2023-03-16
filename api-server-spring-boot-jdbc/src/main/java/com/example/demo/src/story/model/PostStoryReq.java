package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostStoryReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("story_url")
    private String storyUrl;
}

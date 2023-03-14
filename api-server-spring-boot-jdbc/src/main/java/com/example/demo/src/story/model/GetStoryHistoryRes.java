package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStoryHistoryRes {

    @JsonProperty("user_story_id")
    private int userStoryId;
    @JsonProperty("story_url")
    private String storyUrl;
    @JsonProperty("created_at")
    private String createdAt;
}

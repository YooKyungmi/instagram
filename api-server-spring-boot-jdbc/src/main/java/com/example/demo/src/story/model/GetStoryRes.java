package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStoryRes {

    @JsonProperty("user_story_id")
    private int userStoryId;
    @JsonProperty("story_viewer_count")
    private Integer storyViewerCount;
    @JsonProperty("story_viewer_profile_image_urls")
    private List<String> storyViewerProfileImageUrls;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    @JsonProperty("story_url")
    private String storyUrl;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

}

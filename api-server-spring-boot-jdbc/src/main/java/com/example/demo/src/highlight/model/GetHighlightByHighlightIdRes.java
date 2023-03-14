package com.example.demo.src.highlight.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetHighlightByHighlightIdRes {
    @JsonProperty("user_highlight_id")
    private Integer userHighlightId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("story_id")
    private int storyId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cover_image_url")
    private String coverImgUrl;
    @JsonProperty("story_url")
    private String storyUrl;
    @JsonProperty("created_at")
    private String createdAt;
}

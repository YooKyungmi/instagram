package com.example.demo.src.highlight.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetHighlightByUserIdRes {

    @JsonProperty("user_highlight_id")
    private Integer userHighlightId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cover_image_url")
    private String coverImgUrl;
}

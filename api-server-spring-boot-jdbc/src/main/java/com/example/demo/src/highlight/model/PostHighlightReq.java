package com.example.demo.src.highlight.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostHighlightReq {

    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cover_image_url")
    private String coverImgUrl;
    @JsonProperty("story_id_list")
    private List<Integer> storyIdList;

}

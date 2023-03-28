package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostStoryRes {

    @JsonProperty("story_id")
    private int storyId;

}

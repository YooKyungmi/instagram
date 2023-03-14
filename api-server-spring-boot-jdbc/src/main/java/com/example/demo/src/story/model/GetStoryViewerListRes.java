package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStoryViewerListRes {

    @JsonProperty("story_id")
    private int storyId;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("story_viewer_list")
    private List<GetStoryViewerRes> getStoryViewerResList;

}

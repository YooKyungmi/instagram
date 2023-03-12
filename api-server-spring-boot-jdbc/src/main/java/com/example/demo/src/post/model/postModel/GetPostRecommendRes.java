package com.example.demo.src.post.model.postModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetPostRecommendRes {
    private int postId;
    private String firstPhotoUrl;
}

package com.example.demo.src.post.model.postModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetPostRes {
    private int postId;
    private int userId;
    private String content;
    private String place;
    private int likeShowStatus;
    private int commentShowStatus;
    private List<Photo> photos;
    private String createdAt;
    private String updatedAt;
    private int likeCount;
    private String profileName;
    private String profilePicture;
    private GetWhetherDTO scrapOn;
    private List<String> tagWord;
    private GetWhetherDTO likeOn;
    private int userStoryOn;
    private int userFollowOn;

}

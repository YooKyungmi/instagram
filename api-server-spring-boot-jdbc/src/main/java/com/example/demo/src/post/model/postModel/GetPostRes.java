package com.example.demo.src.post.model.postModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostRes {
    private int postId;
    private String content;
    private String place;
    private int likeShowStatus;
    private int commentShowStatus;
    private List<GetPostPhoto> photos;
    private String createdAt;
    private String updatedAt;
    private int likeCount;
    private String profileName;
    private String profilePicture;
    private int scrapOn;
    private List<String> tagWord;
    private int likeOn;

}

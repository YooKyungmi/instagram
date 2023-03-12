package com.example.demo.src.post.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentRes {
    private int commentId;
    private int postId;
    private int userId;
    private String profileName;
    private String profilePicture;
    private int groupId;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private int bigCommentCount;
    private int likeCount;
    private int likeOn;

}

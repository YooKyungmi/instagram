package com.example.demo.src.post.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentRes {
    private int commentId;
    private int postId;
    private int userId;
    private int groupId;
    private String comment;
    private int status;
    private String createdt;
    private String updatedAt;
}

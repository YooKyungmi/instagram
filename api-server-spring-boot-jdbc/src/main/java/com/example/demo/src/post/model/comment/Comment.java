package com.example.demo.src.post.model.comment;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private int groupId;
    private String comment;
    private int status;
    private String createdAt;
    private String updatedAt;

}

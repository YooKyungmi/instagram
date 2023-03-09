package com.example.demo.src.post.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @NotEmpty
    private int commentId;
    @NotEmpty
    private int postId;
    @NotEmpty
    private int userId;
    private int groupId;
    @NotEmpty
    private String comment;
    private int status;
    private String createdAt;
    private String updatedAt;

}

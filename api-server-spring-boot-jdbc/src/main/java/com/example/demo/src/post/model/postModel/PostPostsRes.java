package com.example.demo.src.post.model.postModel;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPostsRes {
@NotNull
    private int postId;
}

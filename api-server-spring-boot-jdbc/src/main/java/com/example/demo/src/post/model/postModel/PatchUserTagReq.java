package com.example.demo.src.post.model.postModel;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PatchUserTagReq {
    @NotNull(message ="postId는 필수 입력요소 입니다")
    private int postId;
    @NotNull
    private int userTagId;
    @NotEmpty
    @URL
    private String photoUrl;
}

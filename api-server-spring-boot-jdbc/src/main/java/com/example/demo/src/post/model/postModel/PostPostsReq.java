package com.example.demo.src.post.model.postModel;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPostsReq {
    private String content;
    private String place;
    @NotNull(message ="likeShowStatus는 필수 입력요소 입니다")
    private int likeShowStatus;
    @NotNull(message ="commentShowStatus는 필수 입력요소 입니다")
    private int commentShowStatus;
    private List<Photo> photos;
    private List<String> tagWord;
}

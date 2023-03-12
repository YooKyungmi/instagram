package com.example.demo.src.post.model.postModel;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostContentTagReq {
    @NotEmpty(message ="postId는 필수 입력요소 입니다")
    private int postId;
    private List<String> tagWord;
}

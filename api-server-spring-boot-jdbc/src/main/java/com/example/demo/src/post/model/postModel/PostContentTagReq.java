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
public class PostContentTagReq {
    @NotNull(message ="postId는 필수 입력요소 입니다")
    private int postId;
    private List<String> tagWord;
}

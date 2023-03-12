package com.example.demo.src.post.model.postModel;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchPlaceReq {
    @NotNull(message ="postId는 필수 입력요소 입니다")
    private int postId;
    private String place;
}

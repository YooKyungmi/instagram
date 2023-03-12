package com.example.demo.src.post.model.postModel;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchPlaceReq {
    @NotEmpty (message ="postId는 필수 입력요소 입니다")
    private int postId;
    private String place;
}

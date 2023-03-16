package com.example.demo.src.post.model.postModel;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletePhotoReq {
    @NotNull(message ="postId는 필수 입력요소 입니다")
    private int postId;
    @NotNull
    @Max(value = 10)
    @Positive
    private int photoIndex;
    @NotEmpty
    @URL
    private String photoUrl;
}

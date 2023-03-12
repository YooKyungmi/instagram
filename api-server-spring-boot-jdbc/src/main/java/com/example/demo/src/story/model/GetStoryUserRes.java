package com.example.demo.src.story.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStoryUserRes{

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    @JsonProperty("view_status")
    private int viewStatus;
    @JsonProperty("updated_at")
    private String updatedAt;

}

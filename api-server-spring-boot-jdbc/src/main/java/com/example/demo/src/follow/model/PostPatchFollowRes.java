package com.example.demo.src.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPatchFollowRes {

    @JsonProperty("user_follow_id")
    int userFollowId;
    int status;
}

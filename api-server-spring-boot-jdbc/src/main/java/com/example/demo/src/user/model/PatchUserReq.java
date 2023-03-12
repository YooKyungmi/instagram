package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchUserReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("introduce")
    private String introduce;
    @JsonProperty("gender")
    private String gender;

}

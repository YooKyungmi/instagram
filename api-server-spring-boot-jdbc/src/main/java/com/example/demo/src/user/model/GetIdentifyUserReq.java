package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetIdentifyUserReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("password")
    private String password;

}

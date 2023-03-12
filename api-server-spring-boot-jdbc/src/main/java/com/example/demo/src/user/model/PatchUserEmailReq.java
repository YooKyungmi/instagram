package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchUserEmailReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("email_address")
    private String emailAddress;

}

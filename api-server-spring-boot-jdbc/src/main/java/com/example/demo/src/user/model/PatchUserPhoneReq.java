package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchUserPhoneReq {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("phone_number")
    private String phoneNumber;

}

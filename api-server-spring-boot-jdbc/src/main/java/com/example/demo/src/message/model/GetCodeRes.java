package com.example.demo.src.message.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCodeRes {

    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("auth_code")
    private String authCode;

}

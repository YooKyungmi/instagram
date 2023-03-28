package com.example.demo.src.highlight.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostHighlightRes {

    @JsonProperty("user_highlight_id")
    private int userHighlightId;

}

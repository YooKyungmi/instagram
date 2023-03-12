package com.example.demo.src.post.model.postModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private String photoUrl;
    private List<String> userTagId;
}

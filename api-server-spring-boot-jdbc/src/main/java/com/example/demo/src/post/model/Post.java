package com.example.demo.src.post.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @NotEmpty
    private int postId;
    @NotEmpty
    private int userId;
    private String content;
    private String place;
   @NotEmpty
    private int commentShowStatus;
    private String photo1;
    private String photo2="none";
    private String photo3="none";
    private String photo4="none";
    private String photo5="none";
    private String photo6="none";
    private String photo7="none";
    private String photo8="none";
    private String photo9="none";
    private String photo10;
    private String createdAt;
    private String updatedAt;


}

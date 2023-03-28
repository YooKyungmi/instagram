package com.example.demo.src.story.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory {

    private int userStoryId;
    private int userId;
    private int storyUrl;
    private int status;
    private String createdAt;
    private String updatedAt;

}

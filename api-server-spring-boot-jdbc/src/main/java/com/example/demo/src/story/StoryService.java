package com.example.demo.src.story;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoryDao storyDao;
    private final StoryProvider storyProvider;
    private final JwtService jwtService;

    @Autowired
    public StoryService(StoryDao storyDao, StoryProvider storyProvider, JwtService jwtService) {
        this.storyDao = storyDao;
        this.storyProvider = storyProvider;
        this.jwtService = jwtService;
    }
}

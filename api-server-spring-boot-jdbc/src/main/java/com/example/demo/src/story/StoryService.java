package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

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

    public void patchStory(int storyId) throws BaseException {
        try{
            if(storyDao.checkStoryId(storyId)==0){
                throw new BaseException(GET_STORIES_STORY_ID_NOT_EXISTS);
            }
            int result = storyDao.patchStory(0,storyId);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_STORY);
            }
        }catch (Exception exception) {
            logger.error("App - patchStory Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

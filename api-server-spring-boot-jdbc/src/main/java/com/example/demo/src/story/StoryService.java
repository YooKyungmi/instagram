package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.src.highlight.HighlightDao;
import com.example.demo.src.highlight.HighlightProvider;
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
    private final HighlightProvider highlightProvider;
    private final HighlightDao highlightDao;

    @Autowired
    public StoryService(StoryDao storyDao, StoryProvider storyProvider, JwtService jwtService, HighlightProvider highlightProvider, HighlightDao highlightDao) {
        this.storyDao = storyDao;
        this.storyProvider = storyProvider;
        this.jwtService = jwtService;
        this.highlightProvider = highlightProvider;
        this.highlightDao = highlightDao;
    }

    public void patchStory(int storyId) throws BaseException {
        if(storyProvider.checkStoryId(storyId)==0){
            throw new BaseException(GET_STORIES_STORY_ID_NOT_EXISTS);
        }
        try{
            int result = storyDao.patchStory(0,storyId);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_STORY);
            }
        }catch (Exception exception) {
            logger.error("App - patchStory Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchStoryLikeStatus(int userId, int storyId, int likeStatus) throws BaseException {
        if (storyDao.checkStoryId(storyId) == 0) {
            throw new BaseException(GET_STORIES_STORY_ID_NOT_EXISTS);
        }
        if (storyDao.checkStoryViewer(storyId, userId) == 0) {
            throw new BaseException(GET_STORIES_STORY_VIEWER_NOT_EXISTS);
        }
        try{
            int result = storyDao.patchStoryLikeStatus(userId,storyId,likeStatus);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_STORY_VIEWER_LIKE);
            }
        }catch (Exception exception) {
            logger.error("App - patchStory Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void addStoryInHighlight(int storyId, int highlightId) throws BaseException {
        if (storyDao.checkStoryId(storyId) == 0) {
            throw new BaseException(GET_STORIES_STORY_ID_NOT_EXISTS);
        }
        if(highlightProvider.checkHighlightByHighlightId(highlightId)==0){
            throw new BaseException(GET_HIGHLIGHTS_INVALID_HIGHLIGHT_ID);
        }
        if(highlightProvider.checkStoryIdInHighlightId(storyId,highlightId)==1){
            throw new BaseException(PATCH_STORIES_ALREADY_IN_HIGHLIGHT);
        }
        try{
            int result = highlightDao.createHighlight(highlightId,storyId);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_STORY_HIGHLIGHT);
            }
        }catch (Exception exception) {
            logger.error("App - addStoryInHighlight Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

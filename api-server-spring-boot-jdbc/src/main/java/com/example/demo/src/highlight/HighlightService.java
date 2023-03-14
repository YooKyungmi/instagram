package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.src.highlight.model.PostHighlightReq;
import com.example.demo.src.highlight.model.PostHighlightRes;
import com.example.demo.src.story.StoryDao;
import com.example.demo.src.story.StoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class HighlightService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HighlightProvider highlightProvider;

    private final HighlightDao highlightDao;

    private final StoryDao storyDao;

    private final StoryProvider storyProvider;

    @Autowired
    public HighlightService(HighlightProvider highlightProvider, HighlightDao highlightDao, StoryDao storyDao, StoryProvider storyProvider) {
        this.highlightProvider = highlightProvider;
        this.highlightDao = highlightDao;
        this.storyDao = storyDao;
        this.storyProvider = storyProvider;
    }


    public PostHighlightRes createHighlight(PostHighlightReq postHighlightReq) throws BaseException {
        try {
            int userHighlightId = highlightDao.createUserHighlight(postHighlightReq);
            postHighlightReq.getStoryIdList().forEach(id -> {
                highlightDao.createHighlight(userHighlightId, id);
            });
            return new PostHighlightRes(userHighlightId);
        } catch (Exception exception) {
            logger.error("App - createHighlight Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteStoryFromHighlight(int storyId) throws BaseException {
        try {
            List<Integer> highlightIdList = highlightDao.getHighlightIdByStoryId(storyId);
            for(int userHighlightId : highlightIdList){
                if(highlightDao.countStoryFromHighlight(userHighlightId)<=1){
                    int r = highlightDao.deleteHighlight(userHighlightId);
                    if(r==0){
                        throw new BaseException(MODIFY_FAIL_HIGHLIGHT);
                    }
                }
            }
            int result = highlightDao.deleteStoryFromHighlight(storyId);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_STORY);
            }
        } catch (Exception exception) {
            logger.error("App - createHighlight Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

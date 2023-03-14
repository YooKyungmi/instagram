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
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public void patchHighlight(int highlightId) throws BaseException {
        if(highlightProvider.checkHighlightByHighlightId(highlightId)==0){
            throw new BaseException(GET_HIGHLIGHTS_INVALID_HIGHLIGHT_ID);
        }
        try{
            int result = highlightDao.deleteHighlight(highlightId);
            if(result ==0){
                throw new BaseException(MODIFY_FAIL_HIGHLIGHT);
            }
            result = highlightDao.deleteStoriesInDeletedHighlight(highlightId);
            if(result ==0){
                throw new BaseException(MODIFY_FAIL_HIGHLIGHT);
            }
        }catch (Exception exception) {
            logger.error("App - patchHighlight Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchHighlightInfo(int highlightId, PostHighlightReq postHighlightReq) throws BaseException {
        for (int storyId : postHighlightReq.getStoryIdList()) {
            if (storyDao.checkStoryIdExists(storyId) == 0) {
                throw new BaseException(GET_STORIES_STORY_ID_NOT_EXISTS);
            }
        }
        try{
            int result = highlightDao.patchHighlightInfo(highlightId,postHighlightReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_HIGHLIGHT);
            }
            result = patchHighlightStory(postHighlightReq.getStoryIdList(),highlightDao.getAllStoryIdByHighlightId(highlightId),highlightId);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_HIGHLIGHT);
            }
        }catch (Exception exception) {
            logger.error("App - patchHighlightInfo Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private int patchHighlightStory(List<Integer> storyIdList, List<Integer> originStoryIdList,int highlightId) {
        List<Integer>addIdList = storyIdList.stream().filter(id->originStoryIdList.stream().noneMatch(Predicate.isEqual(id))).collect(Collectors.toList());
        List<Integer>deleteIdList = originStoryIdList.stream().filter(id -> storyIdList.stream().noneMatch(Predicate.isEqual(id))).collect(Collectors.toList());
        int result = 0;
        for(Integer id : deleteIdList){
            result = highlightDao.deleteStoryFromHighlight(id);
        }
        for(Integer id : addIdList){
            result = highlightDao.createHighlight(highlightId,id);
        }
        return result;
    }


}

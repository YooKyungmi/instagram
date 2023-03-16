package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.highlight.HighlightProvider;
import com.example.demo.src.highlight.HighlightService;
import com.example.demo.src.story.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexImageUrl;

@RestController
@RequestMapping("/app/stories")
public class StoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoryProvider storyProvider;
    @Autowired
    private final StoryService storyService;
    @Autowired
    private final HighlightService highlightService;
    @Autowired
    private final HighlightProvider highlightProvider;
    @Autowired
    private final JwtService jwtService;
    //@Autowired
    //private final FileUploadService fileUploadService;

    public StoryController(StoryProvider storyProvider, StoryService storyService, HighlightService highlightService, HighlightProvider highlightProvider, JwtService jwtService) {
        this.storyProvider = storyProvider;
        this.storyService = storyService;
        this.highlightService = highlightService;
        this.highlightProvider = highlightProvider;
        this.jwtService = jwtService;
    }

    /**
     * 스토리 목록 조회 API
     * [GET] /app/stories/followings
     *
     * @return BaseResponse<List<GetStoryUserRes>>
     */
    @GetMapping("/followings")
    public BaseResponse<List<GetStoryUserRes>> getStoryUsers() {
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetStoryUserRes> getStoryUserResList = storyProvider.getStoryUsers(userIdByJwt);
            return new BaseResponse<>(getStoryUserResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저의 스토리 전체 조회 API
     * [GET] /app/stories?user-id=
     *
     * @return BaseResponse<List<GetStoryRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetStoryRes>> getStoryByUserId(@RequestParam("user-id")Integer userId) {
        if(userId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_USER_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetStoryRes> getStoryResList = storyProvider.getStoryByUserId(userIdByJwt,userId);
            return new BaseResponse<>(getStoryResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 스토리 조회 API
     * [GET] /app/stories/:story-id
     *
     * @return BaseResponse<GetStoryRes>
     */
    @GetMapping("/{story-id}")
    public BaseResponse<GetStoryRes> getStoryByStoryId(@PathVariable("story-id")Integer storyId) {
        if(storyId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_STORY_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            GetStoryRes getStoryRes = storyProvider.getStoryByStoryId(userIdByJwt,storyId);
            return new BaseResponse<>(getStoryRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 삭제 API
     * [PATCH] /app/stories/:story-id
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{story-id}")
    public BaseResponse<String> patchStory(@PathVariable("story-id") Integer storyId){
        if(storyId==null){
            return new BaseResponse<>(PATCH_STORIES_EMPTY_STORY_ID);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != storyProvider.getStoryUserByStoryId(storyId)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(highlightProvider.checkStoryIdInHighlight(storyId)==1) {
                highlightService.deleteStoryFromHighlight(storyId);
            }
            storyService.patchStory(storyId);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 조회한 사람 목록 조회 API
     * [GET] /app/stories/story-viewers?story-id=
     *
     * @return BaseResponse<GetStoryViewerListRes>
     */
    @GetMapping("/story-viewers")
    public BaseResponse<GetStoryViewerListRes> getStoryViewers(@RequestParam("story-id")Integer storyId) {
        if(storyId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_STORY_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != storyProvider.getStoryUserByStoryId(storyId)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetStoryViewerListRes getStoryViewerListRes = storyProvider.getStoryViewers(storyId);
            return new BaseResponse<>(getStoryViewerListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 조회한 스토리 좋아요 수정 API
     * [PATCH] /app/stories/likes/:story-id?user-id= & like-status =
     *
     * @return BaseResponse<String>
     */
    @PatchMapping("/likes/{story-id}")
    public BaseResponse<String> patchFollows(@PathVariable("story-id") Integer storyId, @RequestParam("user-id")Integer userId, @RequestParam("like-status")Integer likeStatus) {
        if(storyId==null){
            return new BaseResponse<>(PATCH_STORIES_EMPTY_STORY_ID);
        }
        if(userId==null){
            return new BaseResponse<>(PATCH_STORIES_EMPTY_USER_ID);
        }
        if(likeStatus==null){
            return new BaseResponse<>(PATCH_STORIES_EMPTY_LIKE_STATUS);
        }
        if(likeStatus!=1&&likeStatus!=0){
            return new BaseResponse<>(PATCH_STORIES_INVALID_LIKE_STATUS);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            storyService.patchStoryLikeStatus(userId, storyId,likeStatus);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저의 스토리 전체 조회 API
     * [GET] /app/stories/histories?user-id=
     *
     * @return BaseResponse<List<GetStoryHistoryRes>>
     */
    @GetMapping("/histories")
    public BaseResponse<List<GetStoryHistoryRes>> getAllStories(@RequestParam("user-id")Integer userId) {
        if(userId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_USER_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetStoryHistoryRes> getStoryHistoryResList = storyProvider.getAllStories(userId);
            return new BaseResponse<>(getStoryHistoryResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 하이라이트에 스토리 추가 API
     * [PATCH] /app/stories/:story-id?highlight-id=
     *
     * @return BaseResponse<String>
     */
    @PatchMapping("/{story-id}/highlights/{highlight-id}")
    public BaseResponse<String> addStoryInHighlight(@PathVariable("story-id") Integer storyId, @PathVariable("highlight-id")Integer highlightId) {
        if(storyId==null){
            return new BaseResponse<>(PATCH_STORIES_EMPTY_STORY_ID);
        }
        if(highlightId==null){
            return new BaseResponse<>(PATCH_HIGHLIGHTS_EMPTY_HIGHLIGHT_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=storyProvider.getStoryUserByStoryId(storyId)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            storyService.addStoryInHighlight(storyId,highlightId);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 추가 API
     * [PATCH] /app/stories
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostStoryRes> postStory(@RequestBody PostStoryReq postStoryReq){
        if(postStoryReq.getStoryUrl()==null){
            return new BaseResponse<>(POST_STORIES_EMPTY_STORY_URL);
        }
        if(!isRegexImageUrl(postStoryReq.getStoryUrl())) {
            return new BaseResponse<>(POST_STORIES_INVALID_STORY_URL);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            postStoryReq.setUserId(userIdByJwt);
            PostStoryRes postStoryRes = storyService.postStory(postStoryReq);
            return new BaseResponse<>(postStoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //s3
    /**
     * 스토리 추가 API
     * [PATCH] /app/stories
     *
     * @return BaseResponse<String>
     *//*
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostStoryRes> postStory(@RequestPart("image") MultipartFile multipartFile){
        try {
            int userIdByJwt = jwtService.getUserId();
            PostStoryReq postStoryReq = PostStoryReq.builder()
                    .userId(userIdByJwt)
                    .storyUrl(fileUploadService.uploadImage(multipartFile));
            PostStoryRes postStoryRes = storyService.postStory(postStoryReq);
            return new BaseResponse<>(postStoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }*/



}

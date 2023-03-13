package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.story.model.GetStoryRes;
import com.example.demo.src.story.model.GetStoryUserRes;
import com.example.demo.src.story.model.GetStoryViewerListRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/stories")
public class StoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoryProvider storyProvider;
    @Autowired
    private final StoryService storyService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public StoryController(StoryProvider storyProvider, StoryService storyService, JwtService jwtService, UserProvider userProvider) {
        this.storyProvider = storyProvider;
        this.storyService = storyService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
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

}

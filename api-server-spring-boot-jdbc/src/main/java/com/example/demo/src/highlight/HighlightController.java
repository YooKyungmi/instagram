package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.highlight.model.GetHighlightByHighlightIdRes;
import com.example.demo.src.highlight.model.GetHighlightByUserIdRes;
import com.example.demo.src.highlight.model.PostHighlightReq;
import com.example.demo.src.highlight.model.PostHighlightRes;
import com.example.demo.src.story.StoryProvider;
import com.example.demo.src.story.StoryService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/highlights")
public class HighlightController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HighlightProvider highlightProvider;
    @Autowired
    private final HighlightService highlightService;
    @Autowired
    private final StoryProvider storyProvider;
    @Autowired
    private final StoryService storyService;
    @Autowired
    private final JwtService jwtService;

    private final String defaultTitle = "하이라이트";

    private final String defaultCoverImgUrl = "https://trudylin.s3.ap-northeast-2.amazonaws.com/postPhoto/profile+Image.png";


    public HighlightController(HighlightProvider highlightProvider, HighlightService highlightService, StoryProvider storyProvider, StoryService storyService, JwtService jwtService) {
        this.highlightProvider = highlightProvider;
        this.highlightService = highlightService;
        this.storyProvider = storyProvider;
        this.storyService = storyService;
        this.jwtService = jwtService;
    }

    /**
     * 하이라이트 생성 API
     * [POST] /app/highlights
     *
     * @return BaseResponse<PostHighlightRes>
     */
    @PostMapping("")
    public BaseResponse<PostHighlightRes> postHighlights(@RequestBody PostHighlightReq postHighlightReq) {
        try {
            if (postHighlightReq.getTitle() == null) {
                postHighlightReq.setTitle(defaultTitle);
            }
            if (postHighlightReq.getCoverImgUrl() == null) {
                postHighlightReq.setCoverImgUrl(defaultCoverImgUrl);
            }
            if (postHighlightReq.getStoryIdList() == null || postHighlightReq.getStoryIdList().size() == 0) {
                return new BaseResponse<>(POST_HIGHLIGHTS_EMPTY_STORY_ID_LIST);
            }
            int userIdByJwt = jwtService.getUserId();
            postHighlightReq.setUserId(userIdByJwt);
            for (Integer storyId : postHighlightReq.getStoryIdList()) {
                if (storyProvider.checkStoryIdExists(storyId) == 0) {
                    return new BaseResponse<>(POST_HIGHLIGHTS_INVALID_STORY_ID);
                }
                if (storyProvider.getStoryUserByStoryId(storyId) != postHighlightReq.getUserId()) {
                    return new BaseResponse<>(POST_HIGHLIGHTS_INVALID_STORY_JWT);
                }
            }
            PostHighlightRes postHighlightRes = highlightService.createHighlight(postHighlightReq);
            return new BaseResponse<>(postHighlightRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스토리 목록 조회 API
     * [GET] /app/highlights?user-id=
     *
     * @return BaseResponse<List<GetHighlightByUserIdRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetHighlightByUserIdRes>> getStoryUsers(@RequestParam("user-id")Integer userId) {
        if(userId==null){
            return new BaseResponse<>(GET_HIGHLIGHTS_EMPTY_USER_ID);
        }
        try{
            List<GetHighlightByUserIdRes> getHighlightByUserIdResList = highlightProvider.getHighlightsByUserId(userId);
            return new BaseResponse<>(getHighlightByUserIdResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 하이라이트 내 스토리 전체 조회 API
     * [GET] /app/highlights/:highlight-id
     *
     * @return BaseResponse<List<GetHighlightByHighlightIdRes>>
     */
    @GetMapping("/{highlight-id}")
    public BaseResponse<List<GetHighlightByHighlightIdRes>> getHighlightByHighlightId(@PathVariable("highlight-id")Integer highlightId) {
        if(highlightId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_HIGHLIGHT_ID);
        }
        try{
            if(highlightProvider.checkHighlightByHighlightId(highlightId)==0) {
                return new BaseResponse<>(GET_HIGHLIGHTS_INVALID_HIGHLIGHT_ID);
            }
            List<GetHighlightByHighlightIdRes> getHighlightByHighlightIdResList = highlightProvider.getAllStoriesByHighlightId(highlightId);
            return new BaseResponse<>(getHighlightByHighlightIdResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 하이라이트 삭제 API
     * [PATCH] app/highlights/:highlight-id
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{highlight-id}")
    public BaseResponse<String> patchHighlight(@PathVariable(value = "highlight-id") Integer highlightId){
        if(highlightId==null){
            return new BaseResponse<>(PATCH_HIGHLIGHTS_EMPTY_HIGHLIGHT_ID);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt != highlightProvider.getHighlightUserByHighlightId(highlightId)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            highlightService.patchHighlight(highlightId);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 하이라이트 수정 API
     * [PATCH] /app/highlights/infos/:highlight-id
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/infos/{highlight-id}")
    public BaseResponse<String> patchHighlightInfo(@PathVariable(value = "highlight-id") Integer highlightId,@RequestBody PostHighlightReq postHighlightReq){
        if(highlightId==null){
            return new BaseResponse<>(PATCH_HIGHLIGHTS_EMPTY_HIGHLIGHT_ID);
        }
        if (postHighlightReq.getTitle() == null) {
            postHighlightReq.setTitle(defaultTitle);
        }
        if (postHighlightReq.getCoverImgUrl() == null) {
            postHighlightReq.setCoverImgUrl(defaultCoverImgUrl);
        }
        try {
            int userIdByJwt = jwtService.getUserId();
            if(highlightProvider.checkHighlightByHighlightId(highlightId)==0){
                return new BaseResponse<>(GET_HIGHLIGHTS_INVALID_HIGHLIGHT_ID);
            }
            if(userIdByJwt != highlightProvider.getHighlightUserByHighlightId(highlightId)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postHighlightReq.getStoryIdList() == null || postHighlightReq.getStoryIdList().size() == 0) {
                highlightService.patchHighlight(highlightId);
            }else {
                postHighlightReq.setUserId(userIdByJwt);
                highlightService.patchHighlightInfo(highlightId, postHighlightReq);
            }
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

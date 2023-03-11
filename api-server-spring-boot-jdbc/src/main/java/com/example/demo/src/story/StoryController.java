package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.story.model.GetStoryUserListRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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
     * [GET] /app/stories?user-id=
     *
     * @return BaseResponse<GetStoryUserListRes>
     */
    @GetMapping("")
    public BaseResponse<GetStoryUserListRes> getStoryUsers(@RequestParam("user-id") Integer userId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetStoryUserListRes getStoryUserResList = storyProvider.getStoryUsers(userIdByJwt);
            return new BaseResponse<>(getStoryUserResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

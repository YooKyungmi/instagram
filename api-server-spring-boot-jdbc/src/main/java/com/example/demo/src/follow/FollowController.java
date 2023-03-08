package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/follows")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService, UserProvider userProvider) {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 팔로워 조회 API
     * [GET] /app/follows?user-id=
     *
     * @return BaseResponse<List<GetFollowerRes>>
     */
    @GetMapping("/followers")
    public BaseResponse<GetFollowerRes> getFollowers(@RequestParam("user-id") Integer userId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            GetFollowerRes getFollowerRes = followProvider.getFollowers(userIdByJwt, userId);
            return new BaseResponse<>(getFollowerRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

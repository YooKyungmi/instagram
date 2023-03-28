package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.GetConnectedFollowRes;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.follow.model.GetFollowingRes;
import com.example.demo.src.follow.model.PostPatchFollowRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_FOLLOW_USER_ID;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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
     * 팔로우 추가 API
     * [POST] /app/follows/:user-id?follow-user-id=
     *
     * @return BaseResponse<PostFollowsRes>
     */
    @PostMapping("/{user-id}")
    public BaseResponse<PostPatchFollowRes> createFollows(@PathVariable("user-id") Integer userId, @RequestParam("follow-user-id")Integer followUserId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(userId==followUserId){
                return new BaseResponse<>(INVALID_FOLLOW_USER_ID);
            }
            PostPatchFollowRes postPatchFollowRes = followService.addFollows(userId, followUserId);
            return new BaseResponse<>(postPatchFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로우 삭제 API
     * [PATCH] /app/follows/:user-id?follow-user-id=
     *
     * @return BaseResponse<PostFollowsRes>
     */
    @PatchMapping("/{user-id}")
    public BaseResponse<PostPatchFollowRes> patchFollows(@PathVariable("user-id") Integer userId, @RequestParam("follow-user-id")Integer followUserId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            if(userIdByJwt!=userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(userId==followUserId){
                return new BaseResponse<>(INVALID_FOLLOW_USER_ID);
            }
            PostPatchFollowRes postPatchFollowRes = followService.patchFollows(userId, followUserId);
            return new BaseResponse<>(postPatchFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로워 조회 API
     * [GET] /app/follows/followers?user-id=
     *
     * @return BaseResponse<GetFollowerRes>
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

    /**
     * 팔로잉 조회 API
     * [GET] /app/follows/followings?user-id=
     *
     * @return BaseResponse<GetFollowingRes>
     */
    @GetMapping("/followings")
    public BaseResponse<GetFollowingRes> getFollowings(@RequestParam("user-id") Integer userId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            GetFollowingRes getFollowingRes = followProvider.getFollowings(userIdByJwt, userId);
            return new BaseResponse<>(getFollowingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 함께 아는 친구 조회 API
     * [GET] /app/follows/connected-follows?user-id=
     *
     * @return BaseResponse<GetConnectedFriendRes>
     */
    @GetMapping("/connected-follows")
    public BaseResponse<GetConnectedFollowRes> getConnectedFollows(@RequestParam("user-id") Integer userId) {
        try{
            int userIdByJwt = jwtService.getUserId();
            GetConnectedFollowRes getConnectedFollowRes = followProvider.getConnectedFollows(userIdByJwt, userId);
            return new BaseResponse<>(getConnectedFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}

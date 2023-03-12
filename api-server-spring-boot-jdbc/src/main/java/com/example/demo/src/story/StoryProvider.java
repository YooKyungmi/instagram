package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.FollowProvider;
import com.example.demo.src.story.model.GetStoryUserListRes;
import com.example.demo.src.story.model.GetStoryUserRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.GET_USERS_INVALID_USER_ID;

@Service
public class StoryProvider {
    private final StoryDao storyDao;
    private final JwtService jwtService;
    private final UserProvider userProvider;
    private final FollowProvider followProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoryProvider(StoryDao storyDao, JwtService jwtService, UserProvider userProvider, FollowProvider followProvider) {
        this.storyDao = storyDao;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
        this.followProvider = followProvider;
    }

    public GetStoryUserListRes getStoryUsers(int userIdByJwt) throws BaseException {
        throwIfInvalidUserIdDetected(userIdByJwt);
        try {
            GetStoryUserListRes getStoryUserListRes = new GetStoryUserListRes();
            List<GetStoryUserRes> getNotViewedStoryUserResList= new ArrayList<>();
            List<GetStoryUserRes> getViewedStoryUserResList= new ArrayList<>();
            if(storyDao.checkStory(userIdByJwt)==1) {
                if (storyDao.checkStoryNotViewedExist(userIdByJwt, userIdByJwt) == 0) {
                    getStoryUserListRes.setOnlineStoryUser(storyDao.getStoryUsersInfo(userIdByJwt, 0));

                } else {
                    getStoryUserListRes.setOnlineStoryUser(storyDao.getStoryUsersInfo(userIdByJwt, 1));
                }
            }
            List<Integer> followingIdList = followProvider.getFollowingIdList(userIdByJwt);
            followingIdList.forEach(id -> {
                if(storyDao.checkStory(id)==1) {
                    if (storyDao.checkStoryNotViewedExist(id, userIdByJwt) == 1) {
                        getNotViewedStoryUserResList.add(storyDao.getStoryUsersInfo(id, 0));

                    } else {
                        getViewedStoryUserResList.add(storyDao.getStoryUsersInfo(id, 1));
                    }
                }
            });
            getStoryUserListRes.setNotViewedStoryUserList(getNotViewedStoryUserResList);
            getStoryUserListRes.setViewedStoryUserList(getViewedStoryUserResList);
            return getStoryUserListRes;
        } catch (Exception exception) {
            logger.error("App - getStoryUsers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }

}

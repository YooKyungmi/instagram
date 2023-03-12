package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.FollowProvider;
import com.example.demo.src.story.model.GetStoryUserRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<GetStoryUserRes> getStoryUsers(int userIdByJwt) throws BaseException {
        throwIfInvalidUserIdDetected(userIdByJwt);
        try {
            List<GetStoryUserRes> getStoryUserResList = new ArrayList<>();
            List<StoryUser> getNotViewedStoryUserResList = new ArrayList<>();
            List<StoryUser> getViewedStoryUserResList = new ArrayList<>();
            if (storyDao.checkStory(userIdByJwt) == 1) {
                if (storyDao.checkStoryNotViewedExist(userIdByJwt, userIdByJwt) == 0) {
                    getStoryUserResList.add(buildGetStoryUserRes(storyDao.getStoryUsersInfo(userIdByJwt, 1, 0)));

                } else {
                    getStoryUserResList.add(buildGetStoryUserRes(storyDao.getStoryUsersInfo(userIdByJwt, 1, 1)));
                }
            }
            List<Integer> followingIdList = followProvider.getFollowingIdList(userIdByJwt);
            followingIdList.forEach(id -> {
                if (storyDao.checkStory(id) == 1) {
                    if (storyDao.checkStoryNotViewedExist(id, userIdByJwt) == 1) {
                        getNotViewedStoryUserResList.add(storyDao.getStoryUsersInfo(id, 0, 0));

                    } else {
                        getViewedStoryUserResList.add(storyDao.getStoryUsersInfo(id, 0, 1));
                    }
                }
            });
            Collections.sort(getNotViewedStoryUserResList);
            Collections.sort(getViewedStoryUserResList);
            getNotViewedStoryUserResList.forEach(res -> getStoryUserResList.add(buildGetStoryUserRes(res)));
            getViewedStoryUserResList.forEach(res -> getStoryUserResList.add(buildGetStoryUserRes(res)));
            return getStoryUserResList;
        } catch (Exception exception) {
            logger.error("App - getStoryUsers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetStoryUserRes buildGetStoryUserRes(StoryUser storyUser) {
        return GetStoryUserRes.builder()
                .userId(storyUser.getUserId())
                .nickname(storyUser.getNickname())
                .profileImageUrl(storyUser.getProfileImageUrl())
                .selfStatus(storyUser.getSelfStatus())
                .viewStatus(storyUser.getViewStatus())
                .updatedAt(storyUser.getUpdatedAt())
                .build();
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoryUser implements Comparable {

        private int userId;
        private String nickname;
        private String profileImageUrl;
        private int selfStatus;
        private int viewStatus;
        private String updatedAt;

        @Override
        public int compareTo(@NotNull Object object) {
            StoryUser otherStoryUser = (StoryUser) object;
            if (this.updatedAt.compareTo(otherStoryUser.updatedAt) < 0) {
                return 1;
            } else if (this.updatedAt.compareTo(otherStoryUser.updatedAt) > 0) {
                return -1;
            }else{
                return 0;
            }
        }
    }
}


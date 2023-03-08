package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.GetFollowerInfoRes;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.follow.model.GetFollowingInfoRes;
import com.example.demo.src.follow.model.GetFollowingRes;
import com.example.demo.src.story.StoryDao;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.GET_USERS_INVALID_USER_ID;

@Service
public class FollowProvider {

    private final FollowDao followDao;

    private final UserDao userDao;

    private final StoryDao storyDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FollowProvider(FollowDao followDao, UserDao userDao, StoryDao storyDao) {
        this.followDao = followDao;
        this.userDao = userDao;
        this.storyDao = storyDao;
    }

    public int getFollowerCount(int userId) throws BaseException {
        try {
            return followDao.getFollowerCount(userId);
        } catch (Exception exception) {
            logger.error("App - getFollowerCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getFollowingCount(int userId) throws BaseException {
        try {
            return followDao.getFollowingCount(userId);
        } catch (Exception exception) {
            logger.error("App - getFollowingCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getConnectedFriendCount(int onlineUserId, int findingUserId) throws BaseException {
        try {
            return followDao.getConnectedFriendCount(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriendCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Integer> getConnectedFollowId(int onlineUserId, int findingUserId) throws BaseException {
        try {
            return followDao.getConnectedFollowId(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriendId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetFollowerRes getFollowers(int onlineUserId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            GetFollowerRes getFollowerRes = GetFollowerRes.builder()
                    .followerCount(followDao.getFollowerCount(userId))
                    .followingCount(followDao.getFollowingCount(userId))
                    .connectedCount(followDao.getConnectedFriendCount(onlineUserId,userId))
                    .build();
            List<Integer> followerIdList = followDao.getFollowers(userId);
            List<GetFollowerInfoRes> getFollowerInfoResList = new ArrayList<>();
            followerIdList.stream().forEach(id -> {
                try {
                    getFollowerInfoResList.add(buildGetFollowerRes(onlineUserId, id));
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            getFollowerRes.setGetFollowerInfoResList(getFollowerInfoResList);
            return getFollowerRes;
        } catch (Exception exception) {
            logger.error("App - getFollowers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetFollowerInfoRes buildGetFollowerRes(int onlineUserId, int id) throws BaseException {
        try {
            User user = userDao.getUser(id);
            return GetFollowerInfoRes.builder()
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .followStatus(followDao.checkFollowing(onlineUserId, id))
                    .storyStatus(storyDao.checkStory(id))
                    .build();
        }catch (Exception exception) {
            logger.error("App - getFollowers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetFollowingRes getFollowings(int onlineUserId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        try {
            GetFollowingRes getFollowingRes = GetFollowingRes.builder()
                    .followerCount(followDao.getFollowerCount(userId))
                    .followingCount(followDao.getFollowingCount(userId))
                    .connectedCount(followDao.getConnectedFriendCount(onlineUserId,userId))
                    .build();
            List<Integer> followingIdList = followDao.getFollowings(userId);
            List<GetFollowingInfoRes> getFollowingInfoResList = new ArrayList<>();
            followingIdList.stream().forEach(id -> {
                try {
                    getFollowingInfoResList.add(buildGetFollowingRes(onlineUserId, id));
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            getFollowingRes.setGetFollowingInfoResList(getFollowingInfoResList);
            return getFollowingRes;
        } catch (Exception exception) {
            logger.error("App - getFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetFollowingInfoRes buildGetFollowingRes(int onlineUserId, int id) throws BaseException {
        try {
            User user = userDao.getUser(id);
            return GetFollowingInfoRes.builder()
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .followStatus(followDao.checkFollowing(onlineUserId, id))
                    .storyStatus(storyDao.checkStory(id))
                    .build();
        }catch (Exception exception) {
            logger.error("App - getFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if(userDao.checkUserId(userId)==0){
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }
}

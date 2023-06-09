package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.GetConnectedFollowRes;
import com.example.demo.src.follow.model.GetFollowUserInfoRes;
import com.example.demo.src.follow.model.GetFollowerRes;
import com.example.demo.src.follow.model.GetFollowingRes;
import com.example.demo.src.story.StoryDao;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
            return followDao.getConnectedFollows(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriendId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetFollowerRes getFollowers(int onlineUserId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserStatus(userDao.getUser(userId));
        try {
            GetFollowerRes getFollowerRes = GetFollowerRes.builder()
                    .followerCount(followDao.getFollowerCount(userId))
                    .followingCount(followDao.getFollowingCount(userId))
                    .build();
            if (onlineUserId != userId) {
                int connectedFriendCount = followDao.getConnectedFriendCount(onlineUserId, userId);
                if (connectedFriendCount != 0) {
                    getFollowerRes.setConnectedCount(connectedFriendCount);
                }
            }
            List<Integer> followerIdList = followDao.getFollowers(userId);
            List<GetFollowUserInfoRes> getFollowerInfoResList = new ArrayList<>();
            followerIdList.stream().forEach(id -> {
                try {
                    getFollowerInfoResList.add(buildGetFollowUserRes(onlineUserId, id));
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            getFollowerRes.setGetFollowUserInfoResList(getFollowerInfoResList);
            return getFollowerRes;
        } catch (Exception exception) {
            logger.error("App - getFollowers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetFollowingRes getFollowings(int onlineUserId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserStatus(userDao.getUser(userId));
        try {
            GetFollowingRes getFollowingRes = GetFollowingRes.builder()
                    .followerCount(followDao.getFollowerCount(userId))
                    .followingCount(followDao.getFollowingCount(userId))
                    .build();
            if (onlineUserId != userId) {
                int connectedFriendCount = followDao.getConnectedFriendCount(onlineUserId, userId);
                if (connectedFriendCount != 0) {
                    getFollowingRes.setConnectedCount(connectedFriendCount);
                }
            }
            List<Integer> followingIdList = followDao.getFollowings(userId);
            List<GetFollowUserInfoRes> getFollowUserInfoResList = new ArrayList<>();
            followingIdList.stream().forEach(id -> {
                try {
                    getFollowUserInfoResList.add(buildGetFollowUserRes(onlineUserId, id));
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            getFollowingRes.setGetFollowUserInfoResList(getFollowUserInfoResList);
            return getFollowingRes;
        } catch (Exception exception) {
            logger.error("App - getFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetConnectedFollowRes getConnectedFollows(int onlineUserId, int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserStatus(userDao.getUser(userId));
        throwIfConnectedFollowNotExist(onlineUserId, userId);
        try {
            GetConnectedFollowRes getConnectedFollowRes = GetConnectedFollowRes.builder()
                    .followerCount(followDao.getFollowerCount(userId))
                    .followingCount(followDao.getFollowingCount(userId))
                    .connectedCount(followDao.getConnectedFriendCount(onlineUserId, userId))
                    .build();
            List<Integer> followingIdList = followDao.getConnectedFollows(onlineUserId, userId);
            List<GetFollowUserInfoRes> getFollowUserInfoResList = new ArrayList<>();
            followingIdList.stream().forEach(id -> {
                try {
                    getFollowUserInfoResList.add(buildGetFollowUserRes(onlineUserId, id));
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            getConnectedFollowRes.setGetFollowUserInfoResList(getFollowUserInfoResList);
            return getConnectedFollowRes;
        } catch (Exception exception) {
            logger.error("App - getFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userDao.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }

    private void throwIfInvalidUserStatus(User user) throws BaseException {
        if (user.getAccountStatus().equals("INACTIVE")) {
            throw new BaseException(POST_USERS_ACCOUNT_INACTIVE);
        }
        if (user.getAccountStatus().equals("DELETED")) {
            throw new BaseException(POST_USERS_ACCOUNT_DELETED);
        }

    }

    private void throwIfConnectedFollowNotExist(int onlineUserId, int userId) throws BaseException {
        if (onlineUserId == userId) {
            throw new BaseException(GET_FOLLOWS_NO_CONNECTED_FOLLOWS_FOR_ONE_SELF);
        }
        if (followDao.getConnectedFriendCount(onlineUserId, userId) == 0) {
            throw new BaseException(GET_FOLLOWS_NO_CONNECTED_FOLLOWS);
        }
    }

    private GetFollowUserInfoRes buildGetFollowUserRes(int onlineUserId, int id) throws BaseException {
        try {
            User user = userDao.getUser(id);
            return GetFollowUserInfoRes.builder()
                    .userId(user.getUserId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .followStatus(followDao.checkFollowing(onlineUserId, id))
                    .storyStatus(storyDao.checkStory(id))
                    .build();
        } catch (Exception exception) {
            logger.error("App - getFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<Integer> getFollowingIdList(int onlineUserId) throws BaseException {
        try {
            return followDao.getFollowId(onlineUserId);
        } catch (Exception exception) {
            logger.error("App - getFollowingIdList Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getConnectedFriedNickname(int userIdByJwt, int userId) throws BaseException {
        try {
            return followDao.getConnectedFriedNickname(userIdByJwt, userId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriedNickname Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkIfFollowing(int onlineUserId, int findingUserId) throws BaseException {
        try {
            return followDao.checkIfFollowing(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - checkIfFollowing Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.follow.FollowProvider;
import com.example.demo.src.post.PostProvider;
import com.example.demo.src.story.StoryDao;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;
    private final PostProvider postProvider;
    private final FollowProvider followProvider;
    private final StoryDao storyDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService, PostProvider postProvider, FollowProvider followProvider, StoryDao storyDao) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.postProvider = postProvider;
        this.followProvider = followProvider;
        this.storyDao = storyDao;
    }

    public int checkEmailAddress(String email) throws BaseException {
        try {
            return userDao.checkEmailAddress(email);
        } catch (Exception exception) {
            logger.error("App - checkEmailAddress Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try {
            return userDao.checkPhoneNumber(phoneNumber);
        } catch (Exception exception) {
            logger.error("App - checkPhoneNumber Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickName) throws BaseException {
        try {
            return userDao.checkNickname(nickName);
        } catch (Exception exception) {
            logger.error("App - checkNickname Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        if (checkNickname(postLoginReq.getId()) == 0 && checkPhoneNumber(postLoginReq.getId()) == 0 && checkEmailAddress(postLoginReq.getId()) == 0) {
            throw new BaseException(POST_USERS_ID_NOT_EXIST);
        }
        User user;
        try {
            user = userDao.findUserById(postLoginReq.getId());
        } catch (Exception exception) {
            logger.error("App - login Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception exception) {
            logger.error("App - login Provider Decrypt Error", exception);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if (postLoginReq.getPassword().equals(password)) {
            if (user.getAccountStatus().equals("DELETED")) {
                throw new BaseException(POST_USERS_ACCOUNT_DELETED);
            }
            if (user.getAccountStatus().equals("INACTIVE")) {
                userDao.updateUserAccountStatus(user.getUserId(), "ACTIVE");
            }
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    @Transactional
    public GetUserRes getUser(int onlineUserId, int findingUserId) throws BaseException {
        if (checkUserId(findingUserId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
        User user;
        try {
            user = userDao.getUser(findingUserId);
        } catch (Exception exception) {
            logger.error("App - getUser Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        throwIfInvalidUserStatus(user);
        try {
            GetUserRes getUserRes = buildGetUserRes(user);
            getUserRes.setPostCount(postProvider.getPostCount(findingUserId));
            getUserRes.setFollowerCount(followProvider.getFollowerCount(findingUserId));
            getUserRes.setFollowingCount(followProvider.getFollowingCount(findingUserId));
            if(onlineUserId!=findingUserId) {
                if (followProvider.checkIfFollowing(onlineUserId, findingUserId) == 1) {
                    getUserRes.setFollowStatus(1);
                } else {
                    getUserRes.setFollowStatus(0);
                }
            }
            if(storyDao.checkStory(findingUserId)==1){
                getUserRes.setStoryStatus(1);
            }
            if (onlineUserId != findingUserId) {
                getUserRes.setConnectedCount(followProvider.getConnectedFriendCount(onlineUserId, findingUserId));
                getUserRes.setConnectedFriendProfiles(setConnectedFriendProfileList(followProvider.getConnectedFollowId(onlineUserId, findingUserId)));
            }
            return getUserRes;
        } catch (Exception exception) {
            logger.error("App - getUser Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(int userId) throws BaseException {
        try {
            return userDao.checkUserId(userId);
        } catch (Exception exception) {
            logger.error("App - checkUserId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkUserAccountStatus(int userId) throws BaseException {
        try {
            return userDao.checkUserAccountStatus(userId);
        } catch (Exception exception) {
            logger.error("App - checkUserAccountStatus Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostLoginRes identifyUser(GetIdentifyUserReq getIdentifyUserReq) throws BaseException {
        User user;
        try {
            user = userDao.getUser(getIdentifyUserReq.getUserId());
        } catch (Exception exception) {
            logger.error("App - login Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception exception) {
            logger.error("App - identifyUser Provider Decrypt Error", exception);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if (getIdentifyUserReq.getPassword().equals(password)) {
            int userId = getIdentifyUserReq.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, jwt);
        } else {
            throw new BaseException(FAILED_TO_IDENTIFY);
        }

    }

    @Transactional
    public List<GetUserSearchRes> searchByUser(int userIdByJwt, String keyword) throws BaseException {
        try {
            List<GetUserSearchRes> getUserSearchResList = userDao.searchByUser(userIdByJwt,keyword);
            getUserSearchResList.forEach(res -> {
                try {
                    res.setConnectedCount(followProvider.getConnectedFriendCount(userIdByJwt, res.getUserId()));
                    if(res.getConnectedCount()!=0){
                        res.setConnectedFriendNickname(followProvider.getConnectedFriedNickname(userIdByJwt, res.getUserId()));
                    }
                    if(storyDao.checkStory(res.getUserId())==1){
                        res.setStoryStatus(1);
                    }
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                }
            });
            return getUserSearchResList;
        } catch (Exception exception) {
            logger.error("App - searchByUserNickname Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<GetUserProfileRes> setConnectedFriendProfileList(List<Integer> followerIdList) {
        List<GetUserProfileRes> userProfileList = new ArrayList<>();
        followerIdList.stream().forEach(id -> userProfileList.add(userDao.getUserProfile(id)));
        return userProfileList;
    }

    private GetUserRes buildGetUserRes(User user) {
        GetUserRes.GetUserResBuilder builder = GetUserRes.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .birthDate(user.getBirthDate())
                .profileImageUrl(user.getProfileImageUrl())
                .accountStatus(user.getAccountStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt());
        if (user.getPhoneNumber() != null) {
            builder.phoneNumber(user.getPhoneNumber());
        }
        if (user.getEmailAddress() != null) {
            builder.emailAddress(user.getEmailAddress());
        }
        if (user.getName() != null) {
            builder.name(user.getName());
        }
        if (user.getIntroduce() != null) {
            builder.introduce(user.getIntroduce());
        }
        if (user.getGender() != null) {
            builder.gender(user.getGender());
        }
        return builder.build();
    }

    private void throwIfInvalidUserStatus(User user) throws BaseException {
        if (user.getAccountStatus().equals("INACTIVE")) {
            throw new BaseException(POST_USERS_ACCOUNT_INACTIVE);
        }
        if (user.getAccountStatus().equals("DELETED")) {
            throw new BaseException(POST_USERS_ACCOUNT_DELETED);
        }

    }
}
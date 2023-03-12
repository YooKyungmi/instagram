package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.PostPatchFollowRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class FollowService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final FollowProvider followProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider, UserProvider userProvider, JwtService jwtService) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public PostPatchFollowRes addFollows(int userId, int followUserId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserIdDetected(followUserId);
        throwIfInvalidUserStatus(userProvider.checkUserAccountStatus(followUserId));
        try {
            PostPatchFollowRes postPatchFollowRes;
            if (followDao.checkUserFollow(userId,followUserId)==0) {
                postPatchFollowRes = PostPatchFollowRes.builder()
                        .userFollowId(followDao.addFollows(userId, followUserId))
                        .status(1)
                        .build();
            } else {
                int userFollowId = followDao.getUserFollowId(userId, followUserId);
                int result = followDao.patchFollows(1, userId, followUserId);
                if (result == 0) {
                    throw new BaseException(MODIFY_FAIL_USER_FOLLOW);
                }
                postPatchFollowRes = PostPatchFollowRes.builder()
                        .userFollowId(userFollowId)
                        .status(1)
                        .build();
            }
            return postPatchFollowRes;
        } catch (Exception exception) {
            logger.error("App - addFollows Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPatchFollowRes patchFollows(Integer userId, Integer followUserId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserIdDetected(followUserId);
        throwIfInvalidUserStatus(userProvider.checkUserAccountStatus(followUserId));
        try {
            if (followDao.checkUserFollow(userId,followUserId)==0) {
                throw new BaseException(PATCH_FOLLOWS_NOT_EXIST);
            }
            Integer userFollowId = followDao.getUserFollowId(userId, followUserId);
            int result = followDao.patchFollows(0, userId, followUserId);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_FOLLOW);
            }
            PostPatchFollowRes postPatchFollowRes = PostPatchFollowRes.builder()
                    .userFollowId(userFollowId)
                    .status(0)
                    .build();
            return postPatchFollowRes;
        } catch (Exception exception) {
            logger.error("App - patchFollows Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }

    private void throwIfInvalidUserStatus(String status) throws BaseException {
        if (status.equals("INACTIVE")) {
            throw new BaseException(POST_USERS_ACCOUNT_INACTIVE);
        }
        if (status.equals("DELETED")) {
            throw new BaseException(POST_USERS_ACCOUNT_DELETED);
        }

    }

}

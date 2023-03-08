package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowProvider implements com.example.demo.src.user.spi.FollowProvider {

    private final FollowDao followDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FollowProvider(FollowDao followDao) {
        this.followDao = followDao;
    }

    @Override
    public int getFollowerCount(int userId) throws BaseException {
        try {
            return followDao.getFollowerCount(userId);
        } catch (Exception exception) {
            logger.error("App - getFollowerCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public int getFollowingCount(int userId) throws BaseException {
        try {
            return followDao.getFollowingCount(userId);
        } catch (Exception exception) {
            logger.error("App - getFollowingCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public int getConnectedFriendCount(int onlineUserId, int findingUserId) throws BaseException {
        try {
            return followDao.getConnectedFriendCount(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriendCount Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public List<Integer> getConnectedFollowId(int onlineUserId, int findingUserId) throws BaseException {
        try {
            return followDao.getConnectedFollowId(onlineUserId, findingUserId);
        } catch (Exception exception) {
            logger.error("App - getConnectedFriendId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 구현하느라 대충 써놨습니다!!
    public List<Integer> getFollowerList(int userId){
        return Arrays.asList(1);
    }
}
